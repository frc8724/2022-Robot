// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ClimberSetArmPositionTo extends InstantCommand {
  boolean m_pos;

  public ClimberSetArmPositionTo(boolean b) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.climber);
    addRequirements(RobotContainer.intakePistons);
    m_pos = b;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.climberPistons.setArmPositionTo(m_pos);
    RobotContainer.intakePistons.Up();
    RobotContainer.climber.stop();
  }
}
