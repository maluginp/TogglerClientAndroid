package ru.malpen.toggler.internal;

import org.json.JSONException;
import org.json.JSONObject;

public class UserConvertor implements IJsonConvertor<TogglerUser> {
    @Override
    public String toJson(TogglerUser user) throws JSONException {

        JSONObject jUser = new JSONObject();
        jUser.put("id", user.getId());
        jUser.put("userName", user.getUserName());

        return jUser.toString();
    }

    @Override
    public TogglerUser fromJson(String json) throws JSONException {
        JSONObject jUser = new JSONObject(json);

        return new TogglerUser()
                .setId(jUser.getString("id"))
                .setUserName(jUser.getString("userName"));
    }
}
