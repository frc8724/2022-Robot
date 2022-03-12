package frc.robot.vision.models;

import org.opencv.core.Scalar;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.vision.VisionModel;

public class BlueBallVisionModel implements VisionModel {
    @Override
    public Scalar getLowerBound() {
        // var h1 = SmartDashboard.getNumber("h1", 0);
        // var s1 = SmartDashboard.getNumber("s1", 0);
        // var v1 = SmartDashboard.getNumber("v1", 0);

        // return new Scalar(h1, s1, v1);

        return new Scalar(70, 68, 84);
    }

    @Override
    public Scalar getUpperBound() {
        // var h2 = SmartDashboard.getNumber("h2", 0);
        // var s2 = SmartDashboard.getNumber("s2", 0);
        // var v2 = SmartDashboard.getNumber("v2", 0);

        // return new Scalar(h2, s2, v2);

        return new Scalar(123, 161, 221);
    }

    @Override
    public Double minArea() {
        return 70.0;
    }

    @Override
    public boolean isPositionCorrect(double x, double y) {
        if (y < 0.5) {
            System.out.println("bad position is " + y);
            return false;
        } else {
            return true;
        }
    }
}
