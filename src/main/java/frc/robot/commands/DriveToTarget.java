// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class DriveToTarget extends CommandBase {
  double m_targetPower;
  double m_desiredDisplacement;

  /** Creates a new DriveToTarget. */
  public DriveToTarget(double arg_targetSpeed, double arg_distance) {
    // Use addRequirements() here to declare subsystem dependencies.
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
    var heading = RobotContainer.targeting.getHeadingToTarget();
    RobotContainer.drive.setDesiredHeading(heading);

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
