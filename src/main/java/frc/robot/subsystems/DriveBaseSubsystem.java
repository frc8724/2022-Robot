package frc.robot.subsystems;

import com.kauailabs.navx.frc.*;

import frc.robot.Constants;
import frc.robot.utils.History;
import frc.robot.utils.MayhemTalonSRX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.*;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// TODO:  Address all deprecated code masked by @SuppressWarnings("removal") annotations (not just in Drive.java) 
@SuppressWarnings("removal")
public class DriveBaseSubsystem extends SubsystemBase /* implements PidTunerObject */ {
    History headingHistory = new History();

    // Brake modes
    public static final boolean BRAKE_MODE = true;
    public static final boolean COAST_MODE = false;

    // PID loop variables
    private final PIDController m_HeadingPid;
    // private final PIDHeadingError m_HeadingError;
    // private final PIDHeadingCorrection m_HeadingCorrection;
    private boolean m_HeadingPidPreventWindup = false;
    private static final int LOOPS_GYRO_DELAY = 10;

    // Talons
    private final MayhemTalonSRX leftFrontTalon = new MayhemTalonSRX(Constants.Talon.DRIVE_LEFT_FRONT);
    private final MayhemTalonSRX leftRearTalon = new MayhemTalonSRX(Constants.Talon.DRIVE_LEFT_REAR);
    private final MayhemTalonSRX rightFrontTalon = new MayhemTalonSRX(Constants.Talon.DRIVE_RIGHT_FRONT);
    private final MayhemTalonSRX rightRearTalon = new MayhemTalonSRX(Constants.Talon.DRIVE_RIGHT_REAR);

    // Sensors
    private AHRS Navx;

    // Driving mode
    private final boolean m_speedRacerDriveMode = true; // set by default

    // NavX parameters
    private double m_desiredHeading = 0.0;
    private boolean m_useHeadingCorrection = true;
    private static final double HEADING_PID_P = 0.010; // was 0.007 at GSD; was 0.030 in 2019 for HIGH_GEAR
    private static final double HEADING_PID_I = 0.000; // was 0.000 at GSD; was 0.000 in 2019
    private static final double HEADING_PID_D = 0.080; // was 0.080 at GSD; was 0.04 in 2019
    private static final double kToleranceDegreesPIDControl = 0.2;

    // Drive parameters
    // pi * diameter * (pulley ratios) / (counts per rev * gearbox reduction)
    public static final double DISTANCE_PER_PULSE_IN_INCHES = 3.14 * 5.75 * 36.0 / 42.0 / (2048.0 * 7.56); // corrected
                                                                                                           // for 2020

    private boolean m_closedLoopMode = true;
    private final double m_maxWheelSpeed = 18000.0; // should be maximum wheel speed in native units
    private static final double CLOSED_LOOP_RAMP_RATE = 0.1; // time from neutral to full in seconds

    private double m_initialWheelDistance = 0.0;
    private int m_iterationsSinceRotationCommanded = 0;
    private int m_iterationsSinceMovementCommanded = 0;

    private boolean autoAlign = false;

    // Targeting
    // private double TARGET_ALIGNED = -0.4;

    /***********************************
     * INITIALIZATION
     **********************************************************/

    public DriveBaseSubsystem() {
        try {
            /* Communicate w/navX MXP via the MXP SPI Bus. */
            /* Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB */
            /*
             * See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for
             * details.
             */
            Navx = new AHRS(SPI.Port.kMXP);
            Navx.reset();
        } catch (final RuntimeException ex) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
            System.out.println("Error loading navx.");
        }

        // create a PID Controller that reads the heading error and outputs the heading
        // correction.
        // m_HeadingError = new PIDHeadingError();
        // m_HeadingError.m_Error = 0.0;
        // m_HeadingCorrection = new PIDHeadingCorrection();
        m_HeadingPid = new PIDController(HEADING_PID_P, HEADING_PID_I, HEADING_PID_D, 0.020 /* period in seconds */);
        m_HeadingPid.enableContinuousInput(-180.0f, 180.0f);
        // m_HeadingPid.setOutputRange(-.50, .50); // set the maximum power to correct
        // twist
        m_HeadingPid.setTolerance(kToleranceDegreesPIDControl);

        // confirm all four drive talons are in coast mode
        leftFrontTalon.setNeutralMode(NeutralMode.Coast);
        leftRearTalon.setNeutralMode(NeutralMode.Coast);
        rightFrontTalon.setNeutralMode(NeutralMode.Coast);
        rightRearTalon.setNeutralMode(NeutralMode.Coast);

        this.configTalon(leftFrontTalon);
        this.configTalon(leftRearTalon);
        this.configTalon(rightFrontTalon);
        this.configTalon(rightRearTalon);

        // configure output voltages
        leftFrontTalon.configNominalOutput(0, 0);
        leftRearTalon.configNominalOutput(0, 0);
        rightFrontTalon.configNominalOutput(0, 0);
        rightRearTalon.configNominalOutput(0, 0);

        leftFrontTalon.configPeakOutputVoltage(1, -1);
        leftRearTalon.configPeakOutputVoltage(1, -1);
        rightFrontTalon.configPeakOutputVoltage(1, -1);
        rightRearTalon.configPeakOutputVoltage(1, -1);

        // configure current limits
        // enabled = true
        // 40 = limit (amps)
        // 60 = trigger_threshold (amps)
        // 0.5 = threshold time(s)
        leftFrontTalon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 40, 60, 0.5));
        leftRearTalon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 40, 60, 0.5));
        rightFrontTalon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 40, 60, 0.5));
        rightRearTalon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 40, 60, 0.5));

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
    }

    private void configTalon(TalonSRX talon) {
        talon.configPeakCurrentLimit(60);
        talon.configContinuousCurrentLimit(40);
        talon.configPeakCurrentDuration(3000);
    }

    public void init() {
        // reset the NavX
        zeroHeadingGyro(0.0);

        // talon closed loop config
        configureDriveTalon(leftFrontTalon);
        configureDriveTalon(rightFrontTalon);
    }

    public void zeroHeadingGyro(final double headingOffset) {
        Navx.zeroYaw();
        setHeadingOffset(headingOffset);

        DriverStation.reportError("heading immediately after zero = " + getHeading() + "\n", false);

        m_desiredHeading = 0.0;

        SmartDashboard.putString("Trace", "Zero Heading Gyro");

        // restart the PID controller loop
        resetAndEnableHeadingPID();
    }

    public void initDefaultCommand() {
        // setDefaultCommand(new SpeedRacerDrive());
    }

    private void configureDriveTalon(final MayhemTalonSRX talon) {
        final double wheelP = 0.020;
        final double wheelI = 0.000;
        final double wheelD = 0.200;
        final double wheelF = 0.060;

        talon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);

        talon.configNominalOutput(0, 0);
        talon.configPeakOutputVoltage(1, -1);

        talon.config_kP(0, wheelP, 0);
        talon.config_kI(0, wheelI, 0);
        talon.config_kD(0, wheelD, 0);
        talon.config_kF(0, wheelF, 0);
        talon.configClosedloopRamp(CLOSED_LOOP_RAMP_RATE); // specify minimum time for neutral to full in seconds

        DriverStation.reportError("setWheelPIDF: " + wheelP + " " + wheelI + " " + wheelD + " " + wheelF + "\n", false);
    }

    /**
     * Set the "Brake Mode" behavior on the drive talons when "in neutral"
     * 
     * @param brakeMode - true for "brake in neutral" and false for "coast in
     *                  neutral"
     */
    public void setBrakeMode(final boolean brakeMode) {

        final NeutralMode mode = (brakeMode) ? NeutralMode.Brake : NeutralMode.Coast;

        leftFrontTalon.setNeutralMode(mode);
        leftRearTalon.setNeutralMode(mode);
        rightFrontTalon.setNeutralMode(mode);
        rightRearTalon.setNeutralMode(mode);
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

    public double getRightEncoder() {
        return rightFrontTalon.getSelectedSensorPosition(0);
    }

    public double getLeftEncoder() {
        return leftFrontTalon.getSelectedSensorPosition(0);
    }

    // speed is in inches per second
    public double getRightSpeed() {
        return rightFrontTalon.getSelectedSensorVelocity(0);
    }

    public double getLeftSpeed() {
        return leftFrontTalon.getSelectedSensorVelocity(0);
    }

    // *************************** GYRO *******************************************

    public double calculateHeadingError(final double Target) {
        final double currentHeading = getHeading();
        double error = Target - currentHeading;
        error = error % 360.0;
        if (error > 180.0) {
            error -= 360.0;
        }
        return error;
    }

    public boolean getHeadingCorrectionMode() {
        return m_useHeadingCorrection;
    }

    public void setHeadingCorrectionMode(final boolean useHeadingCorrection) {
        m_useHeadingCorrection = useHeadingCorrection;
    }

    private void resetAndEnableHeadingPID() {
        m_HeadingPid.reset();
        // m_HeadingPid.enable();
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

    private int LoopCounter = 0;

    public void displayGyroInfo() {
        // SmartDashboard.putNumber("Robot Heading",
        // Utils.twoDecimalPlaces(getHeading()));
        // SmartDashboard.putNumber("Robot Roll",
        // Utils.twoDecimalPlaces(this.getRoll()));
        // SmartDashboard.putNumber("Robot Pitch",
        // Utils.twoDecimalPlaces(this.getPitch()));
        // SmartDashboard.putNumber("Loop Counter", LoopCounter++);
    }

    private double m_headingOffset = 0.0;

    public void setHeadingOffset(final double arg_offset) {
        m_headingOffset = arg_offset;
    }

    public double getHeading() {
        return Navx.getYaw() + m_headingOffset;
    }

    // the Navx is installed sidways with reference to the front of the robot.
    public double getRoll() {
        return Navx.getPitch();
    }

    // the Navx is installed sidways with reference to the front of the robot.
    public double getPitch() {
        return Navx.getRoll();
    }

    public double getDesiredHeading() {
        return m_desiredHeading;
    }

    // ****************** SETTING POWER TO MOTORS ********************

    public void resetNavXDisplacement() {
        Navx.resetDisplacement();
    }

    public void stop() {
        setMotorPower(0, 0);
    }

    private void setMotorPower(double leftPower, double rightPower) {
        if (rightPower > 1.0) {
            rightPower = 1.0;
        }
        if (rightPower < -1.0) {
            rightPower = -1.0;
        }

        if (leftPower > 1.0) {
            leftPower = 1.0;
        }
        if (leftPower < -1.0) {
            leftPower = -1.0;
        }

        if (m_closedLoopMode) {
            rightFrontTalon.set(ControlMode.Velocity, rightPower * m_maxWheelSpeed);
            leftFrontTalon.set(ControlMode.Velocity, leftPower * m_maxWheelSpeed);
        } else {
            rightFrontTalon.set(ControlMode.PercentOutput, rightPower);
            leftFrontTalon.set(ControlMode.PercentOutput, leftPower);
        }
    }

    PowerDistribution pdp = new PowerDistribution();

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
        // if (leftSideThrottle >= 0.0) {
        // leftSideThrottle = (leftSideThrottle * leftSideThrottle); // squaring inputs
        // increases fine control
        // } else {
        // leftSideThrottle = -(leftSideThrottle * leftSideThrottle); // preserves the
        // sign while squaring negative
        // // values
        // }

        // if (rightSideThrottle >= 0.0) {
        // rightSideThrottle = (rightSideThrottle * rightSideThrottle);
        // } else {
        // rightSideThrottle = -(rightSideThrottle * rightSideThrottle);
        // }

        setMotorPower(leftSideThrottle, rightSideThrottle);
    }

    public void setAutoAlignTrue() {
        autoAlign = true;
        // reset the PID controller loop for steering now that we are auto-aligning
        resetAndEnableHeadingPID();
        // Robot.lights.set(LedPatternFactory.autoAlignTrying);
    }

    public void setAutoAlignFalse() {
        autoAlign = false;
        resetAndEnableHeadingPID();
        // Robot.lights.set(LedPatternFactory.autoAlignGotIt);
    }

    public void speedRacerDrive(double throttle, double rawSteeringX, boolean quickTurn) {
        double leftPower, rightPower;
        double rotation = 0;
        final double QUICK_TURN_GAIN = 0.55; // 2019: .75. 2020: .75 was too fast.

        int throttleSign;
        if (throttle >= 0.0) {
            throttleSign = 1;
        } else {
            throttleSign = -1;
        }

        // check for if steering input is essentially zero for "DriveStraight"
        // functionality
        if ((-0.01 < rawSteeringX) && (rawSteeringX < 0.01)) {
            // no turn being commanded, drive straight or stay still
            m_iterationsSinceRotationCommanded++;
            if ((-0.01 < throttle) && (throttle < 0.01)) {
                // no motion commanded, stay still
                m_iterationsSinceMovementCommanded++;
                rotation = 0.0;
                m_desiredHeading = getHeading(); // whenever motionless, set desired heading to current heading
                // reset the PID controller loop now that we have a new desired heading
                resetAndEnableHeadingPID();
            } else {
                // driving straight
                if ((m_iterationsSinceRotationCommanded == LOOPS_GYRO_DELAY)
                        || (m_iterationsSinceMovementCommanded >= LOOPS_GYRO_DELAY)) {
                    // exactly LOOPS_GYRO_DELAY iterations with no commanded turn,
                    // or haven't had movement commanded for longer than LOOPS_GYRO_DELAY,
                    // so we want to take steps to preserve our current heading hereafter

                    // get current heading as desired heading
                    m_desiredHeading = getHeading();

                    // reset the PID controller loop now that we have a new desired heading
                    resetAndEnableHeadingPID();
                    rotation = 0.0;
                } else if (m_iterationsSinceRotationCommanded < LOOPS_GYRO_DELAY) {
                    // not long enough since we were last turning,
                    // just drive straight without special heading maintenance
                    rotation = 0.0;
                } else if (m_iterationsSinceRotationCommanded > LOOPS_GYRO_DELAY) {
                    // after more then LOOPS_GYRO_DELAY iterations since commanded turn,
                    // maintain the target heading
                    rotation = maintainHeading();
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

    public int stage = 0;
    public final double DELAY = 4;
    public double timerStart = Timer.getFPGATimestamp();

    public boolean selfTest() {
        return false;
    }

    /**
     * The headings are from -180 to 180. The CurrentHeading is the current robot
     * orientation. The TargetHeading is where we want the robot to face.
     * 
     * e.g. Current = 0, Target = 10, error = 10 Current = 180, Target = -170, error
     * = 10 (we need to turn 10 deg Clockwise to get to -170) Current = -90, Target
     * = 180, error = -90 (we need to turn 90 deg Counter-Clockwise to get to 180)
     * Current = 10, target = -10, error = -20 (we need to turn Counterclockwise -20
     * deg)
     * 
     * @param CurrentHeading
     * @param TargetHeading
     * @return
     */
    private double maintainHeading() {
        double headingCorrection = 0.0;

        // below line is essential to let the Heading PID know the current heading error
        var headingError = m_desiredHeading - getHeading();

        if (m_useHeadingCorrection) {
            headingCorrection = -m_HeadingPid.calculate(headingError);
        } else {
            headingCorrection = 0.0;
        }

        // check for major heading changes and take action to prevent
        // integral windup if there is a major heading error
        // TODO: In 2020, this code was causing "wandering" with non-zero HEADING_PID_I.
        // Worked around issue by setting HEADING_PID_I = 0
        if (Math.abs(headingError) > 10.0) {
            if (!m_HeadingPidPreventWindup) {
                m_HeadingPid.setI(0.0);
                resetAndEnableHeadingPID();
                m_HeadingPidPreventWindup = true;
            }
        } else {
            m_HeadingPidPreventWindup = false;
            m_HeadingPid.setI(HEADING_PID_I);
        }

        return headingCorrection;
    }

    public void rotate(final double RotateX) {
        m_desiredHeading += RotateX;
        if (m_desiredHeading > 180) {
            m_desiredHeading -= 360;
        }
        if (m_desiredHeading < -180) {
            m_desiredHeading += 360;
        }
        m_iterationsSinceRotationCommanded = LOOPS_GYRO_DELAY + 1; // hack so speedracerdrive maintains heading
        m_iterationsSinceMovementCommanded = 0;
    }

    public void rotateToHeading(final double desiredHeading) {
        m_desiredHeading = desiredHeading;
    }

    // **********************************************DISPLAY****************************************************

    @Override
    public void periodic() {
        updateSmartDashboard();
    }

    public void updateSmartDashboard() {
        displayGyroInfo();

        SmartDashboard.putBoolean("In Autonomous", DriverStation.getInstance().isAutonomous());
        SmartDashboard.putNumber("Battery Voltage", RobotController.getBatteryVoltage());

        // ***** KBS: Uncommenting below, as it takes a LONG time to get PDP values
        // updateSdbPdp();

        int matchnumber = DriverStation.getInstance().getMatchNumber();
        DriverStation.MatchType MatchType = DriverStation.getInstance().getMatchType();
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

        SmartDashboard.putBoolean("Heading Correction Mode", m_useHeadingCorrection);
        SmartDashboard.putNumber("Heading Desired", m_desiredHeading);
        // SmartDashboard.putNumber("Heading Error", m_HeadingError.m_Error);
        // SmartDashboard.putNumber("Heading Correction",
        // -m_HeadingCorrection.HeadingCorrection);

        // SmartDashboard.putNumber("Joystick Drive Throttle",
        // Robot.oi.driveThrottle());
        // SmartDashboard.putNumber("Joystick SteeringX", Robot.oi.steeringX());

        // determine currentSpeed and display it
        // double currentSpeed = getLeftSpeed() > getRightSpeed() ? getLeftSpeed() :
        // getRightSpeed();

        // display current speed to driver
        // SmartDashboard.putNumber("Current Speed", currentSpeed);
    }

    private static final double CAMERA_LAG = 0.150; // was .200; changing to .150 at CMP

    public void updateHistory() {
        final double now = Timer.getFPGATimestamp();
        headingHistory.add(now, getHeading());
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
        m_desiredHeading = desiredHeading;
        m_iterationsSinceRotationCommanded = LOOPS_GYRO_DELAY + 1;
        m_iterationsSinceMovementCommanded = 0;

        // reset the heading control loop for the new heading
        resetAndEnableHeadingPID();
    }

    ////////////////////////////////////////////////////
    // PidTunerObject
    public double getP() {
        return leftFrontTalon.kP;
    }

    public double getI() {
        return leftFrontTalon.kI;
    }

    public double getD() {
        return leftFrontTalon.kD;
    }

    public double getF() {
        return leftFrontTalon.kF;

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
