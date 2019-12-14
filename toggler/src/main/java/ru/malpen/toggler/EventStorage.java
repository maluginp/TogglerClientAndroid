package ru.malpen.toggler;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import ru.malpen.toggler.events.IEvent;
import ru.malpen.toggler.internal.IEventStorage;

class EventStorage implements IEventStorage {

    private static final String TAG = "TogglerEvents";
    private final Context context;
    private final List<IEvent> events;


    EventStorage(Context context) {
        this.context = context;
        this.events = new ArrayList<>();
    }

    @Override
    public void put(IEvent event) {
        events.add(event);
    }

    @Override
    public void clear() {
        events.clear();
    }

    @Override
    public Queue<IEvent> getAllReadyToSync() {
        return new ConcurrentLinkedQueue<>(events);
    }
}