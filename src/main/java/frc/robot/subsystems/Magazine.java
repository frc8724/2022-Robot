package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;

public class Magazine extends SubsystemBase {
    private final VictorSPX motorControl = new WPI_VictorSPX(Constants.Talon.INTAKE_ROLLERS);

    public Magazine() {
        motorControl.setNeutralMode(NeutralMode.Coast);
        motorControl.configNominalOutputForward(+0.0f);
        motorControl.configNominalOutputReverse(0.0);
        motorControl.configPeakOutputForward(+12.0);
        motorControl.configPeakOutputReverse(-12.0);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void setSpeed(double d) {
        motorControl.set(VictorSPXControlMode.PercentOutput, d);
    }

}
