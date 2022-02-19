// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;

public class SystemWarmUpShooter extends InstantCommand {
  /** Creates a new SystemWarmUpShooter. */
  public SystemWarmUpShooter() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.shooter);
    addRequirements(RobotContainer.accelerator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.accelerator.setAcceleratorSpeedVBus(0.3);
    RobotContainer.shooter.setShooterSpeed(400);
  }

}
