package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveBaseSubsystem extends SubsystemBase {
    private final DifferentialDrive drive;

    public DriveBaseSubsystem() {
        var leftFront = new WPI_TalonSRX(Constants.DRIVE_LEFT_FRONT);
        var leftRear = new WPI_TalonSRX(Constants.DRIVE_LEFT_REAR);
        var rightFront = new WPI_TalonSRX(Constants.DRIVE_RIGHT_FRONT);
        var rightRear = new WPI_TalonSRX(Constants.DRIVE_RIGHT_REAR);

        var left = new MotorControllerGroup(leftFront, leftRear);
        var right = new MotorControllerGroup(rightFront, rightRear);

        this.drive = new DifferentialDrive(left, right);
    }

    public void arcadeDrive(double xSpeed, double zRotation) {
        this.drive.arcadeDrive(xSpeed, zRotation);
    }
}
