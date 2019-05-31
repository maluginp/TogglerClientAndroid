package ru.malpen.toggler.internal;

import org.json.JSONException;

public interface IJsonConvertor<Model> {
    String toJson(Model model) throws JSONException;
    Model fromJson(String json) throws JSONException;
}
