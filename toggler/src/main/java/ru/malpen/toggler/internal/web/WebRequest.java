package ru.malpen.toggler.internal.web;

public class WebRequest<T> implements ICancelable {
    private final IWebClientRequest<T> request;

    public WebRequest(IWebClientRequest<T> request) {
        this.request = request;
    }

    public ICancelable enqueue(IWebCallback<WebResponse<T>> onCompletion) {
        request.execute(onCompletion);
        return request;
    }

    @Override
    public void cancel() {
        request.cancel();
    }
}