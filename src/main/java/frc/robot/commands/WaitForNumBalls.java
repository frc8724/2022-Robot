package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class WaitForNumBalls extends CommandBase {
    final int numBallsDesired;
    int ballsShot = 0;

    int atSpeedCounter = 0;
    int notAtSpeedCounter = 0;

    boolean wasShooterAtSpeed = false;

    public WaitForNumBalls(int numBallsDesired) {
        this.numBallsDesired = numBallsDesired;

        this.updateDashboard();
    }

    @Override
    public void initialize() {
        this.wasShooterAtSpeed = false;

        this.ballsShot = 0;

        this.atSpeedCounter = 0;
        this.notAtSpeedCounter = 0;

        this.updateDashboard();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return this.ballsShot >= this.numBallsDesired;
    }

    private void updateDashboard() {
        SmartDashboard.putNumber("Balls shot", this.ballsShot);
    }
}
