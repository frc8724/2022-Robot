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
  public ThreeBallPath() {
    super();
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    // turn to line up to the ball.
    addCommands(new DriveStraightOnHeading(0.2, DistanceUnits.INCHES, 70.0, +90));

    // run down field to gather ball and get 1/2 way to next one.
    addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 150.0, -30));

    // angle toward ball against terminal
    addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 90.0, -10));
    addCommands(new DriveStraightOnHeading(0.2, DistanceUnits.INCHES, 10.0, 0.0));
    // curve in to get ball at terminal
    addCommands(new DriveStraightOnHeading(0.2, DistanceUnits.INCHES, 20.0, -60));

    addCommands(new DriveStraightOnHeading(-0.2, DistanceUnits.INCHES, 10.0, 0.0));
    addCommands(new DriveStraightOnHeading(-0.4, DistanceUnits.INCHES, 10.0, 0.0));
    addCommands(new DriveStraightOnHeading(-0.6, DistanceUnits.INCHES, 10.0, 0.0));
    addCommands(new DriveStraightOnHeading(-0.8, DistanceUnits.INCHES, 140.0, 0.0));
    addCommands(new DriveStraightOnHeading(-0.6, DistanceUnits.INCHES, 10.0, 0.0));
    addCommands(new DriveStraightOnHeading(-0.4, DistanceUnits.INCHES, 10.0, 0.0));
    addCommands(new DriveStraightOnHeading(-0.2, DistanceUnits.INCHES, 10.0, 0.0));

    addCommands(new ParallelRaceGroup(new SystemFireWhenReady(() -> 800.0), new WaitCommand(2.0)));
  }
}
