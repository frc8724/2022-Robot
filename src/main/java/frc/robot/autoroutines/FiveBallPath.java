// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.DriveStraightOnHeading;
import frc.robot.commands.DriveToTarget;
import frc.robot.commands.HoodMove;
import frc.robot.commands.IntakePistonsSet;
import frc.robot.commands.IntakeRollerStop;
import frc.robot.commands.IntakeRollerSuckIn;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.MagazinePullInBalls;
import frc.robot.commands.SystemFireWhenReady;
import frc.robot.commands.SystemShooterWarmUp;
import frc.robot.commands.SystemZero;
import frc.robot.commands.DriveStraightOnHeading.DistanceUnits;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.IntakePistons;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FiveBallPath extends SequentialCommandGroup {
    /** Creates a new FiveBallPath. */
    public FiveBallPath() {
        // Add your commands in the addCommands() call, e.g.
        // addCommands(new FooCommand(), new BarCommand());

        addCommands(new SystemZero());

        addCommands(new IntakePistonsSet(IntakePistons.INTAKE_DOWN));
        addCommands(new HoodMove(() -> {
            return Hood.AUTO_5_BALL_FIRST_SHOT;
        }));

        addCommands(new MagazinePullInBalls());

        // move away from the hub
        addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 2 * 12.0, 160.0));

        addCommands(new IntakeRollerSuckIn());
        addCommands(new SystemShooterWarmUp(800));

        // drive to get the 2nd ball
        addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 7 * 12.0, 160.0));
        addCommands(new DriveStraightOnHeading(0.3, DistanceUnits.INCHES, 12, 160.0));
        addCommands(new DriveStraightOnHeading(0.3, DistanceUnits.INCHES, 12, 120));

        addCommands(new IntakeRollerSuckIn());
        // shoot the balls
        addCommands(new ParallelRaceGroup(
                new SystemFireWhenReady(() -> 800.0), //
                new WaitCommand(2.0)));//

        // drive down range.
        addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 20, 190.0));
        addCommands(new DriveStraightOnHeading(0.8, DistanceUnits.INCHES, 130, 190.0));
        addCommands(new DriveStraightOnHeading(0.5, DistanceUnits.INCHES, 20, 190.0));

        addCommands(new IntakeRollerSuckIn());
        addCommands(new MagazinePullInBalls());

        // get ball 3 and 4.
        addCommands(new DriveStraightOnHeading(0.3, DistanceUnits.INCHES, 75,
                140.0));

        addCommands(new WaitCommand(0.2));

        // drive back to the hub. Accelerate with 3 commands
        addCommands(new DriveStraightOnHeading(-0.2, DistanceUnits.INCHES, 20,
                175.0));
        addCommands(new DriveStraightOnHeading(-0.4, DistanceUnits.INCHES, 20,
                175.0));
        addCommands(new DriveStraightOnHeading(-0.8, DistanceUnits.INCHES, 200,
                175.0));
        addCommands(new DriveStraightOnHeading(-0.5, DistanceUnits.INCHES, 20,
                175.0));

        addCommands(new SystemShooterWarmUp(800));

        // slow down
        addCommands(new DriveStraightOnHeading(-0.3, DistanceUnits.INCHES, 20,
                175.0));
        // turn towards hub
        addCommands(new DriveStraightOnHeading(-0.3, DistanceUnits.INCHES, 40,
                111.0));

        // shoot the balls
        addCommands(new ParallelRaceGroup(
                new SystemFireWhenReady(() -> 800.0), new WaitCommand(2.0)));

        addCommands(new IntakeRollerSuckIn());
        addCommands(new MagazinePullInBalls());

        // drive to ball 5
        addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 80,
                90.0));

        addCommands(new SystemShooterWarmUp(800));

        addCommands(new DriveStraightOnHeading(-0.4, DistanceUnits.INCHES, 100,
                90.0));

        // shoot the balls
        addCommands(new ParallelRaceGroup(
                new SystemFireWhenReady(() -> 800.0), new WaitCommand(2.0)));

    }
}
