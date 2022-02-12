package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.*;

public class IntakeRollers extends SubsystemBase {

    private final VictorSPX rollersTalon = new WPI_VictorSPX(Constants.Talon.INTAKE_ROLLERS);

    private final double ROLLER_INTAKE = 0.7;
    private final double ROLLER_STOP = 0.0;

    public IntakeRollers() {
        rollersTalon.setNeutralMode(NeutralMode.Coast);
        rollersTalon.configNominalOutputForward(+0.0f);
        rollersTalon.configNominalOutputReverse(0.0);
        rollersTalon.configPeakOutputForward(+12.0);
        rollersTalon.configPeakOutputReverse(-12.0);
    }

    public void suckIn() {
        rollersTalon.set(VictorSPXControlMode.PercentOutput, -ROLLER_INTAKE);
    }

    public void spitOut() {
        rollersTalon.set(VictorSPXControlMode.PercentOutput, ROLLER_INTAKE);
    }

    public void stop() {
        rollersTalon.set(VictorSPXControlMode.PercentOutput, ROLLER_STOP);
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Intake Speed", rollersTalon.getMotorOutputPercent());
    }
}
