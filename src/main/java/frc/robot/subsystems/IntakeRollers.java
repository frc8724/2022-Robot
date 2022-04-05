package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.util.MayhemTalonSRX.CurrentLimit;

public class IntakeRollers extends SubsystemBase {

    private final MayhemTalonSRX rollersTalon = new MayhemTalonSRX(Constants.Talon.INTAKE_ROLLERS,
            CurrentLimit.LOW_CURRENT);
    private final MayhemTalonSRX rollersTalon2 = new MayhemTalonSRX(Constants.Talon.INTAKE_ROLLERS_2,
            CurrentLimit.LOW_CURRENT);

    private final double ROLLER_INTAKE = 0.8;
    private final double ROLLER_STOP = 0.0;

    public IntakeRollers() {
        configureTalon(rollersTalon);
        configureTalon(rollersTalon2);

        rollersTalon.setInverted(true);
        rollersTalon2.setInverted(false);
    }

    void configureTalon(MayhemTalonSRX talon) {
        talon.setNeutralMode(NeutralMode.Coast);
        talon.configNominalOutputForward(+0.0f);
        talon.configNominalOutputReverse(0.0);
        talon.configPeakOutputForward(+12.0);
        talon.configPeakOutputReverse(-12.0);
    }

    public void suckIn() {
        setTalons(-ROLLER_INTAKE);
    }

    void setTalons(double d) {
        rollersTalon.set(ControlMode.PercentOutput, d);
        rollersTalon2.set(ControlMode.PercentOutput, d);
    }

    @Override
    public void periodic() {
    }

    public void spitOut() {
        setTalons(ROLLER_INTAKE);
    }

    public void stop() {
        setTalons(ROLLER_STOP);
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Intake Speed", rollersTalon.getMotorOutputPercent());
        SmartDashboard.putNumber("Intake Current", rollersTalon.getStatorCurrent());
    }
}
