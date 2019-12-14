package ru.malpen.toggler;

import java.util.Map;

public class HttpPacket {
    private final String body;
    private final Map<String, String> headers;

    public HttpPacket(String body, Map<String, String> headers) {
        this.body = body;
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
