// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import frc.robot.commands.*;
import frc.robot.commands.DriveStraightOnHeading.DistanceUnits;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FourBallPath extends ThreeBallPath {
    /** Creates a new FourBallPath. */
    public FourBallPath() {
        super(false);
        // angle toward ball against terminal
        addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 20.0,
                -20));
        addCommands(new DriveStraightOnHeading(0.6, DistanceUnits.INCHES, 100.0,
                0));
        addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 10.0,
                0));
        addCommands(new DriveStraightOnHeading(0.2, DistanceUnits.INCHES, 20.0,
                0.0));
        // curve in to get ball at terminal
        addCommands(new DriveStraightOnHeading(0.2, DistanceUnits.INCHES, 30.0,
                -60));

        addCommands(new DriveStraightOnHeading(-0.2, DistanceUnits.INCHES, 10.0,
                0.0));
        addCommands(new DriveStraightOnHeading(-0.4, DistanceUnits.INCHES, 10.0,
                0.0));
        addCommands(new DriveStraightOnHeading(-0.6, DistanceUnits.INCHES, 10.0,
                0.0));
        addCommands(new DriveStraightOnHeading(-0.8, DistanceUnits.INCHES, 140.0,
                0.0));
        addCommands(new DriveStraightOnHeading(-0.6, DistanceUnits.INCHES, 10.0,
                0.0));
        addCommands(new DriveStraightOnHeading(-0.4, DistanceUnits.INCHES, 10.0,
                0.0));
        addCommands(new DriveStraightOnHeading(-0.2, DistanceUnits.INCHES, 10.0,
                0.0));

        // addCommands(new ParallelRaceGroup(new SystemFireWhenReady(() -> 800.0), new
        // WaitCommand(2.0)));
    }
}
