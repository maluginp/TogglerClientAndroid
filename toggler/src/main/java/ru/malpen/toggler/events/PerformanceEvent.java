package ru.malpen.toggler.events;

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

        jEvent.put("id", guid);
        jEvent.put("type", type.getNumber());
        jEvent.put("duration", duration);
        jEvent.put("tag", tag);
        jEvent.put("tag", result);
        jEvent.put("posId", posId);
        jEvent.put("timestamp", timestamp);

        return jEvent.toString();
    }

    @Override
    public String getPath() {
        return "/v1/collector/performance";
    }
}
