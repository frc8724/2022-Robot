// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ShooterSetSpeed extends CommandBase {
  Supplier<Double> m_speed;

  public ShooterSetSpeed(Supplier<Double> d) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.shooter);

    m_speed = d;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.shooter.setShooterSpeed(m_speed.get());
  }

  @Override
  public boolean isFinished() {
    return RobotContainer.shooter.isShooterAtSpeed();
  }

  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      RobotContainer.shooter.setShooterSpeed(0.0);
    }
  }
}
