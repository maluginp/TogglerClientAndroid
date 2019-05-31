package ru.malpen.toggler.internal.web;

public class WebResponse<T> {
    private final int status;
    private final T data;

    public WebResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}
