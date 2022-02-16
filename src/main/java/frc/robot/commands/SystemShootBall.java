// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SystemShootBall extends SequentialCommandGroup {
  /** Creates a new SystemShootBall. */
  public SystemShootBall() {

    // start the shooter and accelerator wheel and wait at least 1 second.
    addCommands(
        new ParallelCommandGroup(new ShooterSetSpeed(1000), new ShooterSetAccelerator(0.5), new WaitCommand(1.5)));

    addCommands(new SystemFireWhenReady(1000));
  }

  @Override
  public void end(boolean interrupted) {
  }
}
