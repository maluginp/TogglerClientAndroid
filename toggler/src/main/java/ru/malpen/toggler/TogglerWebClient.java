package ru.malpen.toggler;

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

class TogglerWebClient {
    private final GetUserConfigQueryConvertor getUserConfigQueryConvertor = new GetUserConfigQueryConvertor();
    private final UserConfigJsonConvertor userConfigJsonConvertor = new UserConfigJsonConvertor();

    private static final String API_URL = "http://192.168.100.4:7000/api";

    private static final int CONNECTION_TIMEOUT = 5;

    UserConfig getUserConfig(String appKey, TogglerUser user, DeviceInfo deviceInfo) throws IOException {
        GetUserConfigQuery query = new GetUserConfigQuery()
                .setAppKey(appKey)
                .setUser(user)
                .setDeviceInfo(deviceInfo);

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
                    return userConfigJsonConvertor.fromJson(data);

                } catch (JSONException e) {
                        e.printStackTrace();
                    return null;
                }


            } catch (IOException exception) {
                throw exception;
            } finally {
                httpURLConnection.disconnect();
            }

        } catch (JSONException| MalformedURLException | ProtocolException e) {
//            delay(DELAY_ERROR);
            return null;
        }
    }

    boolean sendEvent(String appKey, String userId, DeviceInfo deviceInfo, String event) throws IOException {
        StringBuilder json = new StringBuilder();

        json.append("{");

        json.append("\"appKey\":");
        json.append("\"");
        json.append(appKey);
        json.append("\",");

        json.append("\"userId\":");
        json.append("\"");
        json.append(userId);
        json.append("\",");


        json.append("\"device\":{");

        json.append("\"platform\":");
        json.append("\"");
        json.append(deviceInfo.getPlatform());
        json.append("\",");

        json.append("\"version\":");
        json.append("\"");
        json.append(deviceInfo.getVersion());
        json.append("\",");

        json.append("\"model\":");
        json.append("\"");
        json.append(deviceInfo.getModel());
        json.append("\"");

        json.append("},");

        json.append("\"events\":[");
        json.append(event);
        json.append("]");

        json.append("}");


        try {
            URL url = new URL(API_URL + "/v1/client/events");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT*1000);
            httpURLConnection.setReadTimeout(CONNECTION_TIMEOUT*1000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            try {
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(json.toString());
                writer.flush();
                writer.close();
                os.close();

                httpURLConnection.connect();

//                String data = readFullyAsString(httpURLConnection.getInputStream(), "UTF-8");
                int status = httpURLConnection.getResponseCode();

                return status == 200;

            } catch (IOException exception) {
                throw exception;
            } finally {
                httpURLConnection.disconnect();
            }

        } catch (MalformedURLException | ProtocolException e) {
//            delay(DELAY_ERROR);
            return false;
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




}
