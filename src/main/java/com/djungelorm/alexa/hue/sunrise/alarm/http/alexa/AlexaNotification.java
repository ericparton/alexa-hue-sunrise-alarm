package com.djungelorm.alexa.hue.sunrise.alarm.http.alexa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AlexaNotification {
    private String status;
    private String type;
    private String originalDate;
    private String originalTime;
    private String deviceSerialNumber;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getOriginalTime() {
        return originalTime;
    }

    public void setOriginalTime(String originalTime) {
        this.originalTime = originalTime;
    }

    public String getOriginalDate() {
        return originalDate;
    }

    public void setOriginalDate(String originalDate) {
        this.originalDate = originalDate;
    }

    public LocalDateTime getAlarmTime() {
        String dateTimeString = String.format("%s %s", getOriginalDate(), getOriginalTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}
