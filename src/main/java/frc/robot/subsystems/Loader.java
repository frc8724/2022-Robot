// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Loader extends SubsystemBase {

  private final VictorSPX motorControl = new WPI_VictorSPX(Constants.Talon.LOADER_ROLLER);

  /** Creates a new Loader. */
  public Loader() {
    motorControl.setNeutralMode(NeutralMode.Coast);
    motorControl.configNominalOutputForward(+0.0f);
    motorControl.configNominalOutputReverse(0.0);
    motorControl.configPeakOutputForward(+12.0);
    motorControl.configPeakOutputReverse(-12.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setSpeed(double d) {
    motorControl.set(VictorSPXControlMode.PercentOutput, d);
  }
}
