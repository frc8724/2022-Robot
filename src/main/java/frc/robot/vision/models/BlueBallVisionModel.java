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
        // SmartDashboard.putNumber("h1", 70);
        // SmartDashboard.putNumber("h2", 123);
        // SmartDashboard.putNumber("s1", 68);
        // SmartDashboard.putNumber("s2", 161);
        // SmartDashboard.putNumber("v1", 84);
        // SmartDashboard.putNumber("v2", 221);
    }

    @Override
    public Scalar getLowerBound() {
        var h1 = SmartDashboard.getNumber("h1", 70);
        var s1 = SmartDashboard.getNumber("s1", 68);
        var v1 = SmartDashboard.getNumber("v1", 84);

        return new Scalar(h1, s1, v1);

        // return new Scalar(70, 68, 84);
    }

    @Override
    public Scalar getUpperBound() {
        var h2 = SmartDashboard.getNumber("h2", 123);
        var s2 = SmartDashboard.getNumber("s2", 161);
        var v2 = SmartDashboard.getNumber("v2", 221);

        return new Scalar(h2, s2, v2);

        // return new Scalar(123, 161, 221);
    }

    @Override
    public boolean isPositionCorrect(double x, double y) {
        if (y < 0.5) {
            // System.out.println("bad position is " + y);
            return false;
        } else {
            return true;
        }
    }
}
