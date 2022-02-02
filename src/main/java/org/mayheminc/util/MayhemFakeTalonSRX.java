/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.util;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Replace a MayhemTalonSRX if you don't have hardware yet.
 */
public class MayhemFakeTalonSRX {
    public MayhemFakeTalonSRX(int id) {

    }

    public void setNeutralMode(NeutralMode coast) {
    }

    public void configNominalOutputVoltage(float f, float g) {
    }

    public void configPeakOutputVoltage(double d, double e) {
    }

    public void set(ControlMode percentoutput, double speed) {
    }

    public double get() {
        return 0;
    }

    public void changeControlMode(ControlMode percentoutput) {
    }

    public void config_kP(int i, double d, int j) {
    }

    public void config_kI(int i, double d, int j) {
    }

    public void config_kF(int i, double d, int j) {
    }

    public void config_kD(int i, double d, int j) {
    }

    public void configSelectedFeedbackSensor(FeedbackDevice quadencoder) {
    }

    public void setPosition(int pos) {
    }
}
