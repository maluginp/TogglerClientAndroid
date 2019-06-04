package ru.malpen.toggler;

class DeviceInfo {
    private String version;
    private String model;

    public String getPlatform() {
        return "android";
    }

    public String getVersion() {
        return version;
    }

    public DeviceInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getModel() {
        return model;
    }

    public DeviceInfo setModel(String model) {
        this.model = model;
        return this;
    }
}
