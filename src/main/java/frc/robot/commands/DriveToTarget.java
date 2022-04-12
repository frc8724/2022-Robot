// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveBaseSubsystem;

public class DriveToTarget extends CommandBase {
  double m_targetPower;
  double m_desiredDisplacement;
  boolean m_targeted = false;

  final double onTargetToleranceDegrees = 1.0;

  /** Creates a new DriveToTarget. */
  public DriveToTarget(double arg_targetSpeed, double arg_distance) {
    // Use addRequirements() here to declare subsystem dependencies.

    addRequirements(RobotContainer.drive);
    addRequirements(RobotContainer.targeting);

    m_targetPower = arg_targetSpeed;

    arg_distance = arg_distance / DriveBaseSubsystem.DISTANCE_PER_PULSE_IN_INCHES;

    m_desiredDisplacement = arg_distance;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.drive.saveInitialWheelDistance();
    m_targeted = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // get the heading to the target and set the desired heading
    double targetHeading = RobotContainer.targeting.getHeadingToTarget();
    if (Double.isNaN(targetHeading)) {
      targetHeading = 0;
    }

    SmartDashboard.putNumber("DriveToTarget: targetHeading", targetHeading);

    // var robotHeading = RobotContainer.drive.getHeading();
    // var robotHeading = RobotContainer.drive.getHeadingForCapturedImage();
    double robotHeading = RobotContainer.drive.getHeading();

    SmartDashboard.putNumber("DriveToTarget: getHeadingForCapturedImage", robotHeading);

    RobotContainer.drive.setDesiredHeading(robotHeading + targetHeading);

    if (Math.abs(targetHeading) < onTargetToleranceDegrees) {
      m_targeted = true;
    }

    // drive to the target
    RobotContainer.drive.speedRacerDrive(m_targetPower, 0, true);
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

    return (m_targeted ||
        displacement >= m_desiredDisplacement);
  }
}
