// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.DriveStraight;
import frc.robot.commands.DriveStraightOnHeading;
import frc.robot.commands.DriveToTarget;
import frc.robot.commands.HoodMove;
import frc.robot.commands.IntakePistonsSet;
import frc.robot.commands.IntakeRollerSuckIn;
import frc.robot.commands.MagazinePullInBalls;
import frc.robot.commands.SystemFireWhenReady;
import frc.robot.commands.SystemShooterWarmUp;
import frc.robot.commands.SystemZero;
import frc.robot.commands.WaitForNumBalls;
import frc.robot.commands.DriveStraightOnHeading.DistanceUnits;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.IntakePistons;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FiveBallPath extends SequentialCommandGroup {
        public static class ShortTargetAndShootBall extends SequentialCommandGroup {
                public ShortTargetAndShootBall() {
                        addCommands(new ParallelCommandGroup(
                                        new MagazinePullInBalls(), //
                                        new IntakeRollerSuckIn(), //
                                        new SystemShooterWarmUp(1000.0), //
                                        new HoodMove(() -> 7500.0), //
                                        new IntakePistonsSet(IntakePistons.INTAKE_DOWN)//
                        ));

                        addCommands(new WaitCommand(1.0));

                        // drive to ball 5
                        // addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 95,
                        // 80.0));
                        addCommands(new DriveToTarget(0.2, 30));
                        addCommands(new DriveStraight(0.2, 60.0));

                        // addCommands(new DriveStraightOnHeading(-0.4, DistanceUnits.INCHES, 100,
                        // 80.0));

                        // shoot the balls
                        addCommands(new ParallelRaceGroup(
                                        new SystemFireWhenReady(() -> 1000.0), new WaitCommand(2.0)));
                }
        }

        /** Creates a new FiveBallPath. */
        public FiveBallPath() {
                // Add your commands in the addCommands() call, e.g.
                // addCommands(new FooCommand(), new BarCommand());

                addCommands(new SystemZero());

                addCommands(new IntakePistonsSet(IntakePistons.INTAKE_DOWN));
                addCommands(new HoodMove(() -> Hood.AUTO_5_BALL_FIRST_SHOT));

                addCommands(new MagazinePullInBalls());

                // move away from the hub
                addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 2 * 12.0, 160.0));

                addCommands(new IntakeRollerSuckIn());
                addCommands(new SystemShooterWarmUp(Shooter.CLOSE_SHOT));

                // drive to get the 2nd ball
                addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 7 * 12.0, 160.0));
                addCommands(new DriveStraightOnHeading(0.3, DistanceUnits.INCHES, 12, 160.0));
                addCommands(new DriveStraightOnHeading(0.3, DistanceUnits.INCHES, 12, 120));

                addCommands(new IntakeRollerSuckIn());
                // shoot the balls
                addCommands(new ParallelRaceGroup(
                                new SystemFireWhenReady(() -> Shooter.CLOSE_SHOT),
                                new WaitCommand(3.0), new WaitForNumBalls(2)));

                // drive down range.
                addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 20, 190.0));
                addCommands(new DriveStraightOnHeading(0.8, DistanceUnits.INCHES, 110, 190.0));
                addCommands(new DriveStraightOnHeading(0.5, DistanceUnits.INCHES, 20, 190.0));

                addCommands(new IntakeRollerSuckIn());
                addCommands(new MagazinePullInBalls());

                // get ball 3 and 4.
                addCommands(new DriveStraightOnHeading(0.4, DistanceUnits.INCHES, 90,
                                140.0));

                addCommands(new WaitCommand(0.2));

                // drive back to the hub. Accelerate with 3 commands
                addCommands(new DriveStraightOnHeading(-0.2, DistanceUnits.INCHES, 20,
                                175.0));
                addCommands(new DriveStraightOnHeading(-0.4, DistanceUnits.INCHES, 20,
                                175.0));
                addCommands(new DriveStraightOnHeading(-0.8, DistanceUnits.INCHES, 240,
                                175.0));
                // addCommands(new DriveStraightOnHeading(-0.5, DistanceUnits.INCHES, 20,
                // 175.0));

                addCommands(new ParallelCommandGroup(
                                new SystemShooterWarmUp(Shooter.CLOSE_SHOT),
                                // slow down
                                new DriveStraightOnHeading(-0.3, DistanceUnits.INCHES, 20, 175.0)));

                // turn towards hub
                addCommands(new DriveStraightOnHeading(-0.3, DistanceUnits.INCHES, 40,
                                111.0));

                // shoot the balls
                addCommands(new ParallelRaceGroup(
                                new SystemFireWhenReady(() -> Shooter.CLOSE_SHOT), new WaitCommand(2.0),
                                new WaitForNumBalls(2)));

                addCommands(new IntakeRollerSuckIn());
                addCommands(new MagazinePullInBalls());

                addCommands(new ShortTargetAndShootBall());
        }
}
