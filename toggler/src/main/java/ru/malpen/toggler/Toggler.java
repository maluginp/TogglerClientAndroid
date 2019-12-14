package ru.malpen.toggler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.json.JSONException;

import java.io.IOException;

import ru.malpen.toggler.events.ErrorEvent;


public class Toggler {
    private final String KEY_USER = "TOGGLER_KEY_USER";
    private final String KEY_CONFIG = "TOGGLER_KEY_CONFIG";

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

    public void trackEvent(String tag, String message, Long posId) {
        if (posId != null) {
            eventsWorker.addEvent(new ErrorEvent(tag, message, posId));
        }
    }

    //region Private Area
    private Toggler(Context context, String appKey) throws IOException {
        this.webClient = new TogglerWebClient();
        this.eventsWorker = new AsyncEventWorker(context, webClient);
    }

//endregion

}
