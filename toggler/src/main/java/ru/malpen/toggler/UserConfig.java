package ru.malpen.toggler;

import java.util.List;

class UserConfig {
    List<FeatureConfig> features;

    public UserConfig(List<FeatureConfig> features) {
        this.features = features;
    }

    public UserConfig() {
    }

    public List<FeatureConfig> getFeatures() {
        return features;
    }

    public UserConfig setFeatures(List<FeatureConfig> features) {
        this.features = features;
        return this;
    }

    @Override
    public String toString() {
        return "UserConfig{" +
                "features=" + features +
                '}';
    }
}
