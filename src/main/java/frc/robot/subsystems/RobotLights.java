// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.mayheminc.util.LEDLights;
import org.mayheminc.util.LEDLights.PatternID;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class RobotLights extends SubsystemBase {
  LEDLights lights = new LEDLights();

  /** Creates a new RobotLights. */
  public RobotLights() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (isBrownOut()) {
      DriverStation.reportError("Brown out", false);
    } else if (isShooterAtSpeed()) {
      // DriverStation.reportError("Shooter Ready", false);

    } else if (isShooterWarmingUp()) {
      // DriverStation.reportError("Shooter Warming", false);

    } else if (isInAuto()) {
      // DriverStation.reportError("Auto Enabled", false);

    } else if (isInTeleOp()) {
      // DriverStation.reportError("TeleOp Enabled", false);

    } else {
      isDefault();
      // DriverStation.reportError("Default", false);
      // System.out.println("Lights: Default");
    }
  }

  private void isDefault() {
    lights.set(PatternID.TWINKLES_PARTY_PALETTE);
  }

  private boolean isShooterWarmingUp() {
    boolean b = RobotContainer.shooter.isShooterWarmingUp();
    if (b) {
      lights.set(PatternID.ORANGE);
    }
    return b;
  }

  private boolean isShooterAtSpeed() {
    boolean b = RobotContainer.shooter.isShooterAtSpeed();
    if (b) {
      lights.set(PatternID.GREEN);
    }
    return b;
  }

  private boolean isInTeleOp() {
    boolean b = DriverStation.isTeleopEnabled();
    if (b) {
      if (DriverStation.getAlliance() == Alliance.Blue) {
        lights.set(PatternID.BLUE);
      } else {
        lights.set(PatternID.RED);
      }
    }
    return b;
  }

  private boolean isInAuto() {
    boolean b = DriverStation.isAutonomousEnabled();
    if (b) {
      lights.set(PatternID.YELLOW);
    }
    return b;
  }

  private boolean isBrownOut() {
    boolean b = RobotController.getBatteryVoltage() < 7.0;

    if (b) {
      lights.set(PatternID.BLACK);
    }
    return b;
  }
}
