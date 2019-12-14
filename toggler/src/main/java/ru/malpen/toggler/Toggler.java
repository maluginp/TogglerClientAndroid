package ru.malpen.toggler;

import android.content.Context;

import ru.malpen.toggler.events.ErrorEvent;
import ru.malpen.toggler.events.HttpEvent;
import ru.malpen.toggler.events.PerformanceEvent;
import ru.malpen.toggler.events.WarningEvent;

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

    public void trackWarning(String tag, String message, Long posId) {
        if (posId != null) {
            eventsWorker.addEvent(new WarningEvent(tag, message, posId));
        }
    }

    public void trackPerformance(PerformanceType type, int duration, String tag, String result, Long posId) {
        if (posId != null) {
            eventsWorker.addEvent(new PerformanceEvent(type, duration, tag, result, posId));
        }
    }

    public void trackHttp(String path, String method, int statusCode, HttpPacket request, HttpPacket response, Long posId) {
        if (posId != null) {
            eventsWorker.addEvent(new HttpEvent(path, method, statusCode, request, response, posId));
        }
    }

    //region Private Area
    private Toggler(Context context, String appKey) {
        this.eventsWorker = new AsyncEventWorker(context, new TogglerWebClient());
    }

//endregion

}
