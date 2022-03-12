package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

// import org.mayheminc.util.MayhemTalonFX/;
import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.util.PidTunerObject;
import org.mayheminc.util.MayhemTalonSRX.CurrentLimit;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase implements PidTunerObject {
    private final Solenoid strongArmPiston = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.Solenoid.CLIMBER);
    private final MayhemTalonSRX leftTalon = new MayhemTalonSRX(Constants.Talon.CLIMBER_L, CurrentLimit.HIGH_CURRENT);
    private final MayhemTalonSRX rightTalon = new MayhemTalonSRX(Constants.Talon.CLIMBER_R, CurrentLimit.HIGH_CURRENT);

    private final DigitalInput leftLimit = new DigitalInput(Constants.DigitalInput.LEFT_CLIMBER_LIMIT);
    private final DigitalInput rightLimit = new DigitalInput(Constants.DigitalInput.RIGHT_CLIMBER_LIMIT);

    public static final boolean ARMS_UP = true;
    public static final boolean ARMS_DOWN = false;
    public static final double ARMS_OUT_POSITION = 330000.0;// 300000
    public static final double ARMS_UNHOOK_POSITION = 100000.0;
    public static final double ARMS_IN_POSITION = -20000.0;

    private final double MAX_POSITION = 3300000.0; // this is WAY TOO BIG
    private final double MIN_POSITION = -300000.0; // this is way too small
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
        talon.config_kP(0, 2.0, 0);
        talon.config_kI(0, 0.01, 0);
        talon.config_kD(0, 40.0, 0);
        talon.config_kF(0, 0.0, 0);

        talon.changeControlMode(ControlMode.Position);
        talon.setNeutralMode(NeutralMode.Brake);
        talon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(+12.0, -12.0);

        talon.configAllowableClosedloopError(0, POSIITON_TOLERANCE, 0);

        talon.configForwardSoftLimitThreshold(MAX_POSITION);
        talon.configForwardSoftLimitEnable(true);
        talon.configReverseSoftLimitThreshold(MIN_POSITION);
        talon.configReverseSoftLimitEnable(true);

        talon.configMaxIntegralAccumulator(0, 10000);
    }

    public void setArmPositionTo(boolean b) {
        strongArmPiston.set(b);
    }

    // public void setArmLengthTo(double d) {
    // m_target = d;
    // leftTalon.set(ControlMode.Position, m_target);
    // rightTalon.set(ControlMode.Position, m_target);
    // }

    public void setArmLengthPowerTo(double d) {
        rightTalon.set(ControlMode.PercentOutput, d);

        // Up
        if (d > 0) {
            ApplyTopPowerToTalon(leftTalon, leftLimit, d);

        } else { // down
            ApplyBottomPowerToTalon(leftTalon, leftLimit, d);
        }

        // Up
        if (d > 0) {
            ApplyTopPowerToTalon(rightTalon, rightLimit, d);

        } else { // down
            ApplyBottomPowerToTalon(rightTalon, rightLimit, d);
        }
    }

    private void ApplyTopPowerToTalon(MayhemTalonSRX talon, DigitalInput limit, double d) {

        // if we are close to the top and the limit is pressed, stop
        if (talon.getSelectedSensorPosition() > MAX_POSITION / 2 &&
                limit.get()) {
            // turn off
            talon.set(ControlMode.PercentOutput, 0);
        } else {
            // apply power
            talon.set(ControlMode.PercentOutput, d);
        }
    }

    private void ApplyBottomPowerToTalon(MayhemTalonSRX talon, DigitalInput limit, double d) {

        // if we are close to the bottom and the limit is pressed, stop
        if (talon.getSelectedSensorPosition() < MAX_POSITION / 2 &&
                limit.get()) {
            // turn off
            talon.set(ControlMode.PercentOutput, 0);
        } else {
            // apply power
            talon.set(ControlMode.PercentOutput, d);
        }
    }

    public boolean isAtPosition() {
        boolean left = Math.abs(leftTalon.getSelectedSensorPosition() - m_target) < POSIITON_TOLERANCE;
        boolean right = Math.abs(rightTalon.getSelectedSensorPosition() - m_target) < POSIITON_TOLERANCE;
        return left && right;
    }

    public void stop() {
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
        SmartDashboard.putNumber("Climber Target", m_target);
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
        // setArmLengthTo(0.0);
    }

}
