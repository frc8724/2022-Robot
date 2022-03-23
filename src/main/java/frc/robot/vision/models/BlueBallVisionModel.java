package frc.robot.vision.models;

import org.opencv.core.Scalar;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.vision.VisionModel;

public class BlueBallVisionModel implements VisionModel {

    double h1;
    double h2;
    double s1;
    double s2;
    double v1;
    double v2;

    public BlueBallVisionModel() {
        SmartDashboard.putNumber("h1", 90);
        SmartDashboard.putNumber("h2", 118);
        SmartDashboard.putNumber("s1", 155);
        SmartDashboard.putNumber("s2", 255);
        SmartDashboard.putNumber("v1", 88);
        SmartDashboard.putNumber("v2", 221);
    }

    @Override
    public Scalar getLowerBound() {
        var h1 = SmartDashboard.getNumber("h1", 0);
        var s1 = SmartDashboard.getNumber("s1", 0);
        var v1 = SmartDashboard.getNumber("v1", 0);

        return new Scalar(h1, s1, v1);

        // return new Scalar(70, 68, 84);
    }

    @Override
    public Scalar getUpperBound() {
        var h2 = SmartDashboard.getNumber("h2", 0);
        var s2 = SmartDashboard.getNumber("s2", 0);
        var v2 = SmartDashboard.getNumber("v2", 0);

        return new Scalar(h2, s2, v2);

        // return new Scalar(123, 161, 221);
    }

    @Override
    public boolean isPositionCorrect(double x, double y) {
        if (y < 0.3) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isRatioCorrect(double ratio) {
        if (ratio > 1.5)
            return false;

        if (ratio < 0.75)
            return false;

        return true;
    }

    @Override
    public Double minArea() {
        return 800.0;
    }
}
