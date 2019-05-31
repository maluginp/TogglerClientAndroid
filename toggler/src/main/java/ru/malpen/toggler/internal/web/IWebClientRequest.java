package ru.malpen.toggler.internal.web;

public interface IWebClientRequest<T> extends ICancelable, IRunnable<IWebCallback<WebResponse<T>>> {

}