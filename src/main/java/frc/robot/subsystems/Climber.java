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
    private final int PositionControl = 0;
    private final int VelocityControl = 1;

    private final MayhemTalonSRX leftTalon = new MayhemTalonSRX(Constants.Talon.CLIMBER_L, CurrentLimit.HIGH_CURRENT);
    private final MayhemTalonSRX rightTalon = new MayhemTalonSRX(Constants.Talon.CLIMBER_R, CurrentLimit.HIGH_CURRENT);

    private final DigitalInput leftTopLimit = new DigitalInput(Constants.DigitalInput.LEFT_CLIMBER_TOP_LIMIT);
    private final DigitalInput rightTopLimit = new DigitalInput(Constants.DigitalInput.RIGHT_CLIMBER_TOP_LIMIT);

    private final DigitalInput leftBottomLimit = new DigitalInput(Constants.DigitalInput.LEFT_CLIMBER_BOTTOM_LIMIT);
    private final DigitalInput rightBottomLimit = new DigitalInput(Constants.DigitalInput.RIGHT_CLIMBER_BOTTOM_LIMIT);

    public static final double ARMS_OUT_POSITION = 435000.0;
    public static final double ARMS_UNHOOK_POSITION = 100000.0;
    public static final double ARMS_IN_POSITION = 20000.0;

    public static final double MAX_POSITION = 435000.0;
    public static final double MIN_POSITION = 0.0;
    public static final double POSITION_TOLERANCE = 10000;
    public static final double CLOSE_TO_LIMIT = 50000;

    public static final double TEST_1 = 100000.0;
    public static final double TEST_2 = 150000.0;
    public static final double TEST_3 = 200000.0;
    public static final double TEST_4 = 300000.0;

    double m_leftSpeed;
    double m_rightSpeed;

    boolean m_leftGoingUp;
    boolean m_leftGoingDown;
    boolean m_rightGoingUp;
    boolean m_rightGoingDown;

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
        talon.config_kP(PositionControl, 1.0, 0);
        talon.config_kI(PositionControl, 0.01, 0);
        talon.config_kD(PositionControl, 40.0, 0);
        talon.config_kF(PositionControl, 0.0, 0);

        // velocity PID constants.
        talon.config_kP(VelocityControl, 0.01, 0);
        talon.config_kI(VelocityControl, 0.00, 0);
        talon.config_kD(VelocityControl, 0.0, 0);
        talon.config_kF(VelocityControl, 1023.0 / 1000.0, 0);

        talon.changeControlMode(ControlMode.Position);
        talon.setNeutralMode(NeutralMode.Brake);
        talon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(+12.0, -12.0);

        talon.configAllowableClosedloopError(0, POSITION_TOLERANCE, 0);

        talon.configForwardSoftLimitThreshold(MAX_POSITION);
        // talon.configForwardSoftLimitEnable(true);
        talon.configReverseSoftLimitThreshold(MIN_POSITION);
        // talon.configReverseSoftLimitEnable(true);

        talon.configMaxIntegralAccumulator(0, 10000);
    }

    public void setArmExtensionVelocity(double speed) {
        m_leftSpeed = m_rightSpeed = speed;

        m_leftGoingUp = speed > 0;
        m_leftGoingDown = speed < 0;

        m_rightGoingUp = speed > 0;
        m_rightGoingDown = speed < 0;

        rightTalon.selectProfileSlot(VelocityControl, 0);
        leftTalon.selectProfileSlot(VelocityControl, 0);

        rightTalon.set(ControlMode.Velocity, speed);
        leftTalon.set(ControlMode.Velocity, speed);
    }

    public void setArmLengthTo(double d) {
        m_target = d;

        m_leftGoingUp = m_target > MAX_POSITION / 2;
        m_leftGoingDown = m_target < MAX_POSITION / 2;
        m_rightGoingUp = m_target > MAX_POSITION / 2;
        m_rightGoingDown = m_target < MAX_POSITION / 2;

        rightTalon.selectProfileSlot(PositionControl, 0);
        leftTalon.selectProfileSlot(PositionControl, 0);

        leftTalon.set(ControlMode.Position, m_target);
        rightTalon.set(ControlMode.Position, m_target);
    }

    public void setArmLengthPowerTo(double d) {
        // Up
        if (d > 0) {
            applyPowerToTalon(leftTalon, leftTopLimit, d);
            applyPowerToTalon(rightTalon, rightTopLimit, d);
        } else { // down
            applyPowerToTalon(leftTalon, leftBottomLimit, d);
            applyPowerToTalon(rightTalon, rightBottomLimit, d);
        }
    }

    private void applyPowerToTalon(MayhemTalonSRX talon, DigitalInput limit, double d) {
        if (limit.get()) {
            talon.set(ControlMode.PercentOutput, 0);
        } else {
            talon.set(ControlMode.PercentOutput, d);
        }
    }

    public boolean isAtPosition() {
        boolean left = Math.abs(leftTalon.getSelectedSensorPosition() - m_target) < POSITION_TOLERANCE;
        boolean right = Math.abs(rightTalon.getSelectedSensorPosition() - m_target) < POSITION_TOLERANCE;
        return left && right;
    }

    public void stop() {
        leftTalon.set(ControlMode.PercentOutput, 0.0);
        rightTalon.set(ControlMode.PercentOutput, 0.0);
    }

    public boolean isAtTop() {
        return rightTopLimit.get() && leftTopLimit.get();
    }

    public boolean isAtBottom() {
        return rightBottomLimit.get() && leftBottomLimit.get();
    }

    @Override
    public void periodic() {
        updateSmartDashboard();

        // check the right limit switches
        if (rightTopLimit.get() && m_rightGoingUp) {
            m_rightSpeed = 0;
            m_rightGoingUp = false;
            rightTalon.set(ControlMode.Velocity, 0.0);
        }
        if (rightBottomLimit.get() && m_rightGoingDown) {
            m_rightSpeed = 0;
            m_rightGoingDown = false;
            rightTalon.set(ControlMode.Velocity, 0.0);
        }

        // check the left limit switches
        if (leftTopLimit.get() && m_leftGoingUp) {
            m_leftSpeed = 0;
            m_leftGoingUp = false;
            leftTalon.set(ControlMode.Velocity, 0.0);
        }
        if (leftBottomLimit.get() && m_leftGoingDown) {
            m_leftSpeed = 0;
            m_leftGoingDown = false;
            leftTalon.set(ControlMode.Velocity, 0.0);
        }

    }

    private void updateSmartDashboard() {
        SmartDashboard.putNumber("Climber Left Pos", leftTalon.getSelectedSensorPosition());
        SmartDashboard.putNumber("Climber Right Pos", rightTalon.getSelectedSensorPosition());
        SmartDashboard.putNumber("Climber Target", m_target);

        SmartDashboard.putBoolean("Climber Limit Left Top", leftTopLimit.get());
        SmartDashboard.putBoolean("Climber Limit Right Top", rightTopLimit.get());
        SmartDashboard.putBoolean("Climber Limit Left Bottom", leftBottomLimit.get());
        SmartDashboard.putBoolean("Climber Limit Right Bottom", rightBottomLimit.get());

        SmartDashboard.putNumber("Climber Velocity", rightTalon.getSpeed());
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
