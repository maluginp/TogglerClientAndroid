package ru.malpen.toggler.internal.web;

class Response {
    private final int status;
    private final String data;
    private final Exception exception;

    public Response(int status, String data, Exception e) {
        this.status = status;
        this.data = data;
        this.exception = e;
    }

    public Response(Exception e) {
        this(0, "", e);
    }

    public Response(int status, String data) {
        this(status, data, null);
    }


    public boolean isFail() {
        return exception != null;
    }

    public int getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    public Exception getException() {
        return exception;
    }
}
