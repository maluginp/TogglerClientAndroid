package ru.malpen.toggler.internal;

public class FeatureConfig {
    private String name;
    private String type;
    private String value;

    public String getName() {
        return name;
    }

    public FeatureConfig setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public FeatureConfig setType(String type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public FeatureConfig setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "FeatureConfig{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
