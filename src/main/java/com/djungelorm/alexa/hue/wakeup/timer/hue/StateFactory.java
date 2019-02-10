package com.djungelorm.alexa.hue.wakeup.timer.hue;

import com.github.zeroone3010.yahueapi.State;

import java.util.List;

public class StateFactory {
    public static State buildState(final boolean on, final Float x, final Float y, final Integer brightness) {
        var state = new State();
        state.setXy(List.of(x, y));
        state.setBri(brightness);
        state.setOn(on);
        return state;
    }
}
