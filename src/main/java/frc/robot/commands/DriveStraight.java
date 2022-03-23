package frc.robot.commands;

// import org.mayheminc.robot2019.Robot;
// import org.mayheminc.robot2019.subsystems.Drive;

// import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveBaseSubsystem;

/**
 *
 */
public class DriveStraight extends CommandBase {

	double m_targetPower;
	Double m_distance;

	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 */
	public DriveStraight(double arg_targetSpeed) {
		this(arg_targetSpeed, null);
	}

	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 * @param arg_distance    Distance in inches
	 */
	public DriveStraight(double arg_targetSpeed, Double arg_distance) {
		addRequirements(RobotContainer.drive);

		this.m_targetPower = arg_targetSpeed;
		if (arg_distance != null) {
			this.m_distance = arg_distance / DriveBaseSubsystem.DISTANCE_PER_PULSE_IN_INCHES;
		}
	}

	// Called just before this Command runs the first time
	@Override
	public void initialize() {
		RobotContainer.drive.saveInitialWheelDistance();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	public void execute() {
		RobotContainer.drive.speedRacerDrive(m_targetPower, 0, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		if (this.m_distance == null) {
			return false;
		}

		int actualDistance = Math.abs((int) RobotContainer.drive.getWheelDistance());

		return (actualDistance >= m_distance);
	}

	// Called once after isFinished returns true
	@Override
	public void end(boolean interrupted) {
		RobotContainer.drive.stop();
	}

}
