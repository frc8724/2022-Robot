// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SystemClimberAttachToNextRung extends SequentialCommandGroup {
  /** Creates a new SystemClimberAttachToNextRung. */
  public SystemClimberAttachToNextRung() {

    // traverse
    // arm extend unhook
    // arm down
    // arm extend
    // arm up
    // arm retract

    // // hooks to partially out either to unhook a rung or we are on the ground to
    // we
    // // can extend a little
    // addCommands(new ClimberWaitForArmLengthTo(Climber.ARMS_UNHOOK_POSITION));

    // // put arms down
    // addCommands(new ClimberSetArmPositionTo(Climber.ARMS_DOWN));
    // addCommands(new WaitCommand(0.5));

    // // extend arms out
    // addCommands(new ClimberWaitForArmLengthTo(Climber.ARMS_OUT_POSITION));

    // // put arms up
    // addCommands(new ClimberSetArmPositionTo(Climber.ARMS_UP));
    // addCommands(new WaitCommand(0.5));

    // // pull arms in. This will unhook the weak arms
    // addCommands(new ClimberSetArmLengthTo(Climber.ARMS_UNHOOK_POSITION));

    // // wait for weak arms to meet up with the strong arms
    // addCommands(new WaitCommand(1.0));

    // // pull up to hook the weak arms on the rung
    // addCommands(new ClimberSetArmLengthTo(Climber.ARMS_IN_POSITION));

  }

  @Override
  public void end(boolean interrupted) {
    RobotContainer.climber.stop();
  }
}
