package frc.robot.vision.models;

import org.opencv.core.Scalar;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.vision.VisionModel;

// TODO: fill with real values
public class TargetVisionModel implements VisionModel {
    @Override
    public Scalar getLowerBound() {
        var r1 = SmartDashboard.getNumber("r1", 0);
        var g1 = SmartDashboard.getNumber("g1", 0);
        var b1 = SmartDashboard.getNumber("b1", 0);

        return new Scalar(r1, g1, b1);
    }

    @Override
    public Scalar getUpperBound() {
        var r2 = SmartDashboard.getNumber("r2", 0);
        var g2 = SmartDashboard.getNumber("g2", 0);
        var b2 = SmartDashboard.getNumber("b2", 0);

        return new Scalar(r2, g2, b2);
    }
}
