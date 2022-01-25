package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBaseSubsystem;

public class DriveBaseTeleopCommand extends CommandBase {
    private final DriveBaseSubsystem drive;
    private final Joystick driverStick;

    public DriveBaseTeleopCommand(DriveBaseSubsystem drive, Joystick driverStick) {
        this.drive = drive;
        this.driverStick = driverStick;

        addRequirements(drive);
    }

    @Override
    public void execute() {
        this.drive.speedRacerDrive(this.driverStick.getY(), this.driverStick.getX(), false);
    }
}
