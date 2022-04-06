package frc.robot.vision;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.opencv.core.MatOfPoint;

/**
 * An easily filterable list of contours (`VisionObject`s, actually)
 */
public class ContourCollection extends ArrayList<VisionObject> {
    public static ContourCollection fromContours(List<MatOfPoint> contours, double imageWidth,
            double imageHeight) {
        ContourCollection contourCollection = new ContourCollection();

        for (MatOfPoint contour : contours) {
            contourCollection.add(new VisionObject(contour, imageWidth, imageHeight));
        }

        return contourCollection;
    }

    public ContourCollection filter(Function<VisionObject, Boolean> predicate) {
        super.removeIf(x -> !predicate.apply(x));
        return this;
    }

    public ContourCollection minArea(double area) {
        this.filter(visionObject -> visionObject.rect.area() > area);
        return this;
    }

    public ContourCollection maxArea(double area) {
        this.filter(visionObject -> visionObject.rect.area() < area);
        return this;
    }

    public ContourCollection minRatio(double ratio) {
        this.filter(visionObject -> visionObject.ratio() > ratio);
        return this;
    }

    public ContourCollection maxRatio(double ratio) {
        this.filter(visionObject -> visionObject.ratio() < ratio);
        return this;
    }

    /**
     * Filters contours based on their vertical position. `0.0` is the top of the
     * image, and `1.0` is the bottom.
     */
    public ContourCollection above(double position) {
        this.filter(visionObject -> visionObject.getCenter().y < position);
        return this;
    }

    /**
     * Filters contours based on their vertical position. `0.0` is the top of the
     * image, and `1.0` is the bottom.
     */
    public ContourCollection below(double position) {
        this.filter(visionObject -> visionObject.getCenter().y > position);
        return this;
    }

    public ContourCollection inUpperHalf() {
        this.above(0.5);
        return this;
    }

    public ContourCollection inLowerHalf() {
        this.below(0.5);
        return this;
    }

    public VisionObject largest() {
        VisionObject largestObject = null;

        for (VisionObject object : this) {
            if (largestObject == null || object.rect.area() > largestObject.rect.area()) {
                largestObject = object;
            }
        }

        return largestObject;
    }

    public VisionObject closestToCenter() {
        VisionObject centermostObject = null;

        // 0.6 is just a bit more than the maximum possible value of
        // `centermostObjectDistance`
        double centermostObjectDistance = 0.6;

        for (VisionObject object : this) {
            var objectDistance = Math.abs(object.getCenter().x - 0.5);

            if (centermostObject == null || objectDistance < centermostObjectDistance) {
                centermostObject = object;
                centermostObjectDistance = objectDistance;
            }
        }

        return centermostObject;
    }

    public VisionObject centermost() {
        var copy = new ArrayList<>(this);

        copy.sort((a, b) -> {
            if (a.getCenter().x < b.getCenter().x) {
                return -1;
            } else if (a.getCenter().x > b.getCenter().x) {
                return 1;
            }

            return 0;
        });

        if (copy.size() == 0)
            return null;

        return copy.get(copy.size() / 2);
    }
}
