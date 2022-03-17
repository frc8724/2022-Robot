// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;

public class ClimberSetVelocity extends CommandBase {
  double m_speed;

  /** Creates a new ClimberSetVelocity. */
  public ClimberSetVelocity(double speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.climber);

    m_speed = speed;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.climber.setArmExtensionVelocity(m_speed);
  }

  @Override
  public void end(boolean interrupted) {
    RobotContainer.climber.setArmExtensionVelocity(0);
  }
}
