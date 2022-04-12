package frc.robot.vision;

import org.opencv.core.Scalar;

public interface VisionModel {
    Scalar getLowerBound();

    Scalar getUpperBound();

    VisionObject getBestObject(ContourCollection contours);

    default Integer cameraExposure() {
        return null;
    }
}
