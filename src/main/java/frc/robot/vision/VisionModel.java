package frc.robot.vision;

import org.opencv.core.Scalar;

public interface VisionModel {
    Scalar getLowerBound();

    Scalar getUpperBound();

    default Double minArea() {
        return null;
    }

    default Double maxArea() {
        return null;
    }

    default boolean isRatioCorrect(double ratio) {
        return true;
    }

    default boolean isPositionCorrect(double x, double y) {
        return true;
    }
}
