// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveBaseSubsystem;

public class DriveToTarget extends CommandBase {
  double m_targetPower;
  double m_desiredDisplacement;

  /** Creates a new DriveToTarget. */
  public DriveToTarget(double arg_targetSpeed, double arg_distance) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_targetPower = arg_targetSpeed;

    arg_distance = arg_distance / DriveBaseSubsystem.DISTANCE_PER_PULSE_IN_INCHES;

    m_desiredDisplacement = arg_distance;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.drive.saveInitialWheelDistance();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // get the heading to the target and set the desired heading
    var targetHeading = RobotContainer.targeting.getHeadingToTarget();
    var robotHeading = RobotContainer.drive.getHeading();
    RobotContainer.drive.setDesiredHeading(robotHeading + targetHeading);

    // drive to the target
    RobotContainer.drive.speedRacerDrive(m_targetPower, 0, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.drive.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    int displacement = (int) RobotContainer.drive.getWheelDistance();

    displacement = Math.abs(displacement);

    return (displacement >= m_desiredDisplacement);
  }
}
