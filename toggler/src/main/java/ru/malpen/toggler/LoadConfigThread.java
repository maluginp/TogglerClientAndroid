package ru.malpen.toggler;

import java.io.IOException;

public class LoadConfigThread extends Thread {

    private static final int DELAY_SUCCESS = 5 * 1000; //15 * 60 * 1000;
    private static final int DELAY_ERROR = 1 * 1000; // 30 * 1000;


    private TogglerUser mUser;
    private final DeviceInfo mDeviceInfo;
    private final TogglerWebClient webClient;
    private final OnLoadConfig onLoadConfig;
    private String mAppKey;

    public LoadConfigThread(
            TogglerWebClient webClient,
            OnLoadConfig onLoadConfig, String appKey, TogglerUser user, DeviceInfo mDeviceInfo) {
        this.webClient = webClient;
        this.onLoadConfig = onLoadConfig;
        this.mAppKey = appKey;
        this.mUser = user;
        this.mDeviceInfo = mDeviceInfo;
    }

    public void setUser(TogglerUser user) {
        this.mUser = user;
    }

    @Override
    public void run() {
        while (true) {
            try {
                UserConfig config = webClient.getUserConfig(mAppKey, mUser, mDeviceInfo);

                if (config != null) {
                    if (this.onLoadConfig != null) {
                        this.onLoadConfig.onConfigUpdated(config);
                    }

                    delay(DELAY_SUCCESS);
                } else {
                    delay(DELAY_ERROR);
                }
            } catch (IOException exception) {
                delay(DELAY_ERROR);
            }
        }

    }

    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignore) {
            //
        }
    }

    interface OnLoadConfig {
        void onConfigUpdated(UserConfig config);
    }
}
