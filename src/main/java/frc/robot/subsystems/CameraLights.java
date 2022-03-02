// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class CameraLights extends SubsystemBase {
  private final Solenoid lights = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.Solenoid.CAMERA_LIGHTS);

  private final boolean CAMERA_ON = false;
  private final boolean CAMERA_OFF = true;

  public CameraLights() {
    this.setDefaultCommand(new RunCommand(this::off, this));
  }

  public void on() {
    lights.set(CAMERA_ON);
  }

  public void off() {
    lights.set(CAMERA_OFF);
  }
}
