package ru.malpen.toggler.internal.web;

public interface IRunnable<T> {
    void execute(T completion);
}