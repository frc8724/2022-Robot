// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.HoodMove;
import frc.robot.commands.SystemShootBall;
import frc.robot.commands.SystemZero;
import frc.robot.subsystems.Hood;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ShootLong extends SequentialCommandGroup {
  /** Creates a new ShootLong. */
  public ShootLong() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new SystemZero());

    // addCommands(new HoodMove(Hood.LONGEST_SHOT));

    // addCommands(new SystemShootBall(SystemShootBall.LongShot, Ho));
  }
}
