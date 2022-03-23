// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class ClimberExtendCrawlDown extends CommandBase {
  /** Creates a new ClimberExtendCrawlDown. */
  public ClimberExtendCrawlDown() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.climber.setArmExtensionVelocity(-350);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return RobotContainer.climber.isAtBottom();
  }
}
