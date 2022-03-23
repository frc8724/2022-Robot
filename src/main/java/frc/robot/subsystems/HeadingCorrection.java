package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HeadingCorrection {
    private final PIDController m_HeadingPid;
    private double m_correction;

    // Sensors
    private AHRS Navx;

    // NavX parameters
    // private double m_desiredHeading = 0.0;
    private boolean m_useHeadingCorrection = true;
    private static final double HEADING_PID_P = 0.007; // was 0.007 at GSD; was 0.030 in 2019 for HIGH_GEAR
    private static final double HEADING_PID_I = 0.000; // was 0.000 at GSD; was 0.000 in 2019
    private static final double HEADING_PID_D = 0.000; // was 0.080 at GSD; was 0.04 in 2019
    private static final double kToleranceDegreesPIDControl = 0.2;

    public HeadingCorrection() {
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
        m_HeadingPid = new PIDController(HEADING_PID_P, HEADING_PID_I, HEADING_PID_D, 0.020 /* period in seconds */);
        m_HeadingPid.enableContinuousInput(-180.0f, 180.0f);
        m_HeadingPid.setTolerance(kToleranceDegreesPIDControl);
    }

    /**
     * Called every 20ms, get the new correction
     */
    public void periodic() {
        m_correction = m_HeadingPid.calculate(getHeading());
    }

    private double m_headingOffset = 0.0;

    public void setHeadingOffset(final double arg_offset) {
        m_headingOffset = arg_offset;
    }

    public void zeroHeadingGyro(final double headingOffset) {
        Navx.zeroYaw();
        setHeadingOffset(headingOffset);

        DriverStation.reportError("heading immediately after zero = " + getHeading() + "\n", false);

        m_HeadingPid.setSetpoint(headingOffset);

        SmartDashboard.putString("Trace", "Zero Heading Gyro");

        // restart the PID controller loop
        resetAndEnableHeadingPID();
    }

    public boolean getHeadingCorrectionMode() {
        return m_useHeadingCorrection;
    }

    public void setHeadingCorrectionMode(final boolean useHeadingCorrection) {
        m_useHeadingCorrection = useHeadingCorrection;
    }

    public void resetAndEnableHeadingPID() {
        m_HeadingPid.reset();
    }

    // the NavX is installed upside-down, so it rotates backwards.
    public double getHeading() {
        return Navx.getYaw() + m_headingOffset;
    }

    // the Navx is installed sideways with reference to the front of the robot.
    public double getRoll() {
        return Navx.getPitch();
    }

    // the Navx is installed sideways with reference to the front of the robot.
    public double getPitch() {
        return Navx.getRoll();
    }

    public double getDesiredHeading() {
        return m_HeadingPid.getSetpoint();
    }

    public void setDesiredHeading(double d) {
        m_HeadingPid.setSetpoint(d);
    }

    public void lockHeading() {
        m_HeadingPid.setSetpoint(getHeading());
        resetAndEnableHeadingPID();
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
    public double maintainHeading() {
        return m_correction;
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Heading: desired", m_HeadingPid.getSetpoint());
        SmartDashboard.putNumber("Heading: actual", this.getHeading());
        SmartDashboard.putBoolean("Heading: correcting", m_useHeadingCorrection);
    }
}
