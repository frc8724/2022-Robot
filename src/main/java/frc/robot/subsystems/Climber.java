package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;

import org.mayheminc.util.PidTunerObject;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.MayhemTalonSRX;

public class Climber extends SubsystemBase implements PidTunerObject {
    private final Solenoid strongArmPiston = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.Solenoid.CLIMBER);
    private final MayhemTalonSRX leftTalon = new MayhemTalonSRX(Constants.Talon.CLIMBER_L);
    private final MayhemTalonSRX rightTalon = new MayhemTalonSRX(Constants.Talon.CLIMBER_R);

    public static final boolean ARMS_UP = true;
    public static final boolean ARMS_DOWN = false;
    public static final double ARMS_OUT_POSITION = 100.0;
    public static final double ARMS_UNHOOK_POSITION = 75.0;
    public static final double ARMS_IN_POSITION = 50.0;

    private final double MAX_POSITION = 150.0;
    private final double MIN_POSITION = 25.0;
    private final double POSIITON_TOLERANCE = 100;

    double m_target;

    public Climber() {
        ConfigureTalon(leftTalon);
        ConfigureTalon(rightTalon);
    }

    private void ConfigureTalon(MayhemTalonSRX talon) {
        talon.config_kP(0, 10.0, 0);
        talon.config_kI(0, 0.0, 0);
        talon.config_kD(0, 0.0, 0);
        talon.config_kF(0, 0.0, 0);

        talon.setNeutralMode(NeutralMode.Coast);

        talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        talon.configNominalOutput(0, 0);
        talon.configPeakOutputVoltage(1, -1);
        talon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 40, 60, 0.5));

        talon.configNominalOutput(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(100.0, -100.0);
        talon.setInverted(false);
        talon.setSensorPhase(false);

        talon.configForwardSoftLimitThreshold(MAX_POSITION);
        talon.configForwardSoftLimitEnable(true);
        talon.configReverseSoftLimitThreshold(MIN_POSITION);
        talon.configReverseSoftLimitEnable(true);
    }

    public void setArmPositionTo(boolean b) {
        strongArmPiston.set(b);
    }

    public void setArmLengthTo(double d) {
        m_target = d;
        leftTalon.set(ControlMode.Position, m_target);
        rightTalon.set(ControlMode.Position, m_target);
    }

    public boolean isAtPosition() {
        boolean left = Math.abs(leftTalon.getSelectedSensorPosition() - m_target) < POSIITON_TOLERANCE;
        boolean right = Math.abs(rightTalon.getSelectedSensorPosition() - m_target) < POSIITON_TOLERANCE;
        return left && right;
    }

    public void stop() {
        setArmLengthTo(leftTalon.getSelectedSensorPosition());
    }

    @Override
    public double getP() {
        return leftTalon.kP;
    }

    @Override
    public double getI() {
        return leftTalon.kI;
    }

    @Override
    public double getD() {
        return leftTalon.kD;
    }

    @Override
    public double getF() {
        return leftTalon.kF;
    }

    @Override
    public void setP(double d) {
        leftTalon.config_kP(0, d, 0);
        rightTalon.config_kP(0, d, 0);
    }

    @Override
    public void setI(double d) {
        leftTalon.config_kI(0, d, 0);
        rightTalon.config_kI(0, d, 0);
    }

    @Override
    public void setD(double d) {
        leftTalon.config_kD(0, d, 0);
        rightTalon.config_kD(0, d, 0);
    }

    @Override
    public void setF(double d) {
        leftTalon.config_kF(0, d, 0);
        rightTalon.config_kF(0, d, 0);
    }

    public void zero() {
        leftTalon.setSelectedSensorPosition(0.0);
        rightTalon.setSelectedSensorPosition(0.0);
    }

}
