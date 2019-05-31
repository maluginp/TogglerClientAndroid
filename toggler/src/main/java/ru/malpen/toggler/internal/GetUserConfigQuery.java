package ru.malpen.toggler.internal;

public class GetUserConfigQuery {
    private String appKey;
    private TogglerUser user;

    public String getAppKey() {
        return appKey;
    }

    public GetUserConfigQuery setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public TogglerUser getUser() {
        return user;
    }

    public GetUserConfigQuery setUser(TogglerUser user) {
        this.user = user;
        return this;
    }
}
