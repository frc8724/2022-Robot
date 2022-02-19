package frc.robot.subsystems;

import frc.robot.Constants;
import org.mayheminc.util.History;
import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.util.MayhemTalonSRX.CurrentLimit;

import edu.wpi.first.wpilibj.*;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveBaseSubsystem extends SubsystemBase {
    History headingHistory = new History();

    PowerDistribution pdp = new PowerDistribution();

    // Brake modes
    public static final boolean BRAKE_MODE = true;
    public static final boolean COAST_MODE = false;

    HeadingCorrection headingCorrection = new HeadingCorrection();

    // Talons
    private final MayhemTalonSRX leftFrontTalon = new MayhemTalonSRX(Constants.Talon.DRIVE_LEFT_TOP,
            CurrentLimit.HIGH_CURRENT);
    private final MayhemTalonSRX leftRearTalon = new MayhemTalonSRX(Constants.Talon.DRIVE_LEFT_BOTTOM,
            CurrentLimit.HIGH_CURRENT);
    private final MayhemTalonSRX rightFrontTalon = new MayhemTalonSRX(Constants.Talon.DRIVE_RIGHT_TOP,
            CurrentLimit.HIGH_CURRENT);
    private final MayhemTalonSRX rightRearTalon = new MayhemTalonSRX(Constants.Talon.DRIVE_RIGHT_BOTTOM,
            CurrentLimit.HIGH_CURRENT);

    // Driving mode
    private final boolean m_speedRacerDriveMode = true; // set by default

    // Drive parameters
    // pi * diameter * (pulley ratios) / (counts per rev * gearbox reduction)
    public static final double DISTANCE_PER_PULSE_IN_INCHES = 3.14 * 5.75 * 36.0 / 42.0 / (2048.0 * 7.56); // corrected

    private boolean m_closedLoopMode = true;
    private final double m_maxWheelSpeed = 18000.0; // should be maximum wheel speed in native units
    private static final double CLOSED_LOOP_RAMP_RATE = 0.1; // time from neutral to full in seconds

    private double m_initialWheelDistance = 0.0;
    private int m_iterationsSinceRotationCommanded = 0;
    private int m_iterationsSinceMovementCommanded = 0;

    private static final int LOOPS_GYRO_DELAY = 10;

    /***********************************
     * INITIALIZATION
     **********************************************************/

    public DriveBaseSubsystem() {
        // confirm all four drive talons are in coast mode

        this.configTalon(leftFrontTalon);
        this.configTalon(leftRearTalon);
        this.configTalon(rightFrontTalon);
        this.configTalon(rightRearTalon);

        // set rear talons to follow their respective front talons
        leftRearTalon.follow(leftFrontTalon);
        rightRearTalon.follow(rightFrontTalon);

        // the left motors move the robot forwards with positive power
        // but the right motors are backwards.
        leftFrontTalon.setInverted(false);
        leftRearTalon.setInverted(false);
        rightFrontTalon.setInverted(true);
        rightRearTalon.setInverted(true);

        // talon closed loop config
        configureDriveTalon(leftFrontTalon);
        configureDriveTalon(rightFrontTalon);

        headingCorrection.zeroHeadingGyro(0.0);
    }

    private void configTalon(MayhemTalonSRX talon) {
        talon.setNeutralMode(NeutralMode.Coast);

        talon.configPeakCurrentLimit(60);
        talon.configContinuousCurrentLimit(40);
        talon.configPeakCurrentDuration(3000);

        talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(+12.0, -12.0);

        // configure current limits
        // enabled = true
        // 40 = limit (amps)
        // 60 = trigger_threshold (amps)
        // 0.5 = threshold time(s)
        talon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 40, 60, 0.5));
    }

    public void init() {
        // reset the NavX
        headingCorrection.zeroHeadingGyro(0.0);
    }

    private void configureDriveTalon(final MayhemTalonSRX talon) {
        final double wheelP = 0.020;
        final double wheelI = 0.000;
        final double wheelD = 0.200;
        final double wheelF = 0.060;
        final int slot = 0;
        final int timeout = 0;

        talon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);

        talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(12, -12);

        talon.config_kP(slot, wheelP, timeout);
        talon.config_kI(slot, wheelI, timeout);
        talon.config_kD(slot, wheelD, timeout);
        talon.config_kF(slot, wheelF, timeout);
        talon.configClosedloopRamp(CLOSED_LOOP_RAMP_RATE); // specify minimum time for neutral to full in seconds

        DriverStation.reportError("setWheelPIDF: " + wheelP + " " + wheelI + " " + wheelD + " " + wheelF + "\n", false);
    }

    // *********************** CLOSED-LOOP MODE ********************************

    public void toggleClosedLoopMode() {
        if (!m_closedLoopMode) {
            setClosedLoopMode();
        } else {
            setOpenLoopMode();
        }
    }

    public void setClosedLoopMode() {
        m_closedLoopMode = true;
    }

    public void setOpenLoopMode() {
        m_closedLoopMode = false;
    }

    // ********************* ENCODER-GETTERS ************************************

    private double getRightEncoder() {
        return rightFrontTalon.getSelectedSensorPosition(0);
    }

    private double getLeftEncoder() {
        return leftFrontTalon.getSelectedSensorPosition(0);
    }

    static private final double STATIONARY = 0.1;
    static private double m_prevLeftDistance = 0.0;
    static private double m_prevRightDistance = 0.0;

    public boolean isStationary() {
        final double leftDistance = getLeftEncoder();
        final double rightDistance = getRightEncoder();

        final double leftDelta = Math.abs(leftDistance - m_prevLeftDistance);
        final double rightDelta = Math.abs(rightDistance - m_prevRightDistance);

        m_prevLeftDistance = leftDistance;
        m_prevRightDistance = rightDistance;

        return leftDelta < STATIONARY && rightDelta < STATIONARY;
    }

    public static double twoDecimalPlaces(double d) {
        return ((double) ((int) (d * 100))) / 100;
    }

    public void stop() {
        setMotorPower(0.0, 0.0);
    }

    private void setMotorPower(double leftPower, double rightPower) {
        if (rightPower > 1.0)
            rightPower = 1.0;
        if (rightPower < -1.0)
            rightPower = -1.0;
        if (leftPower > 1.0)
            leftPower = 1.0;
        if (leftPower < -1.0)
            leftPower = -1.0;

        // System.out.printf("Left: %f Right: %f\n", leftPower, rightPower);

        if (m_closedLoopMode) {
            rightFrontTalon.set(ControlMode.Velocity, rightPower * m_maxWheelSpeed);
            leftFrontTalon.set(ControlMode.Velocity, leftPower * m_maxWheelSpeed);
        } else {
            rightFrontTalon.set(ControlMode.PercentOutput, rightPower);
            leftFrontTalon.set(ControlMode.PercentOutput, leftPower);
        }
    }

    /**
     * updateSdbPdp Update the Smart Dashboard with the Power Distribution Panel
     * currents.
     */
    public void updateSdbPdp() {
        double lf;
        double rf;
        double lb;
        double rb;
        final double fudgeFactor = 0.0;

        lf = pdp.getCurrent(Constants.PDP.DRIVE_LEFT_FRONT) - fudgeFactor;
        rf = pdp.getCurrent(Constants.PDP.DRIVE_LEFT_REAR) - fudgeFactor;
        lb = pdp.getCurrent(Constants.PDP.DRIVE_RIGHT_FRONT) - fudgeFactor;
        rb = pdp.getCurrent(Constants.PDP.DRIVE_RIGHT_REAR) - fudgeFactor;

        SmartDashboard.putNumber("Left Front I", lf);
        SmartDashboard.putNumber("Right Front I", rf);
        SmartDashboard.putNumber("Left Back I", lb);
        SmartDashboard.putNumber("Right Back I", rb);
    }

    /*
     * This method allows one to drive in "Tank Drive Mode". Tank drive mode uses
     * the left side of the joystick to control the left side of the robot, whereas
     * the right side of the joystick controls the right side of the robot.
     */
    public void tankDrive(double leftSideThrottle, double rightSideThrottle) {
        setMotorPower(leftSideThrottle, rightSideThrottle);
    }

    double m_lastThrottle;
    double m_lastSteering;

    public void speedRacerDrive(double throttle, double rawSteeringX, boolean quickTurn) {
        double leftPower;
        double rightPower;
        double rotation = 0;
        final double QUICK_TURN_GAIN = 0.55; // 2019: .75. 2020: .75 was too fast.

        m_lastThrottle = throttle;
        m_lastSteering = rawSteeringX;

        // check for if steering input is essentially zero for "DriveStraight"
        // functionality
        if ((-0.01 < rawSteeringX) && (rawSteeringX < 0.01)) {
            // no turn being commanded, drive straight or stay still
            m_iterationsSinceRotationCommanded++;

            if ((-0.01 < throttle) && (throttle < 0.01)) {
                // System.out.println("Drive: stay still");

                // no motion commanded, stay still
                m_iterationsSinceMovementCommanded++;
                rotation = 0.0;

                // if we are standing still and we are turned, assume the new direction is the
                // correct direction
                headingCorrection.lockHeading();
            } else {
                // driving straight
                if ((m_iterationsSinceRotationCommanded == LOOPS_GYRO_DELAY)
                        || (m_iterationsSinceMovementCommanded >= LOOPS_GYRO_DELAY)) {
                    // exactly LOOPS_GYRO_DELAY iterations with no commanded turn,
                    // or haven't had movement commanded for longer than LOOPS_GYRO_DELAY,
                    // so we want to take steps to preserve our current heading hereafter

                    // get current heading as desired heading
                    headingCorrection.lockHeading();
                    rotation = 0.0;

                    System.out.println("Drive: drive straight LOCK");
                } else if (m_iterationsSinceRotationCommanded < LOOPS_GYRO_DELAY) {
                    // not long enough since we were last turning,
                    // just drive straight without special heading maintenance
                    rotation = 0.0;
                    System.out.println("Drive: drive straight");
                } else if (m_iterationsSinceRotationCommanded > LOOPS_GYRO_DELAY) {
                    // after more then LOOPS_GYRO_DELAY iterations since commanded turn,
                    // maintain the target heading
                    rotation = headingCorrection.maintainHeading();
                    System.out.println("Drive: drive straight w/ correction");
                }
                m_iterationsSinceMovementCommanded = 0;
            }
            // driveStraight code benefits from "spin" behavior when needed
            leftPower = throttle + rotation;
            rightPower = throttle - rotation;
        } else {
            // commanding a turn, reset iterationsSinceRotationCommanded
            m_iterationsSinceRotationCommanded = 0;
            m_iterationsSinceMovementCommanded = 0;
            if (quickTurn) {

                int throttleSign;
                if (throttle >= 0.0) {
                    throttleSign = 1;
                } else {
                    throttleSign = -1;
                }

                // want a high-rate turn (also allows "spin" behavior)
                // power to each wheel is a combination of the throttle and rotation
                rotation = rawSteeringX * throttleSign * QUICK_TURN_GAIN;
                leftPower = throttle + rotation;
                rightPower = throttle - rotation;
            } else {
                // want a standard rate turn (scaled by the throttle)
                if (rawSteeringX >= 0.0) {
                    // turning to the right, derate the right power by turn amount
                    // note that rawSteeringX is positive in this portion of the "if"
                    leftPower = throttle;
                    rightPower = throttle * (1.0 - Math.abs(rawSteeringX));
                } else {
                    // turning to the left, derate the left power by turn amount
                    // note that rawSteeringX is negative in this portion of the "if"
                    leftPower = throttle * (1.0 - Math.abs(rawSteeringX));
                    rightPower = throttle;
                }
            }
        }
        setMotorPower(leftPower, rightPower);
    }

    public void rotateToHeading(final double desiredHeading) {
        headingCorrection.setDesiredHeading(desiredHeading);
    }

    // **********************************************DISPLAY****************************************************

    @Override
    public void periodic() {
        headingCorrection.periodic();
        updateSmartDashboard();
    }

    private void updateSmartDashboard() {
        headingCorrection.updateSmartDashboard();

        SmartDashboard.putBoolean("In Autonomous", DriverStation.isAutonomous());
        SmartDashboard.putNumber("Battery Voltage", RobotController.getBatteryVoltage());

        SmartDashboard.putNumber("Throttle", m_lastThrottle);
        SmartDashboard.putNumber("Steering", m_lastSteering);

        // ***** KBS: Uncommenting below, as it takes a LONG time to get PDP values
        // updateSdbPdp();

        int matchnumber = DriverStation.getMatchNumber();
        DriverStation.MatchType MatchType = DriverStation.getMatchType();
        SmartDashboard.putString("matchInfo", "" + MatchType + '_' + matchnumber);

        SmartDashboard.putNumber("Left Front Encoder Counts", leftFrontTalon.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Front Encoder Counts", rightFrontTalon.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Left Rear Encoder Counts", leftRearTalon.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Rear Encoder Counts", rightRearTalon.getSelectedSensorPosition(0));

        // Note: getSpeed() returns ticks per 0.1 seconds
        SmartDashboard.putNumber("Left Encoder Speed", leftFrontTalon.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Right Encoder Speed", rightFrontTalon.getSelectedSensorVelocity(0));

        // To convert ticks per 0.1 seconds into feet per second
        // a - multiply be 10 (tenths of second per second)
        // b - divide by 12 (1 foot per 12 inches)
        // c - multiply by distance (in inches) per pulse
        SmartDashboard.putNumber("Left Speed (fps)",
                leftFrontTalon.getSelectedSensorVelocity(0) * 10 / 12 * DISTANCE_PER_PULSE_IN_INCHES);
        SmartDashboard.putNumber("Right Speed (fps)",
                rightFrontTalon.getSelectedSensorVelocity(0) * 10 / 12 * DISTANCE_PER_PULSE_IN_INCHES);

        SmartDashboard.putNumber("Left Talon Output Voltage", leftFrontTalon.getMotorOutputVoltage());
        SmartDashboard.putNumber("Right Talon Output Voltage", rightFrontTalon.getMotorOutputVoltage());

        SmartDashboard.putNumber("LF Falcon Supply Current", leftFrontTalon.getSupplyCurrent());
        SmartDashboard.putNumber("LR Falcon Supply Current", leftRearTalon.getSupplyCurrent());
        SmartDashboard.putNumber("RF Falcon Supply Current", rightFrontTalon.getSupplyCurrent());
        SmartDashboard.putNumber("RR Falcon Supply Current", rightRearTalon.getSupplyCurrent());

        SmartDashboard.putBoolean("Closed Loop Mode", m_closedLoopMode);
        SmartDashboard.putBoolean("Speed Racer Drive Mode", m_speedRacerDriveMode);
    }

    private static final double CAMERA_LAG = 0.150; // was .200; changing to .150 at CMP

    public void updateHistory() {
        final double now = Timer.getFPGATimestamp();
        headingHistory.add(now, headingCorrection.getHeading());
    }

    public double getHeading() {
        return headingCorrection.getHeading();
    }

    public double getHeadingForCapturedImage() {
        final double now = Timer.getFPGATimestamp();
        final double indexTime = now - CAMERA_LAG;
        return headingHistory.getAzForTime(indexTime);
    }

    /**
     * Start a distance travel
     */
    public void saveInitialWheelDistance() {
        m_initialWheelDistance = (getLeftEncoder() + getRightEncoder()) / 2;
    }

    /**
     * Calculate the distance travelled. Return the second shortest distance. If a
     * wheel is floating, it will have a larger value - ignore it. If a wheel is
     * stuck, it will have a small value
     * 
     * @return
     */
    public double getWheelDistance() {
        final double dist = (getLeftEncoder() + getRightEncoder()) / 2;
        return dist - m_initialWheelDistance;
    }

    // NOTE the difference between rotateToHeading(...) and goToHeading(...)
    public void setDesiredHeading(final double desiredHeading) {
        headingCorrection.setDesiredHeading(desiredHeading);
        m_iterationsSinceRotationCommanded = LOOPS_GYRO_DELAY + 1;
        m_iterationsSinceMovementCommanded = 0;

        // reset the heading control loop for the new heading
        headingCorrection.resetAndEnableHeadingPID();
    }

    ////////////////////////////////////////////////////
    // PidTunerObject
    public double getP() {
        return leftFrontTalon.getP();
    }

    public double getI() {
        return leftFrontTalon.getI();
    }

    public double getD() {
        return leftFrontTalon.getD();
    }

    public double getF() {
        return leftFrontTalon.getF();

    }

    public void setP(double d) {
        leftFrontTalon.config_kP(0, d, 0);
        rightFrontTalon.config_kP(0, d, 0);
    }

    public void setI(double d) {
        leftFrontTalon.config_kI(0, d, 0);
        rightFrontTalon.config_kI(0, d, 0);
    }

    public void setD(double d) {
        leftFrontTalon.config_kD(0, d, 0);
        rightFrontTalon.config_kD(0, d, 0);
    }

    public void setF(double d) {
        leftFrontTalon.config_kF(0, d, 0);
        rightFrontTalon.config_kF(0, d, 0);
    }
}