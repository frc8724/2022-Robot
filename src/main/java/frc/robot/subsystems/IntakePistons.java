// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakePistons extends SubsystemBase {

  public static final boolean INTAKE_DOWN = false;
  public static final boolean INTAKE_UP = true;

  private final Solenoid piston = new Solenoid(PneumaticsModuleType.CTREPCM,
      Constants.Solenoid.INTAKE);

  /** Creates a new IntakePistons. */
  public IntakePistons() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateSmartDashboard();
  }

  public void Up() {
    piston.set(INTAKE_UP);

  }

  public void Down() {
    piston.set(INTAKE_DOWN);
  }

  public void set(boolean b) {
    piston.set(b);
  }

  private void updateSmartDashboard() {
    SmartDashboard.putBoolean("Intake Position", piston.get());
  }
}
