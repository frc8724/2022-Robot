// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.DriveToTarget;
import frc.robot.commands.*;
import frc.robot.commands.SystemZero;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TestAutoTargeting extends SequentialCommandGroup {
  /** Creates a new TestAutoTargeting. */
  public TestAutoTargeting() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    addCommands(new InstantCommand(() -> RobotContainer.cameraLights.on()));
    // addCommands(new WaitCommand(1.0));
    // addCommands(new IntakePistonsSet(IntakePistons.INTAKE_DOWN));
    // addCommands(new WaitCommand(3));
    // addCommands(new IntakeRollerSuckIn());
    // addCommands(new MagazinePullInBalls());
    addCommands(new HoodMove(() -> 7200.0));
    // addCommands(new InstantCommand(() -> {
    // RobotContainer.shooter.setShooterSpeed(Shooter.getShortShot());
    // RobotContainer.accelerator.setAcceleratorSpeedVBus(0.4);
    // }, RobotContainer.shooter, RobotContainer.accelerator));

    addCommands(new DriveToTarget(-0.3, 80));
    // addCommands(new DriveStraightOnHeading(-0.3, DistanceUnits.INCHES, 90, 111));

    addCommands(new InstantCommand(() -> RobotContainer.cameraLights.off()));
    // addCommands(new WaitCommand(5.0));

    // fire
    addCommands(new ParallelRaceGroup(new SystemFireWhenReady(() -> 5000.0), new WaitCommand(3.0)));

  }
}
