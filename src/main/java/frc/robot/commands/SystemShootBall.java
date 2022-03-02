// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SystemShootBall extends SequentialCommandGroup {
  static public final double LongShot = 1750;
  static private final double ShortShot = 1000;
  static private final double LowGoalShot = 500;

  static double shortShot = ShortShot;
  static double lowGoalShot = LowGoalShot;

  public static double getShortShot() {
    return shortShot;
  }

  public static void adjustShortShot(double d) {
    shortShot += d;
  }

  public static double getLowGoalShot() {
    return lowGoalShot;
  }

  public static void adjustLowGoalShot(double d) {
    lowGoalShot += d;
  }

  /** Creates a new SystemShootBall. */
  public SystemShootBall(Supplier<Double> speed, Supplier<Double> hood) {
    // double longShot = 1850;
    // double shortShot = 900;
    // start the shooter and accelerator wheel and wait at least 1 second.

    addCommands(new HoodMove(hood));

    addCommands(
        new ParallelCommandGroup(new ShooterSetSpeed(speed), new ShooterSetAccelerator(0.4), new WaitCommand(1.0)));

    addCommands(new ParallelRaceGroup(new SystemFireWhenReady(speed), new WaitCommand(4.0)));
  }

  @Override
  public void end(boolean interrupted) {
    RobotContainer.shooter.setShooterSpeed(0.0);
    RobotContainer.accelerator.setAcceleratorSpeedVBus(0.0);
    RobotContainer.loader.setSpeed(0.0);
    RobotContainer.magazine.setSpeed(0.0);
  }
}
