package frc.robot.commands;

import org.mayheminc.util.MayhemDriverPad;
import org.mayheminc.util.MayhemOperatorPad;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveBaseSubsystem;

public class DriveBaseTeleopCommand extends CommandBase {
    private final DriveBaseSubsystem drive;
    private final Climber climber;
    private final MayhemDriverPad driverStick;
    private final MayhemOperatorPad operatorPad;

    public DriveBaseTeleopCommand(DriveBaseSubsystem drive, Climber climber, MayhemDriverPad driverStick,
            MayhemOperatorPad operatorPad) {
        this.driverStick = driverStick;
        this.operatorPad = operatorPad;

        this.drive = drive;
        this.climber = climber;

        // addRequirements(drive);
        // addRequirements(climber);
    }

    @Override
    public void initialize() {
        this.drive.init();
    }

    @Override
    public void execute() {
        double throttle = this.driverStick.driveThrottle();
        double steering = this.driverStick.steeringX();
        boolean quickTurn = this.driverStick.quickTurn();

        this.drive.speedRacerDrive(throttle, steering, quickTurn);

        double d = -operatorPad.OPERATOR_PAD.getY();
        this.climber.setArmLengthPowerTo(d);
    }
}
