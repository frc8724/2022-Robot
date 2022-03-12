// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class Targeting extends SubsystemBase {
  private final double fieldOfViewDegrees = 78.0;

  /** Creates a new Targeting. */
  public Targeting() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Get the heading (bearing) to target.
   * Positive is to the right.
   * Negative is to the left.
   * NaN is no target.
   * 
   * @return
   */
  public double getHeadingToTarget() {
    var x = RobotContainer.vision.getXLocationOfTarget();

    if (x < 0) {
      return Double.NaN;
    }
    double targetHeading;
    // if the target is on the right half...
    if (x > 0.5) {
      targetHeading = (x - 0.5) * fieldOfViewDegrees / 2;
    } else {
      targetHeading = -x * fieldOfViewDegrees / 2;
    }

    return targetHeading;
  }
}
