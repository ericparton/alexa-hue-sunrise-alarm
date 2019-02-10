package com.djungelorm.alexa.hue.wakeup.timer.http.alexa;

import java.util.List;

public class AlexaNotifications implements AlexaEntityList<AlexaNotification> {
    private List<AlexaNotification> notifications;

    @Override
    public List<AlexaNotification> getEntities() {
        return notifications;
    }
}
