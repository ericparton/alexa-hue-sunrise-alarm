package com.djungelorm.alexa.hue.sunrise.alarm.http.alexa;

public class AlexaDevice {
    private String accountName;
    private String serialNumber;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
