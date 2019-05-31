package ru.malpen.toggler.internal.web;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetHttpRequestTask extends AsyncTask<Void,Integer, Response> implements IWebClientRequest<String> {

    private final String queryUrl;
    private final int connectTimeout;
    private final int readTimeout;
    private IWebCallback<WebResponse<String>> onCompletion;

    public GetHttpRequestTask(String queryUrl, int connectTimeout, int readTimeout) {
        this.queryUrl = queryUrl;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    @Override
    protected Response doInBackground(Void... params) {
        try {
            URL url = new URL(queryUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(connectTimeout*1000);
            httpURLConnection.setReadTimeout(readTimeout*1000);

            try {
                httpURLConnection.connect();

                String data = readFullyAsString(httpURLConnection.getInputStream(), "UTF-8");
                int status = httpURLConnection.getResponseCode();

                return new Response(status, data);

            } catch (Exception e) {
                return new Response(e);
            } finally {
                // this is done so that there are no open connections left when this task is going to complete
                httpURLConnection.disconnect();
            }


        } catch (Exception e){
            return new Response(e);
        }
    }

    @Override
    protected void onPostExecute(Response response) {
        if (onCompletion != null) {
            if (response.isFail()) {
                onCompletion.failure(response.getException());
            } else {
                onCompletion.success(new WebResponse<>(response.getStatus(), response.getData()));
            }
        }
    }

    @Override
    public void cancel() {
        cancel(true);
    }

    @Override
    public void execute(IWebCallback<WebResponse<String>> completion) {
        this.onCompletion = completion;
        super.execute();
    }

    String readFullyAsString(InputStream inputStream, String encoding)
            throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    byte[] readFullyAsBytes(InputStream inputStream)
            throws IOException {
        return readFully(inputStream).toByteArray();
    }

    private ByteArrayOutputStream readFully(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }
}