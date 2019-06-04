package ru.malpen.toggler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.json.JSONException;

import java.io.IOException;


public class Toggler {
    private final String KEY_USER = "TOGGLER_KEY_USER";
    private final String KEY_CONFIG = "TOGGLER_KEY_CONFIG";

    private final SharedPreferences prefs;
    private final LoadConfigThread loadConfigThread;
    private final UserConvertor userConvertor = new UserConvertor();
    private final UserConfigJsonConvertor userConfigJsonConvertor = new UserConfigJsonConvertor();
    private final TogglerWebClient webClient;
    private final AsyncEventWorker eventsWorker;

    private UserConfig mConfig = null;
    private static Toggler sToggler = null;

    public static Toggler getInstance() {
        if (sToggler == null) {
            throw new IllegalArgumentException("Toggler instance is not initialized. Call init() first!");
        }

        return sToggler;
    }

    public static Toggler init(Context context, String appKey) throws IOException {
        sToggler = new Toggler(context, appKey);
        return sToggler;
    }

    public boolean isFeatureEnabled(String name) {
        if (mConfig != null) {
            for (FeatureConfig feature : mConfig.getFeatures()) {
                if (name.equalsIgnoreCase(feature.getName())) {
                    if (feature.getValue().equalsIgnoreCase("true")) {
                        trackEvent(name, TogglerEvent.FeatureCheck, "", "");
                        return true;
                    }

                    return false;
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

    public void trackEvent(String featureKey, TogglerEvent event, String action, String message) {
        eventsWorker.addEvent(new TrackEvent()
                .setId(Utils.generateGuid())
                .setEvent(event.name())
                .setAction(action)
                .setFeature(featureKey)
                .setMessage(message)
                .setTimestamp(Utils.getUtcTimestamp())
        );
    }

    public void trackLog(String featureKey, String format, Object... args) {
        String message = String.format(format, args);
        trackEvent(featureKey, TogglerEvent.Log, "", message);
    }

    public void trackError(String featureKey, Exception ex) {
        String message = ex.getMessage();
        trackEvent(featureKey, TogglerEvent.Error, "", message);
    }

    public void trackError(String featureKey, String format, Object... args) {
        String message = String.format(format, args);
        trackEvent(featureKey, TogglerEvent.Error, "", message);
    }


    //region Private Area
    private static SharedPreferences getPreferences(Context context) {
        String packageName = context.getPackageName();
        return context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
    }

    private Toggler(Context context, String appKey) throws IOException {
        this.prefs = getPreferences(context);
        this.webClient = new TogglerWebClient();

        loadConfigThread = new LoadConfigThread(webClient, onLoadConfig, appKey, getUser(), getDeviceInfo());
        loadConfigThread.start();

        eventsWorker = new AsyncEventWorker(
                context, appKey, getUser(), getDeviceInfo(), webClient
        );

        mConfig = getConfig();
    }

    private DeviceInfo getDeviceInfo() {
        return new DeviceInfo()
                .setVersion(Build.VERSION.RELEASE)
                .setModel(Build.MODEL)
                ;
    }

    private TogglerUser getUser() {
        try {
            String json = prefs.getString(KEY_USER, "");
            return userConvertor.fromJson(json);
        } catch (JSONException ignore) {
            return null;
        }
    }

    private UserConfig getConfig() {
        try {
            String json = prefs.getString(KEY_CONFIG, "");
            return userConfigJsonConvertor.fromJson(json);
        } catch (JSONException ignore) {
            return null;
        }
    }

    private LoadConfigThread.OnLoadConfig onLoadConfig = new LoadConfigThread.OnLoadConfig() {
        @Override
        public void onConfigUpdated(UserConfig config) {
            mConfig = config;

            try {
                prefs.edit().putString(KEY_CONFIG, userConfigJsonConvertor.toJson(config)).apply();
            } catch (JSONException ignore) {

            }
        }
    };
//endregion

}
