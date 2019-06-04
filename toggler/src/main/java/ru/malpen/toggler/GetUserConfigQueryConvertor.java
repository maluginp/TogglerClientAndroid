package ru.malpen.toggler;

import org.json.JSONException;
import org.json.JSONObject;

class GetUserConfigQueryConvertor implements IJsonConvertor<GetUserConfigQuery> {
    @Override
    public String toJson(GetUserConfigQuery query) throws JSONException {
        JSONObject jQuery = new JSONObject();
        jQuery.put("appKey", query.getAppKey());

        TogglerUser user = query.getUser();
        if (user != null) {
            JSONObject jUser = new JSONObject();
            jUser.put("id", user.getId());
            jUser.put("username", user.getUserName());
            jQuery.put("user", jUser);
        }

        DeviceInfo device = query.getDeviceInfo();
        if (device != null) {
            JSONObject jDevice = new JSONObject();
            jDevice.put("platform", device.getPlatform());
            jDevice.put("version", device.getVersion());
            jDevice.put("model", device.getModel());
            jQuery.put("device", jDevice);
        }

        return jQuery.toString();
    }

    @Override
    public GetUserConfigQuery fromJson(String json) throws JSONException {
        throw new JSONException("Not support");
    }
}
