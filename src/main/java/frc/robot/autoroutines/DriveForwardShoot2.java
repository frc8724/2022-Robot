// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.*;
import frc.robot.subsystems.IntakePistons;
import frc.robot.subsystems.Shooter;
import frc.robot.RobotContainer;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class DriveForwardShoot2 extends SequentialCommandGroup {
  /** Creates a new DriveForwardShoot2. */
  public DriveForwardShoot2() {
    this(true);
  }

  public DriveForwardShoot2(boolean driveStraightAtEnd) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    addCommands(new SystemZero());

    addCommands(new IntakePistonsSet(IntakePistons.INTAKE_DOWN));
    addCommands(new IntakeRollerSuckIn());
    addCommands(new MagazinePullInBalls());

    addCommands(new DriveStraightOnHeading(0.3, 110.0, 111.0));

    addCommands(new HoodMove(() -> {
      return RobotContainer.hood.getHoodClosePosition();
    }));

    addCommands(new DriveStraightOnHeading(-0.3, 90.0, 111.0));

    // fire
    addCommands(new ParallelRaceGroup(new SystemFireWhenReady(() -> Shooter.getShortShot()), new WaitCommand(3.0)));

    if (driveStraightAtEnd) {
      addCommands(new DriveStraightOnHeading(0.3, 90.0, 111.0));
      addCommands(new DriveStraightOnHeading(0.2, 20.0, 111.0));
    }
  }
}
