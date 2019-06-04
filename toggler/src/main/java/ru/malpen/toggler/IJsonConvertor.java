package ru.malpen.toggler;

import org.json.JSONException;

interface IJsonConvertor<Model> {
    String toJson(Model model) throws JSONException;
    Model fromJson(String json) throws JSONException;
}
