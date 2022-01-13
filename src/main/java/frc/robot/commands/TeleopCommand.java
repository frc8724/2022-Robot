package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBaseSubsystem;

public class TeleopCommand extends CommandBase {
    final DriveBaseSubsystem drive;
    final Joystick driverStick;

    public TeleopCommand(DriveBaseSubsystem drive, Joystick driverStick) {
        this.drive = drive;
        this.driverStick = driverStick;

        addRequirements(drive);
    }

    @Override
    public void execute() {
        this.drive.arcadeDrive(this.driverStick.getY(), this.driverStick.getX());
    }
}
