package frc.robot.commands;

// import org.mayheminc.robot2019.Robot;
// import org.mayheminc.robot2019.subsystems.Drive;

// import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

/**
 *
 */
public class DriveStraight extends CommandBase {

	double m_targetPower;

	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 * @param arg_distance    Distance in encoder counts
	 */
	public DriveStraight(double arg_targetSpeed) {
		addRequirements(RobotContainer.drive);

		m_targetPower = arg_targetSpeed;
	}

	// Called just before this Command runs the first time
	@Override
	public void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	public void execute() {
		RobotContainer.drive.speedRacerDrive(m_targetPower, 0, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		return (false);
	}

	// Called once after isFinished returns true
	@Override
	public void end(boolean interrupted) {
		RobotContainer.drive.stop();
	}

}
