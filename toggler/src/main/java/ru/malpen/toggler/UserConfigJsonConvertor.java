package ru.malpen.toggler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class UserConfigJsonConvertor implements IJsonConvertor<UserConfig> {
    @Override
    public String toJson(UserConfig userConfig) throws JSONException {
        JSONObject jObject = new JSONObject();

        JSONArray jFeatures = new JSONArray();

        for (FeatureConfig feature : userConfig.getFeatures()) {
            JSONObject jFeature = new JSONObject();
            jFeature.put("name", feature.getName());
            jFeature.put("type", feature.getType());
            jFeature.put("value", feature.getValue());

            jFeatures.put(jFeature);
        }

        jObject.put("features", jFeatures);


        return jObject.toString();
    }

    @Override
    public UserConfig fromJson(String json) throws JSONException {
        List<FeatureConfig> features = new ArrayList<>();

        JSONObject jObject = new JSONObject(json);

        JSONArray jFeatures = jObject.getJSONArray ("features");

        for (int i = 0; i < jFeatures.length(); i++) {
            JSONObject jFeature = jFeatures.getJSONObject(i);

            FeatureConfig feature = new FeatureConfig()
                    .setName(jFeature.getString("name"))
                    .setType(jFeature.getString("type"))
                    .setValue(jFeature.getString("value"));

            features.add(feature);
        }


        return new UserConfig(features);
    }
}
