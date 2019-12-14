package ru.malpen.toggler;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import ru.malpen.toggler.events.IEvent;

public class AsyncEventWorker {
    private static final String TAG = "TogglerEvents";

    private static final int RECONNECT_WAIT = 100; // milliseconds.
    private static final int MAX_QUEUE_POLL_TIME = 1000; // milliseconds.
    private static final int QUEUE_SIZE = 32768;
    private static final int LOG_LENGTH_LIMIT = 65536;
    private static final int MAX_NETWORK_FAILURES_ALLOWED = 3;
    private static final int MAX_RECONNECT_ATTEMPTS = 3;
    private static final String QUEUE_OVERFLOW = "Buffer Queue Overflow. Message Dropped!";

    private boolean started = false;
    private SocketAppender appender;
    private ArrayBlockingQueue<IEvent> queue;
    private EventStorage eventStorage;

    AsyncEventWorker(Context context, TogglerWebClient webClient) throws IOException {
        queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        eventStorage = new EventStorage(context);

        appender = new SocketAppender(webClient);
        appender.start();
        started = true;
    }

    void addEvent(IEvent event) {
        if (!this.started) {
            appender.start();
            started = true;
        }

        tryOfferToQueue(event);
    }

    private void close(long queueFlushTimeout) {
        if (queueFlushTimeout < 0) {
            throw new IllegalArgumentException("queueFlushTimeout must be greater or equal to zero");
        }

        long now = System.currentTimeMillis();

        while (!queue.isEmpty()) {
            if (queueFlushTimeout != 0) {
                if (System.currentTimeMillis() - now >= queueFlushTimeout) {
                    // The timeout expired - need to stop the appender.
                    break;
                }
            }
        }
        appender.interrupt();
        started = false;
    }

    public void close() {
        close(0);
    }

    private static boolean checkTokenFormat(String token) {
        return Utils.checkValidUUID(token);
    }

    private void tryOfferToQueue(IEvent event) throws RuntimeException {
        if (!queue.offer(event)) {
            Log.e(TAG, "The queue is full - will try to drop the oldest message in it.");
            queue.poll();

            if (!queue.offer(event)) {
                throw new RuntimeException(QUEUE_OVERFLOW);
            }
        }
    }

    private class SocketAppender extends Thread {

        // Formatting constants
        private static final String LINE_SEP_REPLACER = "\u2028";
        private TogglerWebClient webClient;

        SocketAppender(TogglerWebClient webClient) {
            super("Toggler event appender");
            this.webClient = webClient;
            // Don't block shut down
            setDaemon(true);

        }

        private void openConnection() throws IOException, InstantiationException { }

        private boolean reopenConnection(int maxReConnectAttempts) throws InterruptedException, InstantiationException {
            if (maxReConnectAttempts < 0) {
                throw new IllegalArgumentException("maxReConnectAttempts value must be greater or equal to zero");
            }

            // Close the previous connection
            closeConnection();

            for (int attempt = 0; attempt < maxReConnectAttempts; ++attempt) {
                try {

                    openConnection();
                    return true;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Thread.sleep(RECONNECT_WAIT);
            }

            return false;
        }


        private void closeConnection() { }

        private boolean tryUploadNotSyncedEvent() {
            Queue<IEvent> events = new ArrayDeque<>();

            try {

                events = eventStorage.getAllReadyToSync();
                for (IEvent event = events.peek(); event != null; event = events.peek()) {
                    if (webClient.send(event)) {
                        events.poll(); // Remove the message after successful sending.
                    } else {
                        throw new IOException("Failed sending");
                    }
                }

                eventStorage.clear();

                return true;

            } catch (IOException ioEx) {
                Log.e(TAG, "Cannot upload logs to the server. Error: " + ioEx.getMessage());

                eventStorage.clear();
                for (IEvent event : events) {
                    eventStorage.put(event);
                }
            }

            return false;
        }

        @Override
        public void run() {
            try {

                // Open connection
                reopenConnection(MAX_RECONNECT_ATTEMPTS);

                Queue<IEvent> prevSavedLogs = eventStorage.getAllReadyToSync();
                eventStorage.clear();

                int numFailures = 0;
                boolean connectionIsBroken = false;
                IEvent event = null;

                // Send data in queue
                while (true) {

                    // First we need to send the logs from the local storage -
                    // they haven't been sent during the last session, so need to
                    // come first.
                    if (prevSavedLogs.isEmpty()) {

                        // Try to take data from the queue if there are no logs from
                        // the local storage left to send.
                        event = queue.poll(MAX_QUEUE_POLL_TIME, TimeUnit.MILLISECONDS);

                    } else {

                        // Getting messages from the previous session one by one.
                        event = prevSavedLogs.poll();
                    }

                    // Send data, reconnect if needed.
                    while (true) {

                        try {

                            // If we have broken connection, then try to re-connect and send
                            // all logs from the local storage. If succeeded - reset numFailures.
                            if (connectionIsBroken && reopenConnection(MAX_RECONNECT_ATTEMPTS)) {
                                if (tryUploadNotSyncedEvent()) {
                                    connectionIsBroken = false;
                                    numFailures = 0;
                                }
                            }

                            if (event != null) {
                                if (webClient.send(event)) {
                                    event = null;
                                } else {
                                    throw new IOException("Sending failed");
                                }
                            }

                        } catch (IOException e) {

                            if (numFailures >= MAX_NETWORK_FAILURES_ALLOWED) {
                                connectionIsBroken = true; // Have tried to reconnect for MAX_NETWORK_FAILURES_ALLOWED
                                // times and failed, so assume, that we have no link to the
                                // server at all...
                                // ... and put the current message to the local storage.
                                eventStorage.put(event);
                                event = null;
                            } else {
                                ++numFailures;

                                // Try to re-open the lost connection.
                                reopenConnection(MAX_RECONNECT_ATTEMPTS);
                            }

                            continue;
                        }

                        break;
                    }
                }
            } catch (InterruptedException e) {
                // We got interrupted, stop.

            } catch (InstantiationException e) {
                Log.e(TAG, "Cannot instantiate Toggler due to improper configuration. Error: " + e.getMessage());

                // Save all existing logs to the local storage.
                // There is nothing we can do else in this case.
                IEvent event = queue.poll();
                while (event != null) {
                    eventStorage.put(event);
                    event = queue.poll();
                }
            }

            closeConnection();
        }
    }


}