package ru.malpen.toggler.internal.web;

import java.io.IOException;

import ru.malpen.toggler.events.IEvent;

public interface IWebClient {
    boolean send(IEvent event) throws IOException;
}
