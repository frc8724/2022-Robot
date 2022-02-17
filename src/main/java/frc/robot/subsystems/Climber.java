package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;

// import org.mayheminc.util.MayhemTalonFX/;
import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.util.PidTunerObject;
import org.mayheminc.util.MayhemTalonSRX.CurrentLimit;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase implements PidTunerObject {
    private final Solenoid strongArmPiston = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.Solenoid.CLIMBER);
    private final MayhemTalonSRX leftTalon = new MayhemTalonSRX(Constants.Talon.CLIMBER_L, CurrentLimit.HIGH_CURRENT);
    private final MayhemTalonSRX rightTalon = new MayhemTalonSRX(Constants.Talon.CLIMBER_R, CurrentLimit.HIGH_CURRENT);

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

        leftTalon.setInverted(true);
        rightTalon.setInverted(false);

        leftTalon.setSensorPhase(false);
        rightTalon.setSensorPhase(false);
    }

    private void ConfigureTalon(MayhemTalonSRX talon) {
        talon.config_kP(0, 10.0, 0);
        talon.config_kI(0, 0.0, 0);
        talon.config_kD(0, 0.0, 0);
        talon.config_kF(0, 0.0, 0);

        talon.changeControlMode(ControlMode.Position);
        talon.setNeutralMode(NeutralMode.Coast);
        talon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(+12.0, -12.0);

        // talon.configForwardSoftLimitThreshold(MAX_POSITION);
        // talon.configForwardSoftLimitEnable(true);
        // talon.configReverseSoftLimitThreshold(MIN_POSITION);
        // talon.configReverseSoftLimitEnable(true);
    }

    public void setArmPositionTo(boolean b) {
        strongArmPiston.set(b);
    }

    public void setArmLengthTo(double d) {
        m_target = d;
        leftTalon.set(ControlMode.Position, m_target);
        rightTalon.set(ControlMode.Position, m_target);
    }

    public void setArmLengthPowerTo(double d) {
        leftTalon.set(ControlMode.PercentOutput, d);
        rightTalon.set(ControlMode.PercentOutput, d);
    }

    public boolean isAtPosition() {
        boolean left = Math.abs(leftTalon.getSelectedSensorPosition() - m_target) < POSIITON_TOLERANCE;
        boolean right = Math.abs(rightTalon.getSelectedSensorPosition() - m_target) < POSIITON_TOLERANCE;
        return left && right;
    }

    public void stop() {
        // setArmLengthTo(leftTalon.getSelectedSensorPosition());
        leftTalon.set(ControlMode.PercentOutput, 0.0);
        rightTalon.set(ControlMode.PercentOutput, 0.0);
    }

    @Override
    public void periodic() {
        updateSmartDashboard();
    }

    int count;

    private void updateSmartDashboard() {
        SmartDashboard.putNumber("Climber Left Pos", leftTalon.getSelectedSensorPosition());
        SmartDashboard.putNumber("Climber Right Pos", rightTalon.getSelectedSensorPosition());
        // SmartDashboard.putNumber("Climber count", count++);
    }

    @Override
    public double getP() {
        return leftTalon.getP();
    }

    @Override
    public double getI() {
        return leftTalon.getI();
    }

    @Override
    public double getD() {
        return leftTalon.getD();
    }

    @Override
    public double getF() {
        return leftTalon.getF();
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
