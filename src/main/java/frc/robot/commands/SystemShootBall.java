// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Hood;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SystemShootBall extends SequentialCommandGroup {
  static public final double LongShot = 1850;
  static public final double ShortShot = 900;

  /** Creates a new SystemShootBall. */
  public SystemShootBall(double speed, double hood) {
    // double longShot = 1850;
    // double shortShot = 900;
    // start the shooter and accelerator wheel and wait at least 1 second.
    addCommands(new HoodMove(hood));

    addCommands(
        new ParallelCommandGroup(new ShooterSetSpeed(speed), new ShooterSetAccelerator(0.5), new WaitCommand(1.5)));

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
