package org.mayheminc.util;

import edu.wpi.first.wpilibj.DriverStation;

public class History {
    private static final int HISTORY_SIZE = 50;

    private double time[] = new double[HISTORY_SIZE];
    private double azimuth[] = new double[HISTORY_SIZE];
    private int index = 0;

    public History() {
        // ensure there is at least one element in the history
        add(-1.0, 0.0); // make a fictitious element at t=-1 seconds, with heading of 0.0 degrees - NOPMD
    }

    public void add(double t, double az) {

        time[index] = t;
        azimuth[index] = az;

        index++;
        if (index >= HISTORY_SIZE) {
            index = 0;
        }
    }

    public double getAzForTime(double t) {
        double az = azimuth[index];
        int i = index - 1;
        int count = 0;

        // if (t < 0) {
        // DriverStation.reportError("Negative time in history", false);
        // return 0.0; // no negative times.
        // }

        while (i != index) {
            if (i < 0) {
                i = HISTORY_SIZE - 1;
            }

            if (time[i] <= t) {
                az = azimuth[i];
                break;
            }

            i--;
            count++;
            if (count > HISTORY_SIZE) {
                DriverStation.reportError("Looking too far back", false);
                az = azimuth[index];
                break;
            }
        }

        return az;
    }
}
