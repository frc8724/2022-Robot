package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveBaseSubsystem extends SubsystemBase {
    private MotorController left;
    private MotorController right;

    private DifferentialDrive drive;

    public DriveBaseSubsystem(MotorController left, MotorController right) {
        this.left = left;
        this.right = right;

        this.drive = new DifferentialDrive(this.left, this.right);
    }

    public void arcadeDrive(double xSpeed, double zRotation) {
        this.drive.arcadeDrive(xSpeed, zRotation);
    }
}
