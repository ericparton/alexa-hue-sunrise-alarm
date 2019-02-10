package com.djungelorm.alexa.hue.sunrise.alarm.http.alexa;

import java.util.List;

public class AlexaNotifications implements AlexaEntityList<AlexaNotification> {
    private List<AlexaNotification> notifications;

    @Override
    public List<AlexaNotification> getEntities() {
        return notifications;
    }
}
