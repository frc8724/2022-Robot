package frc.robot.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
    class Thing {
        public Point center;
        public Rect boundingRect;
        public int area;

        public Thing(Point center, Rect boundingRect) {
            this.center = center;
            this.boundingRect = boundingRect;
            this.area = boundingRect.width * boundingRect.height;
        }
    }

    private Integer cameraIndex;

    public boolean isRunning = false;

    public VisionModel model;

    private List<Thing> things = new ArrayList<>();

    public Vision(int cameraIndex) {
        this.cameraIndex = cameraIndex;
    }

    public void init() {
        new Thread(() -> {
            UsbCamera camera = CameraServer.startAutomaticCapture(this.cameraIndex);

            camera.setResolution(320, 240);
            camera.setFPS(10);

            CvSink cvSink = CameraServer.getVideo(camera);
            CvSource outputStream = CameraServer.putVideo("Output " + this.cameraIndex, 640, 480);

            Mat source = new Mat();

            while (!Thread.interrupted()) {
                if (!this.isRunning || this.model == null) {
                    // Why should we process vision if nobody needs it?
                    try {
                        // zzz
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        // idk maybe handle this exception gracefully
                        // (sounds like a problem for someone else)
                    }

                    continue;
                }

                if (cvSink.grabFrame(source) == 0) {
                    continue;
                }

                Mat output = processImage(source);

                outputStream.putFrame(output);
            }
        }).start();
    }

    private Mat processImage(Mat source) {
        // blur
        Imgproc.medianBlur(source, source, 7);
        // BGR to HSV
        Mat hsv = new Mat();
        Imgproc.cvtColor(source, hsv, Imgproc.COLOR_BGR2HSV);
        // In Range
        Mat inRange = new Mat();
        Core.inRange(hsv, this.model.getLowerBound(), this.model.getUpperBound(), inRange);
        // Dilate to fill in holes.
        var kernel = new Mat();
        Imgproc.dilate(inRange, inRange, kernel);
        // Erode back down to normal.
        Imgproc.erode(inRange, inRange, kernel);

        // find contours
        var contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(inRange, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        System.out.println("Hey look, we have " + contours.size() + " contours");
        // loop through contours
        for (MatOfPoint contour : contours) {
            var rect = Imgproc.boundingRect(contour);

            var area = rect.area();
            var minArea = this.model.minArea();
            var maxArea = this.model.maxArea();

            if (minArea != null && area < minArea) {
                // System.out.println("invalid min area. area is " + area + " and min area is "
                // + minArea);
                // continue;
            }

            if (maxArea != null && area > maxArea) {
                System.out.println("invalid max area");
                continue;
            }

            if (!this.model.isRatioCorrect(rect.width / rect.height)) {
                System.out.println("invalid ratio");
                continue;
            }

            if (!this.model.isPositionCorrect((double) rect.x / (double) source.width(), (double) rect.y /
                    (double) source.height())) {
                System.out.println("invalid position");
                continue;
            }

            Imgproc.rectangle(source, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
        }

        // get bounding rect of contour
        // if quality of rect is good (aspect ratio, area, etc.)
        // Draw rect on image
        // draw crosshairs on image
        // Imgproc.line(source, new Point(source.width() / 2, 0), new
        // Point(source.width() / 2, source.height()),
        // new Scalar(255, 0, 0), 3);
        // Imgproc.line(source, new Point(0, source.height() / 2), new
        // Point(source.width(), source.height() / 2),
        // new Scalar(255, 0, 0), 3);
        // return image

        if (SmartDashboard.getBoolean("Vision Debug", false)) {
            return inRange;
        } else {
            return source;
        }
    }

    public void start(VisionModel model) {
        this.model = model;
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    public List<Thing> getThings() {
        return this.things;
    }

    /**
     * Return 0.0 to 1.0 to give proportion of vision width to target.
     * 0.0 is far left edge.
     * 0.5 is center
     * 1.0 is far right edge
     * -1.0 for no target
     * 
     * @return
     */
    public double getXLocationOfTarget() {
        return 0.6; // a little off center
    }

    public Thing getLargestThing() {
        Thing largestThing = null;

        for (Thing thing : things) {
            if (largestThing != null && thing.area > largestThing.area) {
                largestThing = thing;
            }
        }

        return largestThing;
    }

    public void setModel(VisionModel model) {
        this.model = model;
    }

    private static Point getCenterOfRect(Rect rect) {
        return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
    }
}
