// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class ClimberExtendAndWait extends CommandBase {
  double m_position;

  /** Creates a new ClimberSetArmLengthToAndWait. */
  public ClimberExtendAndWait(double pos) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.climber);

    m_position = pos;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.climber.setArmLengthTo(m_position);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return RobotContainer.climber.isAtPosition();
  }
}
