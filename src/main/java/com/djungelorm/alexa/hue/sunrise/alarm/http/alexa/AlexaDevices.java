package com.djungelorm.alexa.hue.sunrise.alarm.http.alexa;

import java.util.List;

public class AlexaDevices implements AlexaEntityList<AlexaDevice> {
    private List<AlexaDevice> devices;

    @Override
    public List<AlexaDevice> getEntities() {
        return devices;
    }
}
