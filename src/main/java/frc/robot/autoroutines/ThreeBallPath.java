// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.DriveStraightOnHeading;
import frc.robot.commands.SystemFireWhenReady;
import frc.robot.commands.DriveStraightOnHeading.DistanceUnits;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ThreeBallPath extends TwoBallPath {
  /** Creates a new ThreeBallPath. */
  public ThreeBallPath(boolean shootball) {
    super(false);
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    // turn to line up to the ball.
    addCommands(new DriveStraightOnHeading(0.2, DistanceUnits.INCHES, 20.0, +120.0));
    addCommands(new DriveStraightOnHeading(0.3, DistanceUnits.INCHES, 90.0, +120.0));

    // run down field to gather ball and get 1/2 way to next one.
    // addCommands(new DriveStraightOnHeading(0.3, DistanceUnits.INCHES, 80.0,
    // -40));

    // fire
    if (shootball) {
      addCommands(new DriveStraightOnHeading(-0.2, DistanceUnits.INCHES, 40.0, +60.0));
      addCommands(new ParallelRaceGroup(new SystemFireWhenReady(() -> 800.0), new WaitCommand(3.0)));
    }
  }
}
