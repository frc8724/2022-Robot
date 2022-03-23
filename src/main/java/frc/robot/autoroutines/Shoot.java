package frc.robot.autoroutines;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.HoodMove;
import frc.robot.commands.SystemFireWhenReady;
import frc.robot.commands.WaitForNumBalls;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Shooter;

public class Shoot extends SequentialCommandGroup {
    public Shoot() {
        addCommands(new HoodMove(() -> Hood.CLOSE_POSITION));

        addCommands(new ParallelRaceGroup(new SystemFireWhenReady(() -> Shooter.CLOSE_SHOT), new WaitForNumBalls(10)));
    }
}
