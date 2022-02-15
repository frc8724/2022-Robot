// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.util.MayhemTalonSRX.CurrentLimit;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterAccelerator extends SubsystemBase {

  private final MayhemTalonSRX acceleratorWheelLeft = new MayhemTalonSRX(Constants.Talon.ACCELERATOR_WHEEL_L,
      CurrentLimit.HIGH_CURRENT);
  private final MayhemTalonSRX acceleratorWheelRight = new MayhemTalonSRX(Constants.Talon.ACCELERATOR_WHEEL_R,
      CurrentLimit.HIGH_CURRENT);

  /** Creates a new ShooterAccelerator. */
  public ShooterAccelerator() {
    configureAcceleratorWheels();
  }

  // public void init() {

  // configureAcceleratorWheels();

  // setAcceleratorSpeedVBus(0.0);
  // }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    UpdateDashboard();
  }

  private void UpdateDashboard() {
    SmartDashboard.putNumber("Accel L I", acceleratorWheelLeft.getStatorCurrent());
    SmartDashboard.putNumber("Accel R I", acceleratorWheelRight.getStatorCurrent());
  }

  private void configureAcceleratorWheels() {
    configureOneAcceleratorWheel(acceleratorWheelLeft);
    configureOneAcceleratorWheel(acceleratorWheelRight);

    acceleratorWheelLeft.setInverted(false);
    acceleratorWheelRight.setInverted(true);
  }

  private void configureOneAcceleratorWheel(MayhemTalonSRX acceleratorWheel) {
    acceleratorWheel.setNeutralMode(NeutralMode.Coast);
    acceleratorWheel.configNominalOutputVoltage(+0.0f, -0.0f);
    acceleratorWheel.configPeakOutputVoltage(+12.0, -12.0);
    acceleratorWheel.configNeutralDeadband(0.001); // Config neutral deadband to be the smallest possible
  }

  double accelSpeed;

  public void setAcceleratorSpeedVBus(double pos) {
    accelSpeed = pos;
    acceleratorWheelLeft.set(ControlMode.PercentOutput, pos);
    acceleratorWheelRight.set(ControlMode.PercentOutput, pos);
    System.out.println("setShooterSpeedVBus: " + pos);
  }

  public double getAcceleratorSpeedVBus() {
    return accelSpeed;
  }
}
