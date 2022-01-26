package frc.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;

public class Vision {
    private double x;
    private double y;

    public void start() {
        new Thread(() -> {
            UsbCamera camera = CameraServer.startAutomaticCapture();
            camera.setResolution(640, 480);

            CvSink cvSink = CameraServer.getVideo();
            CvSource outputStream = CameraServer.putVideo("Blur", 640, 480);

            Mat source = new Mat();
            Mat output = new Mat();

            while (!Thread.interrupted()) {
                if (cvSink.grabFrame(source) == 0) {
                    continue;
                }
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
        }).start();
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}
