package com.djungelorm.alexa.hue.sunrise.alarm.hue;

import com.djungelorm.alexa.hue.sunrise.alarm.Configuration;
import com.djungelorm.alexa.hue.sunrise.alarm.http.alexa.AlexaNotification;
import com.github.zeroone3010.yahueapi.Hue;
import com.github.zeroone3010.yahueapi.Light;
import com.github.zeroone3010.yahueapi.Room;
import com.github.zeroone3010.yahueapi.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ArtificialSunriseSequence implements Runnable {
    private static final Logger log = LogManager.getLogger(ArtificialSunriseSequence.class);

    private final AlexaNotification alarm;
    private final Hue hueHttpClient;

    private LocalDateTime sequenceStartTime;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture scheduledFuture;
    private List<State> sequenceSteps;
    private Integer stepDuration;
    private Map<String, State> initialLightStates;

    public ArtificialSunriseSequence(final AlexaNotification alarm, final Hue hueHttpClient) {
        this.alarm = alarm;
        this.hueHttpClient = hueHttpClient;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        log.info("Starting artificial sunrise sequence");

        sequenceStartTime = LocalDateTime.now();
        sequenceSteps = new ArrayList<>();

        sequenceSteps.add(StateFactory.buildState(true, 0.65f, 0.41f, 85));
        sequenceSteps.add(StateFactory.buildState(true, 0.60f, 0.40f, 170));
        sequenceSteps.add(StateFactory.buildState(true, 0.48f, 0.40f, 255));

        var sequenceDuration = Duration.between(sequenceStartTime, alarm.getAlarmTime()).toMillis();

        stepDuration = Math.round((float) sequenceDuration / sequenceSteps.size());

        log.info("Scheduling {} sequence steps {} seconds apart", sequenceSteps.size(), stepDuration / 1000);

        //The API requires transition time to be in centiseconds (???)
        sequenceSteps.forEach(state -> state.setTransitiontime(Math.round(stepDuration / 100f)));

        var hueRoom = getHueRoom();

        initialLightStates = hueRoom.getLights().stream().collect(Collectors.toMap(Light::getName, Light::getState));

        //Initial sunrise state
        hueRoom.setState(StateFactory.buildState(true, 0.67f, 0.39f, 1));

        scheduledFuture = scheduler.schedule(this, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        log.info("Executing step of artificial sunrise sequence");

        try {
            var hueRoom = getHueRoom();

            if (!sequenceSteps.isEmpty()) {
                hueRoom.setState(sequenceSteps.remove(0));
                log.info("Completed step of artificial sunrise sequence. {} step(s) remain", sequenceSteps.size());
                scheduledFuture = scheduler.schedule(this, stepDuration, TimeUnit.MILLISECONDS);
            } else {
                log.info("Artificial sunrise sequence completed. Restoring original light states");
                initialLightStates.forEach((key, value) -> hueRoom.getLightByName(key).ifPresent(light -> {
                    value.setOn(true);
                    light.setState(value);
                }));
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void stop() {
        sequenceStartTime = null;
        scheduledFuture.cancel(false);
    }

    public AlexaNotification getAlarm() {
        return alarm;
    }

    private Room getHueRoom() {
        var roomName = Configuration.getHueRoomName();
        var hueRoom = hueHttpClient.getRoomByName(roomName);

        if (!hueRoom.isPresent()) {
            throw new RuntimeException(String.format("No Hue room named '%s' was found", roomName));
        }

        return hueRoom.get();
    }
}
