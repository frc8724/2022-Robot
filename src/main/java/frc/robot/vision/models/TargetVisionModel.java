package frc.robot.vision.models;

import org.opencv.core.Scalar;

import frc.robot.vision.VisionModel;

public class TargetVisionModel implements VisionModel {
    @Override
    public Scalar getLowerBound() {
        // var h1 = SmartDashboard.getNumber("h1", 0);
        // var s1 = SmartDashboard.getNumber("s1", 0);
        // var v1 = SmartDashboard.getNumber("v1", 0);

        return new Scalar(37.05, 43.59, 74.10);
    }

    @Override
    public Scalar getUpperBound() {
        // var h2 = SmartDashboard.getNumber("h2", 0);
        // var s2 = SmartDashboard.getNumber("s2", 0);
        // var v2 = SmartDashboard.getNumber("v2", 0);

        return new Scalar(55.58, 177.63, 255);
    }
}
