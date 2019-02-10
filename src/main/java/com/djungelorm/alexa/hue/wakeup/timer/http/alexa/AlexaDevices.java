package com.djungelorm.alexa.hue.wakeup.timer.http.alexa;

import java.util.List;

public class AlexaDevices implements AlexaEntityList<AlexaDevice> {
    private List<AlexaDevice> devices;

    @Override
    public List<AlexaDevice> getEntities() {
        return devices;
    }
}
