package ru.malpen.toggler.internal;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

import ru.malpen.toggler.events.IEvent;

public interface IEventStorage {
    void put(IEvent event);
    void clear();

    Queue<IEvent> getAllReadyToSync();
}
