// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Climber;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SystemClimberInitialClimb extends SequentialCommandGroup {
  /** Creates a new SystemClimberInitialClimb. */
  public SystemClimberInitialClimb() {
    // initial climb
    // arm up.
    // arm extend
    // backup
    // arm retract

    addCommands(new ClimberSetArmPositionTo(Climber.ARMS_UP));
    addCommands(new WaitCommand(0.5));

    addCommands(new ClimberWaitForArmLengthTo(Climber.ARMS_OUT_POSITION));

    addCommands(
        new ParallelRaceGroup(new DriveStraight(-0.2),
            new WaitCommand(0.75)));

    addCommands(new ClimberSetArmLengthTo(Climber.ARMS_IN_POSITION));

  }
}
