package frc.robot.vision.models;

import org.opencv.core.Scalar;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.vision.ContourCollection;
import frc.robot.vision.VisionModel;
import frc.robot.vision.VisionObject;

public class TargetVisionModel implements VisionModel {
    public TargetVisionModel() {
        SmartDashboard.putNumber("h1", 67);
        SmartDashboard.putNumber("h2", 118);
        SmartDashboard.putNumber("s1", 172);
        SmartDashboard.putNumber("s2", 255);
        SmartDashboard.putNumber("v1", 56);
        SmartDashboard.putNumber("v2", 230);
    }

    @Override
    public Scalar getLowerBound() {
        var h1 = SmartDashboard.getNumber("h1", 0);
        var s1 = SmartDashboard.getNumber("s1", 0);
        var v1 = SmartDashboard.getNumber("v1", 0);

        return new Scalar(h1, s1, v1);
        // return new Scalar(37.05, 43.59, 74.10);
    }

    @Override
    public Scalar getUpperBound() {
        var h2 = SmartDashboard.getNumber("h2", 0);
        var s2 = SmartDashboard.getNumber("s2", 0);
        var v2 = SmartDashboard.getNumber("v2", 0);

        return new Scalar(h2, s2, v2);
        // return new Scalar(55.58, 177.63, 255);
    }

    @Override
    public VisionObject getBestObject(ContourCollection contours) {
        return contours
                .centermost();
    }
}
