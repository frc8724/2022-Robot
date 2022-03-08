// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
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
      return RobotContainer.hood.getHoodClosePosition();
    }));

    addCommands(new ShooterSetAccelerator(0.5));

    addCommands(new DriveStraightOnHeading(0.2, DistanceUnits.INCHES, 20.0, 90.0));
    addCommands(new IntakeRollerSuckIn());
    addCommands(new MagazinePullInBalls());
    addCommands(new DriveStraightOnHeading(0.3, DistanceUnits.INCHES, 100.0, 90.0));
    addCommands(new InstantCommand(() -> RobotContainer.cameraLights.on()));

    addCommands(new DriveStraightOnHeading(-0.3, DistanceUnits.INCHES, 60.0, 70.0));
    addCommands(new DriveStraightOnHeading(-0.2, DistanceUnits.INCHES, 40.0, 120.0));

    // fire
    addCommands(new ParallelRaceGroup(new SystemFireWhenReady(() -> 1000.0), new WaitCommand(3.0)));
    // was 165
    addCommands(new DriveStraightOnHeading(0.3, DistanceUnits.INCHES, 140.0, 170));

  }
}
