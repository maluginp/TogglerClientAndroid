package ru.malpen.toggler.events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import ru.malpen.toggler.HttpPacket;
import ru.malpen.toggler.Utils;

public class HttpEvent implements IEvent {
    private final String guid;
    private final String path;
    private final String method;
    private final int statusCode;
    private final HttpPacket request;
    private final HttpPacket response;

    private final long posId;
    private final long timestamp;

    public HttpEvent(String path, String method, int statusCode, HttpPacket request, HttpPacket response, long posId) {
        this.guid = Utils.generateGuid();
        this.timestamp =  Utils.getUtcTimestamp();
        this.path = path;
        this.method = method;
        this.statusCode = statusCode;
        this.request = request;
        this.response = response;
        this.posId = posId;
    }

    @Override
    public String toJson() throws JSONException {
        JSONObject jEvent = new JSONObject();

        jEvent.put("guid", guid);
        jEvent.put("path", path);
        jEvent.put("method", method);
        jEvent.put("statusCode", statusCode);
        jEvent.put("request", getJsonHttpPacket(request));
        jEvent.put("response", getJsonHttpPacket(response));
        jEvent.put("posId", posId);
        jEvent.put("timestamp", timestamp);

        JSONArray jEvents = new JSONArray();
        jEvents.put(jEvent);

        JSONObject jRoot = new JSONObject();
        jRoot.put("events", jEvents);

        return jRoot.toString();
    }

    @Override
    public String getPath() {
        return "/v1/collector/http";
    }


    private JSONObject getJsonHttpPacket(HttpPacket packet) throws JSONException {
        JSONObject jEvent = new JSONObject();
        jEvent.put("body", packet.getBody());

        JSONObject jHeaders = new JSONObject();

        for (Map.Entry<String, String> header : packet.getHeaders().entrySet()) {
            jHeaders.put(header.getKey(), header.getValue());
        }

        jEvent.put("headers", jHeaders);

        return jEvent;
    }
}
