package org.mayheminc.util;
import edu.wpi.first.wpilibj.AnalogInput;

/*
 * The Ultrasonic sensor being used is an MB1340. 
 * The conversion factor being used is (5 volts) / (1024 per cm).
 */

public class MB1340Ultrasonic {
	AnalogInput analogUltrasonic;
	final static double voltage = 5.0;
	final static double maxADCBins = 1024;
	
	public MB1340Ultrasonic(int analogPort) {
		analogUltrasonic = new AnalogInput(analogPort);
	}

	/**
	 * Get the Distance in cm.
	 * Full scale is 1024 cm.  The minimum is 25cm.
	 * @return
	 */
	public double getDistance() {
		return (analogUltrasonic.getAverageVoltage() / voltage) * maxADCBins;
	}
	
	public double getDistanceInInches() {
		return getDistance() / 2.54; 
	}
	
}
