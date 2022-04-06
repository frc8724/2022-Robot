package frc.robot.vision;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class VisionObject {
    public MatOfPoint contour;
    public Rect rect;
    private Point center;

    private double imageWidth;
    private double imageHeight;

    public VisionObject(MatOfPoint contour, double imageWidth, double imageHeight) {
        this.contour = contour;
        this.rect = Imgproc.boundingRect(contour);

        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public Point getCenter() {
        if (this.center == null) {
            this.center = getCenterOfRect(this.rect);
        }

        return this.center;
    }

    public double ratio() {
        return this.rect.width / this.rect.height;
    }

    public double area() {
        return this.rect.area();
    }

    private Point getCenterOfRect(Rect rect) {
        double x = rect.x + (rect.width / 2);
        double y = rect.y + (rect.height / 2);

        return new Point(x / this.imageWidth, y / this.imageHeight);
    }
}
