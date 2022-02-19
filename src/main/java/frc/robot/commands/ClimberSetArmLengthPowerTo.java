// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class ClimberSetArmLengthPowerTo extends CommandBase {
  double m_power;

  /** Creates a new ClimberSetArmLengthPowerTo. */
  public ClimberSetArmLengthPowerTo(double d) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.climber);

    m_power = d;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.climber.setArmLengthPowerTo(m_power);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.climber.setArmLengthPowerTo(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
