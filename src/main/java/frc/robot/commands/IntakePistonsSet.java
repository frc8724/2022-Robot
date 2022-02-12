// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.fasterxml.jackson.databind.node.BooleanNode;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.IntakePistons;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class IntakePistonsSet extends InstantCommand {
  boolean m_position;

  public IntakePistonsSet(boolean pos) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.intakePistons);
    m_position = pos;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.intakePistons.set(m_position);
  }
}
