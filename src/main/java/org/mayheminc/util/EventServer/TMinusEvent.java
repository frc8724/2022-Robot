package org.mayheminc.util.EventServer;

import edu.wpi.first.wpilibj.DriverStation;

public class TMinusEvent extends OneTimeEvent {
    String name;
    double time;
    final double MATCH_TIME_SEC = 150.0;

    public TMinusEvent(String S, int T) {
        name = S;
        time = T;
    }

    @Override
    public String OneTimeExecute() {

        // if the match time left is less than the time, return the name.
        if (DriverStation.getMatchTime() < time) {
            return name;
        }
        return "";
    }

}