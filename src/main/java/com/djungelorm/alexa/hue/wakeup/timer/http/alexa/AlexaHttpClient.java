package com.djungelorm.alexa.hue.wakeup.timer.http.alexa;

import com.djungelorm.alexa.hue.wakeup.timer.Configuration;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

//TODO: automate login
public class AlexaHttpClient {
    private static final String ALEXA_DEVICES_ENDPOINT = "https://alexa.amazon.com/api/devices-v2/device";
    private static final String ALEXA_NOTIFICATIONS_ENDPOINT = "https://alexa.amazon.com/api/notifications";

    private String authenticationCookie;

    public AlexaHttpClient(String authenticationCookie) {
        this.authenticationCookie = authenticationCookie;

        if (this.authenticationCookie == null || this.authenticationCookie.isBlank()) {
            throw new IllegalArgumentException("authenticationCookie is required");
        }
    }

    public List<AlexaNotification> getNotifications() {
        return makeGetRequest(ALEXA_NOTIFICATIONS_ENDPOINT, AlexaNotifications.class).getEntities();
    }

    public List<AlexaDevice> getDevices() {
        return makeGetRequest(ALEXA_DEVICES_ENDPOINT, AlexaDevices.class).getEntities();
    }

    private <T extends AlexaEntityList> T makeGetRequest(String endpoint, Class<T> clazz) {
        try {
            var httpRequest = getHttpRequest(endpoint, authenticationCookie);
            var httpResponse = getHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                return new Gson().fromJson(httpResponse.body(), clazz);
            } else {
                throw new RuntimeException(String.format("Received non 200 response from %s: %d", endpoint, httpResponse.statusCode()));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpClient getHttpClient() {
        return HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.of(Math.round(Configuration.getRefreshInterval() / 2f), SECONDS))
                .build();
    }

    private static HttpRequest getHttpRequest(String endpoint, String authenticationCookie) {
        return HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Cookie", authenticationCookie)
                .build();
    }
}
