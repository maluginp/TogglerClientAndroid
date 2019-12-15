package ru.malpen.toggler;

import java.util.Map;

public class HttpPacket {
    private String body;
    private Map<String, String> headers;

    public HttpPacket() {
    }

    public HttpPacket(String body, Map<String, String> headers) {
        this.body = body;
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public HttpPacket setBody(String body) {
        this.body = body;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpPacket setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
}
