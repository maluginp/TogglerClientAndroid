package ru.malpen.toggler.internal;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LoadConfigThread extends Thread {
    private static final String API_URL = "http://192.168.100.28:7000/api";
    private static final int DELAY_SUCCESS = 5 * 1000; //15 * 60 * 1000;
    private static final int DELAY_ERROR = 1 * 1000; // 30 * 1000;

    private static final int CONNECTION_TIMEOUT = 5;

    private final GetUserConfigQueryConvertor getUserConfigQueryConvertor = new GetUserConfigQueryConvertor();
    private final UserConfigJsonConvertor userConfigJsonConvertor = new UserConfigJsonConvertor();

    private TogglerUser mUser;
    private final OnLoadConfig onLoadConfig;
    private String mAppKey;

    public LoadConfigThread(OnLoadConfig onLoadConfig, String appKey, TogglerUser user) {
        this.onLoadConfig = onLoadConfig;
        this.mAppKey = appKey;
        this.mUser = user;
    }

    public void setUser(TogglerUser user) {
        this.mUser = user;
    }

    @Override
    public void run() {
        while (true) {
            GetUserConfigQuery query = new GetUserConfigQuery()
                    .setAppKey(mAppKey)
                    .setUser(mUser);

            try {
                String request = getUserConfigQueryConvertor.toJson(query);

                URL url = new URL(API_URL + "/v1/client/config");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT*1000);
                httpURLConnection.setReadTimeout(CONNECTION_TIMEOUT*1000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                try {
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(request);
                    writer.flush();
                    writer.close();
                    os.close();

                    httpURLConnection.connect();

                    String data = readFullyAsString(httpURLConnection.getInputStream(), "UTF-8");
                    int status = httpURLConnection.getResponseCode();

                    try {
                        UserConfig config = userConfigJsonConvertor.fromJson(data);

                        if (this.onLoadConfig != null) {
                            this.onLoadConfig.onConfigUpdated(config);
                        }

                        delay(DELAY_SUCCESS);


                    } catch (JSONException e) {
//                        e.printStackTrace();
                        delay(DELAY_ERROR);
                    }


                } catch (Exception ignore) {
                    delay(DELAY_ERROR);
                } finally {
                    httpURLConnection.disconnect();
                }

            } catch (JSONException| MalformedURLException| ProtocolException e) {
                delay(DELAY_ERROR);
            } catch (IOException e) {
                delay(DELAY_ERROR);
            }
        }
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


    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignore) {
            //
        }
    }

    public interface OnLoadConfig {
        void onConfigUpdated(UserConfig config);
    }
}
