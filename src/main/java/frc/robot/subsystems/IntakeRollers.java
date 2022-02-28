package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.*;

import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.util.MayhemTalonSRX.CurrentLimit;

public class IntakeRollers extends SubsystemBase {

    private final MayhemTalonSRX rollersTalon = new MayhemTalonSRX(Constants.Talon.INTAKE_ROLLERS,
            CurrentLimit.LOW_CURRENT);

    private final double ROLLER_INTAKE = 0.95;
    private final double ROLLER_STOP = 0.0;

    public IntakeRollers() {
        rollersTalon.setNeutralMode(NeutralMode.Coast);
        rollersTalon.configNominalOutputForward(+0.0f);
        rollersTalon.configNominalOutputReverse(0.0);
        rollersTalon.configPeakOutputForward(+12.0);
        rollersTalon.configPeakOutputReverse(-12.0);
    }

    // boolean suckInFlag = false;
    // int reverseCount = 0;

    public void suckIn() {
        rollersTalon.set(ControlMode.PercentOutput, -ROLLER_INTAKE);
        // suckInFlag = true;
        // reverseCount = 0;
    }

    @Override
    public void periodic() {
        // if (suckInFlag) {
        // if (rollersTalon.getStatorCurrent() > 20) {
        // reverseCount = 30;
        // }

        // if (reverseCount > 0) {
        // reverseCount--;
        // spitOut();
        // } else {
        // rollersTalon.set(ControlMode.PercentOutput, -ROLLER_INTAKE);
        // }
        // } else {
        // reverseCount = 0;
        // }
    }

    public void spitOut() {
        rollersTalon.set(ControlMode.PercentOutput, ROLLER_INTAKE);
    }

    public void stop() {
        rollersTalon.set(ControlMode.PercentOutput, ROLLER_STOP);
        // suckInFlag = false;
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Intake Speed", rollersTalon.getMotorOutputPercent());

        SmartDashboard.putNumber("Intake Current", rollersTalon.getStatorCurrent());
    }
}
