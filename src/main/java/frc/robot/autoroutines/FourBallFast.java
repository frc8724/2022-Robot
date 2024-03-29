// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.RobotContainer;
import frc.robot.commands.*;
import frc.robot.commands.DriveStraightOnHeading.DistanceUnits;
import frc.robot.subsystems.IntakePistons;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FourBallFast extends SequentialCommandGroup {
    /** Creates a new FourBallFast. */
    public FourBallFast() {
        // Add your commands in the addCommands() call, e.g.
        // addCommands(new FooCommand(), new BarCommand());

        addCommands(new SystemZero());

        // addCommands(new DriveStraightOnHeading(-0.2, -0.5, 200.0, 111.0));
        // addCommands(new WaitCommand(4.0));

        addCommands(new IntakePistonsSet(IntakePistons.INTAKE_DOWN));

        addCommands(new HoodMove(() -> {
            return RobotContainer.hood.getHoodClosePosition();
        }));

        addCommands(new ShooterSetAccelerator(0.3)); // start slow

        // drive to get the 2nd ball
        // addCommands(new DriveStraightOnHeading(0.3, 20.0, 90.0));
        // addCommands(new DriveStraightOnHeading(0.5, 60.0, 90.0));
        addCommands(new DriveStraightOnHeading(0.2, 0.5, 80.0, 90.0));
        addCommands(
                new ParallelCommandGroup(//
                        new IntakeRollerSuckIn(), //
                        new MagazinePullInBalls(), //
                        new ShooterSetAccelerator(0.5), //
                        new DriveStraightOnHeading(0.5, 20.0, 90.0)//
                ));
        addCommands(new DriveStraightOnHeading(0.5, 0.2, 15.0, 90.0));
        addCommands(new InstantCommand(() -> RobotContainer.cameraLights.on()));

        // drive back to hub
        addCommands(new DriveStraightOnHeading(-0.2, -0.5, 20.0, 70.0));
        addCommands(new DriveStraightOnHeading(-0.5, 45.0, 70.0));
        addCommands(new DriveStraightOnHeading(-0.5, -0.1, 25.0, 130.0));

        // fire
        addCommands(new ParallelRaceGroup(
                new SystemFireWhenReady(() -> Shooter.getShortShot()), //
                new WaitForNumBalls(2), //
                new WaitCommand(2.0)));

        // drive to ball #3
        addCommands(new DriveStraightOnHeading(0.2, 0.5, 25.0, 170));
        addCommands(new DriveStraightOnHeading(0.5, 55.0, 170));
        addCommands(
                new ParallelCommandGroup(//
                        new IntakeRollerSuckIn(), //
                        new MagazinePullInBalls(), //
                        new DriveStraightOnHeading(0.5, 25.0, 170)));

        addCommands(new DriveStraightOnHeading(0.5, 0.3, 25.0, 170));

        addCommands(new WaitCommand(0.25));

        addCommands(new DriveStraightOnHeading(-0.3, -0.5, 25.0, 170));
        addCommands(new DriveStraightOnHeading(-0.5, 65.0, 170));
        addCommands(new DriveStraightOnHeading(-0.5, -0.1, 25.0, 120));

        // fire
        addCommands(new ParallelRaceGroup(
                new SystemFireWhenReady(() -> Shooter.getShortShot()), //
                new WaitForNumBalls(1), //
                new WaitCommand(1.0)));

        // addCommands(new DriveStraightOnHeading(0.3, 25.0, 180));
        // addCommands(new DriveStraightOnHeading(0.5, 40.0, 180));
        addCommands(new DriveStraightOnHeading(0.2, 0.8, 65.0, 180));

        addCommands(new DriveStraightOnHeading(0.8, 145.0, 180));
        addCommands(new DriveStraightOnHeading(0.8, 0.3, 25.0, 180));
        addCommands(new DriveStraightOnHeading(0.3, 60.0, 135));

    }
}
