package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;

public class Magazine extends SubsystemBase {
    private final VictorSPX motorControl = new WPI_VictorSPX(Constants.Talon.INTAKE_ROLLERS);

    private final double IN_SPEED = 0.6;
    private final double OUT_SPEED = -0.5;

    public Magazine() {
        motorControl.setNeutralMode(NeutralMode.Coast);
        motorControl.configNominalOutputForward(+0.0f);
        motorControl.configNominalOutputReverse(0.0);
        motorControl.configPeakOutputForward(+12.0);
        motorControl.configPeakOutputReverse(-12.0);
    }

    public void moveBallsToShooter() {
        motorControl.set(VictorSPXControlMode.PercentOutput, IN_SPEED);
    }

    public void moveBallsToIntake() {
        motorControl.set(VictorSPXControlMode.PercentOutput, OUT_SPEED);
    }
}
