package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.util.PidTunerObject;
import org.mayheminc.util.MayhemTalonSRX.CurrentLimit;

public class Shooter extends SubsystemBase implements PidTunerObject {
    private final MayhemTalonSRX shooterWheelLeft = new MayhemTalonSRX(Constants.Talon.SHOOTER_WHEEL_L,
            CurrentLimit.HIGH_CURRENT);
    private final MayhemTalonSRX shooterWheelRight = new MayhemTalonSRX(Constants.Talon.SHOOTER_WHEEL_R,
            CurrentLimit.HIGH_CURRENT);

    private final double TALON_TICKS_PER_REV = 2048.0;
    private final double SECONDS_PER_MINUTE = 60.0;
    private final double HUNDRED_MS_PER_SECOND = 10.0;

    public static final double IDLE_SPEED = 1000.0;
    public static final double CLOSE_SHOOTING_SPEED = 4000.0;
    public static final double INITIATION_LINE_SPEED = 4500.0;
    public static final double TRENCH_FRONT_SPEED = 3400.0;
    public static final double MAX_SPEED_RPM = 5000;
    public static final double SPEED_TOLERANCE = 50;

    double m_targetSpeedRPM;

    // Note: for ease of thinking, 1 RPM =~ 3.4 native units for the shooter
    double convertRpmToTicksPer100ms(double rpm) {
        return rpm / SECONDS_PER_MINUTE * TALON_TICKS_PER_REV / HUNDRED_MS_PER_SECOND;
    }

    // Note: 3.413 native units =~ 1.0 RPM for the shooter
    double convertTicksPer100msToRPM(double ticks) {
        return ticks * HUNDRED_MS_PER_SECOND / TALON_TICKS_PER_REV * SECONDS_PER_MINUTE;
    }

    /**
     * Creates a new Shooter.
     */
    public Shooter() {
        configureWheelFalcons();
    }

    public void init() {
        configureWheelFalcons();
        setShooterSpeedVBus(0.0);
    }

    // configure a pair of shooter wheel falcons
    private void configureWheelFalcons() {
        // most of the configuration is shared for the two Falcons
        configureOneWheelFalcon(shooterWheelLeft);
        configureOneWheelFalcon(shooterWheelRight);

        // with the exception of one rotating the opposite direction
        shooterWheelLeft.setInverted(true);
        shooterWheelRight.setInverted(false);
    }

    private void configureOneWheelFalcon(MayhemTalonSRX shooterWheelFalcon) {
        shooterWheelFalcon.setFeedbackDevice(FeedbackDevice.IntegratedSensor);
        shooterWheelFalcon.setNeutralMode(NeutralMode.Coast);
        shooterWheelFalcon.configNominalOutputVoltage(+0.0f, -0.0f);
        shooterWheelFalcon.configPeakOutputVoltage(+12.0, 0.0);
        shooterWheelFalcon.configNeutralDeadband(0.001); // Config neutral deadband to be the smallest possible

        // For PIDF computations, 1023 is interpreted as "full" motor output
        // Velocity is in native units of TicksPer100ms.
        // 1000rpm =~ 3413 native units.
        // P of "3.0" means that full power applied with error of 341 native units =
        // 100rpm
        // (above also means that 50% power boost applied with error of 50rpm)
        // https://docs.ctre-phoenix.com/en/stable/ch16_ClosedLoop.html#calculating-velocity-feed-forward-gain-kf
        shooterWheelFalcon.config_kP(0, 2.0, 0); // if we are 100 rpm off, then apply 10% more output = 10% * 1023 / 100
                                                 // rpm
        shooterWheelFalcon.config_kI(0, 0.0, 0);
        shooterWheelFalcon.config_kD(0, 10.0, 0); // CTRE recommends starting at 10x P-gain
        shooterWheelFalcon.config_kF(0, 1023.0 * 0.2 / convertRpmToTicksPer100ms(1035), 0); // at 0.2 Percent VBus, the
                                                                                            // shooter is at 1035
        shooterWheelFalcon.configAllowableClosedloopError(0, 0, 0); // no "neutral" zone around target
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        UpdateDashboard();
    }

    private void UpdateDashboard() {
        SmartDashboard.putNumber("Shooter Wheel RPM",
                convertTicksPer100msToRPM(shooterWheelLeft.getSelectedSensorVelocity(0)));

        SmartDashboard.putNumber("Shooter Wheel target RPM", m_targetSpeedRPM);
        SmartDashboard.putNumber("Shooter Wheel Error",
                m_targetSpeedRPM - convertTicksPer100msToRPM(shooterWheelLeft.getSelectedSensorVelocity(0)));
    }

    public void zero() {
    }

    /**
     * Set shooter to rpm speed.
     * 
     * @param rpm
     */
    public void setShooterSpeed(double rpm) {

        if (rpm > MAX_SPEED_RPM) {
            rpm = MAX_SPEED_RPM;
        }
        if (rpm < 0)
            rpm = 0;

        m_targetSpeedRPM = rpm;
        System.out.println("setShooterSpeed: " + rpm);
        double ticks = convertRpmToTicksPer100ms(rpm);
        shooterWheelLeft.set(ControlMode.Velocity, ticks);
        shooterWheelRight.set(ControlMode.Velocity, ticks);
    }

    public void setShooterSpeedVBus(double pos) {
        shooterWheelLeft.set(ControlMode.PercentOutput, pos);
        shooterWheelRight.set(ControlMode.PercentOutput, pos);
    }

    public double getShooterSpeed() {
        return convertTicksPer100msToRPM(shooterWheelLeft.getSelectedSensorVelocity(0));
    }

    public double getShooterTargetSpeed() {

        System.out.println("getShooterTargetSpeed: " + m_targetSpeedRPM);
        return m_targetSpeedRPM;
    }

    public boolean isShooterAtSpeed() {
        // the shooter has trouble locking into a low speed. Double the tolerance
        if (m_targetSpeedRPM < 600) {
            return Math.abs(m_targetSpeedRPM - getShooterSpeed()) < SPEED_TOLERANCE * 2;
        } else {
            return Math.abs(m_targetSpeedRPM - getShooterSpeed()) < SPEED_TOLERANCE;
        }
    }

    public double getShooterSpeedVBus() {
        return shooterWheelLeft.getMotorOutputVoltage();
    }

    ////////////////////////////////////////////////////
    // PidTunerObject
    @Override
    public double getP() {
        return shooterWheelLeft.getP();
    }

    @Override
    public double getI() {
        return shooterWheelLeft.getI();
    }

    @Override
    public double getD() {
        return shooterWheelLeft.getD();
    }

    @Override
    public double getF() {
        return shooterWheelLeft.getF();
    }

    @Override
    public void setP(double d) {
        shooterWheelLeft.config_kP(0, d, 0);
        shooterWheelRight.config_kP(0, d, 0);
    }

    @Override
    public void setI(double d) {
        shooterWheelLeft.config_kI(0, d, 0);
        shooterWheelRight.config_kI(0, d, 0);
    }

    @Override
    public void setD(double d) {
        shooterWheelLeft.config_kD(0, d, 0);
        shooterWheelRight.config_kD(0, d, 0);
    }

    @Override
    public void setF(double d) {
        shooterWheelLeft.config_kF(0, d, 0);
        shooterWheelRight.config_kF(0, d, 0);
    }

}