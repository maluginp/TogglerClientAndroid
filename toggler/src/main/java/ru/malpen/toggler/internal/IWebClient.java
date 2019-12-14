package ru.malpen.toggler.internal;

import java.io.IOException;

import ru.malpen.toggler.events.IEvent;

public interface IWebClient {
    boolean sendEvent(IEvent event) throws IOException;
}
