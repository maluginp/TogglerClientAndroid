package ru.malpen.toggler.events;

import org.json.JSONException;

public interface IEvent {

    String toJson() throws JSONException;

    String getPath();
}
