package frc.robot.vision;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
    private Integer cameraIndex;

    public boolean isRunning = false;

    public VisionModel model;

    private VisionObject target;

    // private Timer benchmarkTimer;

    public Vision(int cameraIndex) {
        this.cameraIndex = cameraIndex;
        // this.benchmarkTimer = new Timer();
    }

    // private void writeBenchmarkTime(String label) {
    // SmartDashboard.putNumber("Vision Benchmark - " + label,
    // this.benchmarkTimer.get());
    // this.benchmarkTimer.reset();
    // }

    public void init() {
        new Thread(() -> {
            UsbCamera camera = CameraServer.startAutomaticCapture(this.cameraIndex);

            camera.setResolution(640, 480);
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
        Imgproc.medianBlur(source, source, 3);

        // writeBenchmarkTime("Blurred");

        // BGR to HSV
        Mat hsv = new Mat();
        Imgproc.cvtColor(source, hsv, Imgproc.COLOR_BGR2HSV);

        // In Range
        Mat inRange = new Mat();
        Core.inRange(hsv, this.model.getLowerBound(), this.model.getUpperBound(), inRange);

        // Dilate to fill in holes.
        var kernel = new Mat();
        Imgproc.dilate(inRange, inRange, kernel);

        Imgproc.dilate(inRange, inRange, kernel);

        // Erode back down to normal.
        Imgproc.erode(inRange, inRange, kernel);

        // find contours
        var contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(inRange, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        var contourCollection = ContourCollection.fromContours(contours, source.width(), source.height());

        var target = this.model.getBestObject(contourCollection);

        this.target = target;

        if (target != null) {
            // Imgproc.rectangle(source, new Point(50, 50), new Point(100, 100), new
            // Scalar(255, 0, 0), 2);
            Imgproc.rectangle(source, target.rect.tl(), target.rect.br(), new Scalar(0, 0, 255), 2);

            SmartDashboard.putNumber("Vision X", this.target.getCenter().x);
            SmartDashboard.putNumber("Vision Y", this.target.getCenter().y);

            SmartDashboard.putNumber("Vision area", this.target.area());
        } else {
            SmartDashboard.putNumber("Vision X", -1);
            SmartDashboard.putNumber("Vision Y", -1);

            SmartDashboard.putNumber("Vision area", -1);

        }

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

    /**
     * Return 0.0 to 1.0 to give proportion of vision width to target.
     * 0.0 is far left edge.
     * 0.5 is center
     * 1.0 is far right edge
     * -1.0 for no target
     * 
     * @return
     */
    public VisionObject getTarget() {
        return target;
    }

    public void setModel(VisionModel model) {
        this.model = model;
    }
}
