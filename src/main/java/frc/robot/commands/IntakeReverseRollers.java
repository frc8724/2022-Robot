// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class IntakeReverseRollers extends CommandBase {
  /** Creates a new IntakeReverseRollers. */
  public IntakeReverseRollers() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.intakeRollers);
    addRequirements(RobotContainer.magazine);
    addRequirements(RobotContainer.loader);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.intakeRollers.spitOut();
    RobotContainer.magazine.setSpeed(-0.3);
    RobotContainer.loader.setSpeed(-0.3);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.intakeRollers.stop();
    RobotContainer.magazine.setSpeed(0.0);
    RobotContainer.loader.setSpeed(0.0);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
