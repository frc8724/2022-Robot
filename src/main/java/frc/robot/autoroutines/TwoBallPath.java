// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.*;
import frc.robot.commands.DriveStraightOnHeading.DistanceUnits;
import frc.robot.subsystems.IntakePistons;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TwoBallPath extends SequentialCommandGroup {
  /**
   * Creates a new TwoBallPath.
   * Gather a ball.
   * Go back to starting position
   * Shoot 2 balls.
   */
  public TwoBallPath(boolean driveForward) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    addCommands(new SystemZero());

    addCommands(new IntakePistonsSet(IntakePistons.INTAKE_DOWN));
    // addCommands(new WaitCommand(1.0));

    addCommands(new HoodMove(() -> {
      return 4000.0;
    }));

    addCommands(new ShooterSetAccelerator(0.5));
    // addCommands(new IntakeRollerSuckIn());
    addCommands(new MagazinePullInBalls());

    addCommands(new DriveStraightOnHeading(0.2, DistanceUnits.INCHES, 64.0, -60));

    addCommands(new DriveStraightOnHeading(-0.2, DistanceUnits.INCHES, 70.0, +80));

    // fire
    addCommands(new ParallelRaceGroup(new SystemFireWhenReady(() -> 800.0), new WaitCommand(2.0)));

    if (driveForward) {
      addCommands(new DriveStraightOnHeading(0.2, DistanceUnits.INCHES, 80.0, 20));
    }
  }
}