package frc.robot;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;

public class Camera {
    UsbCamera camera;
    public Camera(int xResloution, int yResolution, int framerate){
        camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(xResloution, yResolution);
        camera.setFPS(framerate);
        camera.setPixelFormat(PixelFormat.kMJPEG);
        camera.setExposureAuto();
        camera.setBrightness(50);
    }
}
