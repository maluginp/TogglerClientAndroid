package ru.malpen.toggler.internal;

import org.json.JSONException;
import org.json.JSONObject;

public class GetUserConfigQueryConvertor implements IJsonConvertor<GetUserConfigQuery> {
    @Override
    public String toJson(GetUserConfigQuery getUserConfigQuery) throws JSONException {
        JSONObject jQuery = new JSONObject();
        jQuery.put("appKey", getUserConfigQuery.getAppKey());

        if (getUserConfigQuery.getUser() != null) {
            JSONObject jUser = new JSONObject();
            jUser.put("id", getUserConfigQuery.getUser().getId());
            jUser.put("userName", getUserConfigQuery.getUser().getUserName());
            jQuery.put("user", jUser);
        }

        return jQuery.toString();
    }

    @Override
    public GetUserConfigQuery fromJson(String json) throws JSONException {
        throw new JSONException("Not support");
    }
}
