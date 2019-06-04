package ru.malpen.toggler;

public class TrackEvent {
    private String id;
    private String feature;
    private String event;
    private String action;
    private String message;
    private long timestamp;


    public String getId() {
        return id;
    }

    public TrackEvent setId(String id) {
        this.id = id;
        return this;
    }

    public String getFeature() {
        return feature;
    }

    public TrackEvent setFeature(String feature) {
        this.feature = feature;
        return this;
    }

    public String getEvent() {
        return event;
    }

    public TrackEvent setEvent(String event) {
        this.event = event;
        return this;
    }

    public String getAction() {
        return action;
    }

    public TrackEvent setAction(String action) {
        this.action = action;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public TrackEvent setMessage(String message) {
        this.message = message;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TrackEvent setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
