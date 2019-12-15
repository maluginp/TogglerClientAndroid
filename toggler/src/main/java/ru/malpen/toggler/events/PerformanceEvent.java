package ru.malpen.toggler.events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.malpen.toggler.PerformanceType;
import ru.malpen.toggler.Utils;

public class PerformanceEvent implements IEvent {
    private final String guid;
    private final PerformanceType type;
    private final int duration;
    private final String tag;
    private final String result;

    private final long posId;
    private final long timestamp;

    public PerformanceEvent(PerformanceType type, int duration, String tag, String result, long posId) {
        this.guid = Utils.generateGuid();
        this.timestamp =  Utils.getUtcTimestamp();
        this.type = type;
        this.duration = duration;
        this.tag = tag;
        this.result = result;
        this.posId = posId;
    }

    @Override
    public String toJson() throws JSONException {

        JSONObject jEvent = new JSONObject();

        jEvent.put("guid", guid);
        jEvent.put("type", type.getNumber());
        jEvent.put("duration", duration);
        jEvent.put("tag", tag);
        jEvent.put("result", result);
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
        return "/v1/collector/performance";
    }
}
