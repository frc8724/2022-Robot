// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class MagazineSetSpeed extends CommandBase {
  double m_Speed;
  boolean m_instant;

  public MagazineSetSpeed(double d) {
    this(d, true);
  }

  public MagazineSetSpeed(double d, boolean instant) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.magazine);

    m_Speed = d;
    m_instant = instant;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.magazine.setSpeed(m_Speed);
  }

  @Override
  public boolean isFinished() {
    return m_instant;
  }

  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      RobotContainer.magazine.setSpeed(0.0);
    }
  }
}
