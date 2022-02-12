package frc.robot.vision;

import org.opencv.core.Scalar;

public interface VisionModel {
    public Scalar getLowerBound();

    public Scalar getUpperBound();
}
