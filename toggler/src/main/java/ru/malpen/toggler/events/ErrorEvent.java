package ru.malpen.toggler.events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.malpen.toggler.Utils;

public class ErrorEvent implements IEvent {
    private final String guid;
    private final String tag;
    private final String message;
    private final long posId;
    private final long timestamp;

    public ErrorEvent(String tag, String message, long posId) {
        this.guid = Utils.generateGuid();
        this.tag = tag;
        this.message = message;
        this.posId = posId;
        this.timestamp =  Utils.getUtcTimestamp();
    }

    @Override
    public String toJson() throws JSONException {

        JSONObject jEvent = new JSONObject();

        jEvent.put("guid", guid);
        jEvent.put("tag", tag);
        jEvent.put("message", message);
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
        return "/v1/collector/errors";
    }
}
