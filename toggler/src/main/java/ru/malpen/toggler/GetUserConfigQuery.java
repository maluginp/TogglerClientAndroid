package ru.malpen.toggler;

class GetUserConfigQuery {
    private String appKey;
    private TogglerUser user;
    private DeviceInfo deviceInfo;

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

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public GetUserConfigQuery setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }
}
