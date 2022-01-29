package frc.robot.commands;

import org.mayheminc.util.MayhemDriverPad;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBaseSubsystem;

public class DriveBaseTeleopCommand extends CommandBase {
    private final DriveBaseSubsystem drive;
    private final MayhemDriverPad driverStick;

    public DriveBaseTeleopCommand(DriveBaseSubsystem drive, MayhemDriverPad driverStick) {
        this.drive = drive;
        this.driverStick = driverStick;

        addRequirements(drive);
    }

    @Override
    public void initialize() {
        this.drive.zeroHeadingGyro(0.0);
    }

    @Override
    public void execute() {

        double throttle = this.driverStick.driveThrottle();
        double steering = this.driverStick.steeringX();
        boolean quickTurn = this.driverStick.quickTurn();

        System.out.printf("DriveBaseTeleopCommand execute: %f, %f\n", throttle, steering);
        // System.out.println("DriveBaseTeleopCommand");

        this.drive.speedRacerDrive(throttle, steering, quickTurn);
    }
}
