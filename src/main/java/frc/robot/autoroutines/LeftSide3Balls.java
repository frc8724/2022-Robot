// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.DriveStraightOnHeading;
import frc.robot.commands.SystemFireWhenReady;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class LeftSide3Balls extends DriveForwardShoot2 {
  /** Creates a new LeftSide3Balls. */
  public LeftSide3Balls() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    super(false);

    addCommands(new DriveStraightOnHeading(0.2, 20.0, 45.0));
    addCommands(new DriveStraightOnHeading(0.3, 20.0, 45.0));
    addCommands(new DriveStraightOnHeading(0.4, 110.0, 45.0));
    addCommands( //
        new ParallelCommandGroup( //
            new DriveStraightOnHeading(0.4, 200.0, 33.0), //
            new ParallelRaceGroup(//
                new SystemFireWhenReady(() -> 500.0), //
                new WaitCommand(3.0)//
            )));

    addCommands(new DriveStraightOnHeading(-0.2, 20.0, 37.0));
    addCommands(new DriveStraightOnHeading(-0.3, 20.0, 37.0));
    addCommands(new DriveStraightOnHeading(-0.5, 200.0, 37.0));

    addCommands(new DriveStraightOnHeading(-0.2, 30.0, 111.0));
  }
}
