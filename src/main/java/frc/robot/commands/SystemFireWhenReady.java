// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

/**
 * Wait for the shooter to be at speed and then load balls.
 */
public class SystemFireWhenReady extends CommandBase {
  /** Creates a new SystemFireWhenReady. */
  Supplier<Double> m_shooterSpeed;
  boolean m_quickFire;

  public SystemFireWhenReady(Supplier<Double> shooterSpeed) {
    this(shooterSpeed, false);
  }

  public SystemFireWhenReady(Supplier<Double> shooterSpeed, boolean quickFire) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.shooter);
    addRequirements(RobotContainer.loader);
    addRequirements(RobotContainer.magazine);
    addRequirements(RobotContainer.accelerator);

    m_shooterSpeed = shooterSpeed;
    m_quickFire = quickFire;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.shooter.setShooterSpeed(m_shooterSpeed.get());
    RobotContainer.accelerator.setAcceleratorSpeedVBus(0.4);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean atSpeed = RobotContainer.shooter.isShooterAtSpeed();
    if (atSpeed || m_quickFire) {
      RobotContainer.loader.setSpeed(0.75);
      RobotContainer.magazine.setSpeed(0.5);
    } else {
      RobotContainer.loader.setSpeed(0.0);
      RobotContainer.magazine.setSpeed(0.2);
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
