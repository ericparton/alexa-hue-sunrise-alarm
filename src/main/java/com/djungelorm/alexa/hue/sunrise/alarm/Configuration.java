package com.djungelorm.alexa.hue.sunrise.alarm;

public class Configuration {
    public static Long getRefreshInterval() {
        return getEnvironmentVariableAsLong("REFRESH_INTERVAL");
    }

    public static Long getSequenceDuration() {
        return getEnvironmentVariableAsLong("PRE_ALARM_SEQUENCE_DURATION");
    }

    public static String getHueRoomName() {
        return getEnvironmentVariable("HUE_ROOM_NAME");
    }

    public static String getHueBridgeIpAddress() {
        return getEnvironmentVariable("HUE_BRIDGE_IP_ADDRESS");
    }

    public static String getHueApiKey() {
        return getEnvironmentVariable("HUE_API_KEY");
    }

    public static String getAlexaDeviceName() {
        return getEnvironmentVariable("ALEXA_DEVICE_NAME");
    }

    public static String getAlexaAuthenticationCookie() {
        return getEnvironmentVariable("ALEXA_AUTHENTICATION_COOKIE");
    }

    private static Long getEnvironmentVariableAsLong(String name) {
        String value = getEnvironmentVariable(name);

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Environment variable %s must be a number", name));
        }
    }

    private static String getEnvironmentVariable(String name) {
        String value = System.getenv(name);

        if (value == null) {
            throw new RuntimeException(String.format("Required environment variable %s is not set", name));
        }

        return value;
    }
}
