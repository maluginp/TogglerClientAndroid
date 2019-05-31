package ru.malpen.toggler;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;

import ru.malpen.toggler.internal.FeatureConfig;
import ru.malpen.toggler.internal.LoadConfigThread;
import ru.malpen.toggler.internal.TogglerUser;
import ru.malpen.toggler.internal.UserConfig;
import ru.malpen.toggler.internal.UserConfigJsonConvertor;
import ru.malpen.toggler.internal.UserConvertor;

public class Toggler implements LoadConfigThread.OnLoadConfig {
    private final String KEY_USER = "TOGGLER_KEY_USER";
    private final String KEY_CONFIG = "TOGGLER_KEY_CONFIG";

    private final SharedPreferences prefs;
    private final LoadConfigThread loadConfigThread;
    private final UserConvertor userConvertor = new UserConvertor();
    private final UserConfigJsonConvertor userConfigJsonConvertor = new UserConfigJsonConvertor();

    private UserConfig mConfig = null;
    private static Toggler sToggler = null;

    public static Toggler getInstance() {
        if (sToggler == null) {
            throw new IllegalArgumentException("Toggler instance is not initialized. Call init() first!");
        }

        return sToggler;
    }

    public static Toggler init(Context context, String appKey) {
        sToggler = new Toggler(context, appKey);
        return sToggler;
    }

    public boolean isFeatureEnabled(String name) {
        if (mConfig != null) {
            for (FeatureConfig feature : mConfig.getFeatures()) {
                if (name.equalsIgnoreCase(feature.getName())) {
                    return (feature.getValue().equalsIgnoreCase("true"));
                }
            }
        }

        return false;
    }

    public void setUser(TogglerUser user) {
        try {
            prefs.edit().putString(KEY_USER, userConvertor.toJson(user)).apply();
        } catch (JSONException ignore) {

        }
        loadConfigThread.setUser(user);
    }

    private static SharedPreferences getPreferences(Context context) {
        String packageName = context.getPackageName();
        return context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
    }

    private Toggler(Context context, String appKey) {
        this.prefs = getPreferences(context);
        loadConfigThread = new LoadConfigThread(this,  appKey, getUser());
        loadConfigThread.start();

        mConfig = getConfig();
    }


//region Private methods
    private TogglerUser getUser() {
        try {
            String json =  prefs.getString(KEY_USER, "");
            return userConvertor.fromJson(json);
        } catch (JSONException ignore) {
            return null;
        }
    }

    @Override
    public void onConfigUpdated(UserConfig config) {
        mConfig = config;

        try {
            prefs.edit().putString(KEY_CONFIG, userConfigJsonConvertor.toJson(config)).apply();
        } catch (JSONException ignore) {

        }
    }

    private UserConfig getConfig() {
        try {
            String json =  prefs.getString(KEY_CONFIG, "");
            return userConfigJsonConvertor.fromJson(json);
        } catch (JSONException ignore) {
            return null;
        }
    }
//endregion

}
