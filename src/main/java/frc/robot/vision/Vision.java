package frc.robot.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;

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

    public Vision() {
    }

    public Vision(int cameraIndex) {
        this.cameraIndex = cameraIndex;
    }

    public void init() {
        new Thread(() -> {
            UsbCamera camera;

            if (this.cameraIndex != null) {
                camera = CameraServer.startAutomaticCapture(this.cameraIndex);
            } else {
                camera = CameraServer.startAutomaticCapture();
            }

            camera.setResolution(640, 480);
            camera.setFPS(10);

            CvSink cvSink = CameraServer.getVideo();
            CvSource outputStream = CameraServer.putVideo("Output", 640, 480);

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

                // Mat output = new Mat();
                Mat output = ProcessImage(source);

                Core.inRange(source, this.model.getLowerBound(), this.model.getUpperBound(), output);

                var contours = new ArrayList<MatOfPoint>();

                // Imgproc.findContours(output, contours, null, Imgproc.RETR_TREE,
                // Imgproc.CHAIN_APPROX_SIMPLE);

                this.things.clear();

                // for (MatOfPoint contour : contours) {
                // var rect = Imgproc.boundingRect(contour);

                // this.things.add(new Thing(getCenterOfRect(rect), rect));
                // }

                // Imgproc.drawContours(source, contours, 0, new Scalar(255, 0, 0));

                outputStream.putFrame(output);
            }
        }).start();
    }

    private Mat ProcessImage(Mat source) {
        // blur
        // BGR to HSV
        // In Range
        // Dialte to fill in holes.
        // Erode back down to normal.

        // find contours
        // loop through contours
        // get bounding rect of contour
        // if quality of rect is good (aspect ratio, area, etc.)
        // Draw rect on image
        // draw crosshairs on image
        // return image
        return null;
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
