package com.djungelorm.alexa.hue.wakeup.timer.hue;

import com.djungelorm.alexa.hue.wakeup.timer.Configuration;
import com.djungelorm.alexa.hue.wakeup.timer.http.alexa.AlexaNotification;
import com.github.zeroone3010.yahueapi.Hue;
import com.github.zeroone3010.yahueapi.Room;
import com.github.zeroone3010.yahueapi.State;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ArtificialSunriseSequence implements Runnable {
    private final AlexaNotification alarm;
    private final Hue hueHttpClient;

    private LocalDateTime sequenceStartTime;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture scheduledFuture;
    private List<State> sequenceSteps;
    private Integer stepDuration;

    public ArtificialSunriseSequence(final AlexaNotification alarm, final Hue hueHttpClient) {
        this.alarm = alarm;
        this.hueHttpClient = hueHttpClient;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        System.out.println("Starting artificial sunrise sequence");

        sequenceStartTime = LocalDateTime.now();
        sequenceSteps = new ArrayList<>();

        sequenceSteps.add(StateFactory.buildState(true, 0.65f, 0.41f, 85));
        sequenceSteps.add(StateFactory.buildState(true, 0.60f, 0.40f, 170));
        sequenceSteps.add(StateFactory.buildState(true, 0.48f, 0.40f, 255));

        var sequenceDuration = Duration.between(sequenceStartTime, alarm.getAlarmTime()).toMillis();

        stepDuration = Math.round((float) sequenceDuration / sequenceSteps.size());

        System.out.println(String.format("Scheduling %d sequence steps %dms apart", sequenceSteps.size(), stepDuration));

        //The API requires transition time to be in centiseconds (???)
        sequenceSteps.forEach(state -> state.setTransitiontime(Math.round(stepDuration / 100f)));

        getHueRoom().setState(StateFactory.buildState(true, 0.67f, 0.39f, 1));
        scheduledFuture = scheduler.schedule(this, stepDuration, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        System.out.println("Executing next step of artificial sunrise sequence");

        getHueRoom().setState(sequenceSteps.remove(0));

        if (!sequenceSteps.isEmpty()) {
            scheduledFuture = scheduler.schedule(this, stepDuration, TimeUnit.MILLISECONDS);
        } else {
            System.out.println("Artificial sunrise sequence completed");
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
