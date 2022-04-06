package frc.robot.vision.models;

import org.opencv.core.Scalar;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.vision.ContourCollection;
import frc.robot.vision.VisionModel;
import frc.robot.vision.VisionObject;

public class RedBallVisionModel implements VisionModel {
    double h1;
    double h2;
    double s1;
    double s2;
    double v1;
    double v2;

    public RedBallVisionModel() {
        SmartDashboard.putNumber("red ball - h1", 70);
        SmartDashboard.putNumber("red ball - h2", 123);
        SmartDashboard.putNumber("red ball - s1", 68);
        SmartDashboard.putNumber("red ball - s2", 161);
        SmartDashboard.putNumber("red ball - v1", 84);
        SmartDashboard.putNumber("red ball - v2", 221);
    }

    @Override
    public Scalar getLowerBound() {
        var h1 = SmartDashboard.getNumber("red ball - h1", 70);
        var s1 = SmartDashboard.getNumber("red ball - s1", 68);
        var v1 = SmartDashboard.getNumber("red ball - v1", 84);

        return new Scalar(h1, s1, v1);
    }

    @Override
    public Scalar getUpperBound() {
        var h2 = SmartDashboard.getNumber("red ball - h2", 123);
        var s2 = SmartDashboard.getNumber("red ball - s2", 161);
        var v2 = SmartDashboard.getNumber("red ball - v2", 221);

        return new Scalar(h2, s2, v2);
    }

    @Override
    public VisionObject getBestObject(ContourCollection contours) {
        return contours
                .minArea(800.0)
                .minRatio(0.75).maxRatio(1.5)
                .below(0.3)
                .largest();
    }
}
