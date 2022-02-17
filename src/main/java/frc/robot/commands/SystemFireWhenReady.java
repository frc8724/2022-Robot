// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

/**
 * Wait for the shooter to be at speed and then load balls.
 */
public class SystemFireWhenReady extends CommandBase {
  /** Creates a new SystemFireWhenReady. */
  double m_shooterSpeed;

  public SystemFireWhenReady(double d) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.shooter);
    addRequirements(RobotContainer.loader);
    addRequirements(RobotContainer.magazine);

    m_shooterSpeed = d;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.shooter.setShooterSpeed(m_shooterSpeed);
    RobotContainer.accelerator.setAcceleratorSpeedVBus(0.5);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean atSpeed = RobotContainer.shooter.isShooterAtSpeed();
    if (atSpeed) {
      RobotContainer.loader.setSpeed(0.5);
      RobotContainer.magazine.setSpeed(0.5);
    } else {
      RobotContainer.loader.setSpeed(0.0);
      RobotContainer.magazine.setSpeed(0.0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.shooter.setShooterSpeed(0.0);
    RobotContainer.accelerator.setAcceleratorSpeedVBus(0.0);
    RobotContainer.loader.setSpeed(0.0);
    RobotContainer.magazine.setSpeed(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
