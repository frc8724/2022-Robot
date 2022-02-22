// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class HoodMove extends InstantCommand {
  Supplier<Double> m_pos;

  public HoodMove(Supplier<Double> pos) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.hood);

    m_pos = pos;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.hood.setPosition(m_pos.get());
  }
}
