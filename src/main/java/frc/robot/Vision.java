package frc.robot;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
    private double x;
    private double y;

    public boolean isRunning = false;

    public void init() {
        SmartDashboard.putNumber("r1", 0);
        SmartDashboard.putNumber("r2", 0);
        SmartDashboard.putNumber("g1", 0);
        SmartDashboard.putNumber("g2", 0);
        SmartDashboard.putNumber("b1", 0);
        SmartDashboard.putNumber("b2", 0);

        new Thread(() -> {
            UsbCamera camera = CameraServer.startAutomaticCapture();
            camera.setResolution(640, 480);
            camera.setFPS(10);

            CvSink cvSink = CameraServer.getVideo();
            CvSource outputStream = CameraServer.putVideo("Output", 640, 480);

            Mat source = new Mat();
            Mat beep = new Mat();

            while (!Thread.interrupted()) {
                if (!this.isRunning) {
                    continue;
                }

                if (cvSink.grabFrame(source) == 0) {
                    continue;
                }

                var r1 = SmartDashboard.getNumber("r1", 0);
                var r2 = SmartDashboard.getNumber("r2", 0);
                var g1 = SmartDashboard.getNumber("g1", 0);
                var g2 = SmartDashboard.getNumber("g2", 0);
                var b1 = SmartDashboard.getNumber("b1", 0);
                var b2 = SmartDashboard.getNumber("b2", 0);

                Core.inRange(source, new Scalar(r1, g1, b1), new Scalar(r2, g2, b2), beep);

                // Imgproc.findContours(beep, contours, null, Imgproc.RETR_TREE,
                // Imgproc.CHAIN_APPROX_SIMPLE);

                // Imgproc.drawContours(source, contours, 0, new Scalar(255, 0, 0));

                Mat output = new Mat();

                Core.bitwise_or(source, source, output, beep);

                outputStream.putFrame(output);
            }
        }).start();
    }

    public void start() {
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}
