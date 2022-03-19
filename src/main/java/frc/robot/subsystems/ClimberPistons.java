// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberPistons extends SubsystemBase {
  private final Solenoid strongArmPiston = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.Solenoid.CLIMBER);
  public static final boolean ARMS_UP = true;
  public static final boolean ARMS_DOWN = false;

  /** Creates a new ClimberPistons. */
  public ClimberPistons() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setArmPositionTo(boolean b) {
    strongArmPiston.set(b);
  }
}
