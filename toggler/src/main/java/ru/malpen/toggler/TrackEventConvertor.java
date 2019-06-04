package ru.malpen.toggler;

import org.json.JSONException;
import org.json.JSONObject;

public class TrackEventConvertor implements IJsonConvertor<TrackEvent> {
    @Override
    public String toJson(TrackEvent event) throws JSONException {
        JSONObject jEvent = new JSONObject();

        jEvent.put("id", event.getId());
        jEvent.put("feature", event.getFeature());
        jEvent.put("event", event.getEvent());
        jEvent.put("action", event.getAction());
        jEvent.put("message", event.getMessage());
        jEvent.put("timestamp", event.getTimestamp());

        return jEvent.toString();
    }

    @Override
    public TrackEvent fromJson(String json) throws JSONException {
        throw new JSONException("Not support");
    }
}
