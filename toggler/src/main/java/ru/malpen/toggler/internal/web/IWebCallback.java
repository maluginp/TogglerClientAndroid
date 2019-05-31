package ru.malpen.toggler.internal.web;

public interface IWebCallback<T> {
    void success(T o);
    void failure(Throwable t);
}