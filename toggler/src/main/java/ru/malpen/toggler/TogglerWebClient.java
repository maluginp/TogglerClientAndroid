package ru.malpen.toggler;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import ru.malpen.toggler.events.IEvent;
import ru.malpen.toggler.internal.IWebClient;

class TogglerWebClient implements IWebClient {
    private static final String API_URL = "http://192.168.100.4:7000/api";

    private static final int CONNECTION_TIMEOUT = 5;

    @Override
    public boolean sendEvent(IEvent event) throws IOException {
        String body;

        try {
            body = event.toJson();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        try {
            URL url = new URL(API_URL + event.getPath());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT*1000);
            httpURLConnection.setReadTimeout(CONNECTION_TIMEOUT*1000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            try {
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(body);
                writer.flush();
                writer.close();
                os.close();

                httpURLConnection.connect();

                int status = httpURLConnection.getResponseCode();

                return status == 200;

            } catch (IOException exception) {
                throw exception;
            } finally {
                httpURLConnection.disconnect();
            }

        } catch (MalformedURLException | ProtocolException e) {
            return false;
        }
    }
 }
