// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import org.mayheminc.util.MayhemOperatorPad;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class ClimberTeleopCommand extends CommandBase {
  MayhemOperatorPad m_pad;

  /** Creates a new ClimberTeleopCommand. */
  public ClimberTeleopCommand(MayhemOperatorPad pad) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.climber);
    m_pad = pad;
  }

  // Called when the command is initially scheduled.
  // @Override
  // public void initialize() {
  // }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double d = -m_pad.OPERATOR_PAD.getY();
    RobotContainer.climber.setArmLengthPowerTo(d);
  }

  // Called once the command ends or is interrupted.
  // @Override
  // public void end(boolean interrupted) {
  // }

  // // Returns true when the command should end.
  // @Override
  // public boolean isFinished() {
  // return false;
  // }
}
