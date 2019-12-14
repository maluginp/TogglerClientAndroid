package ru.malpen.toggler;

import android.content.Context;

import ru.malpen.toggler.events.ErrorEvent;

public class Toggler {
    private final AsyncEventWorker eventsWorker;

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

    public void trackEvent(String tag, String message, Long posId) {
        if (posId != null) {
            eventsWorker.addEvent(new ErrorEvent(tag, message, posId));
        }
    }

    //region Private Area
    private Toggler(Context context, String appKey) {
        this.eventsWorker = new AsyncEventWorker(context, new TogglerWebClient());
    }

//endregion

}
