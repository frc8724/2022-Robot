package org.mayheminc.util;

import edu.wpi.first.wpilibj.AnalogInput;

// http://media.digikey.com/pdf/Data%20Sheets/Sharp%20PDFs/GP2D120.pdf

public class RangeFinder_GP2D120 {
	
	private AnalogInput m_channel;
	private static final int HISTORY_BUFFER_SIZE = 5;
	private double m_historyBuffer[]=new double[HISTORY_BUFFER_SIZE];
	private int m_historyBufferIndex;
	private double m_filteredVoltage;
	
	public RangeFinder_GP2D120(int channel)	{
		m_channel = new AnalogInput(channel);
		m_filteredVoltage = 0.0;
	}
	
	public void periodic() {
		double min = m_historyBuffer[0];
		m_historyBuffer[m_historyBufferIndex]= m_channel.getVoltage(); //return channel.getVoltage();
		m_historyBufferIndex++;
		
		if (m_historyBufferIndex>=HISTORY_BUFFER_SIZE) {
			m_historyBufferIndex=0;
		}
		
		for (int i=1; i<HISTORY_BUFFER_SIZE; i++) {
			if(m_historyBuffer[i] < min) {
				min = m_historyBuffer[i];
			}
		}
		m_filteredVoltage = min;
	}
	
	public double getFilteredVoltage() {
		return m_filteredVoltage;
	}
	
	//	can we see an object at all? 
	private static final double OBJECT_IS_SEEN_VOLTAGE = 0.50;
//	0.5V is roughly equal to 28cm
	public boolean isObjectSeen() {
		return getFilteredVoltage() >= OBJECT_IS_SEEN_VOLTAGE;
	}

	//	had been 0.80 prior to 31 Jan
	private static final double OBJECT_IS_CLOSE_VOLTAGE = 0.80;
	public boolean isObjectClose() {
		return getFilteredVoltage() >= OBJECT_IS_CLOSE_VOLTAGE;
	}

	//	added on 11 Feb 2015 to allow "floating off tote"
	private static final double OBJECT_IS_VERY_CLOSE_VOLTAGE = 1.60;
	public boolean isObjectVeryClose() {
		return getFilteredVoltage() >= OBJECT_IS_VERY_CLOSE_VOLTAGE;
	}
	
}