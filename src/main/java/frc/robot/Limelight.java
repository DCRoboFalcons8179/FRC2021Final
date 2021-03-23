package frc.robot;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    public double tv; //Determines whether the limelight has any valid targets (0 or 1, but the .getDouble(0) requires it to be a double)
    public double tx; //Horizontal offset from crosshair to target (LL2 (what we have): -29.8 degrees to 29.8 degrees)
    public double ty; //Vertical offset from crosshair to target (LL2 (what we have): -24.85 degrees to 24.85 degrees)
    public double ta; //Target area (0 - 100%)
    public double ts; //Skew or rotation (-90 degrees to 0 degrees)
    public double tl; //Latency

    public double tshort; //Sidelength of shortest side of the fitted bounding box (pixels)
    public double tlong; //Sidelength of the longest side of the fitted bounding box (pixels)
    public double thor; //Horizontal sidelength of the rough bounding box (0 - 320 pixels)
    public double tvert; //Vertical sidelength of the rough bounding box (0 - 320 pixels)
    public double getpipe; //Active pipeline index of the camera (0 - 9)

    public void refreshValues(){
        tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        ts = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ts").getDouble(0);
        ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        tl = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tl").getDouble(0);
      
        tshort = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tshort").getDouble(0);
        tlong = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tlong").getDouble(0);
        thor = NetworkTableInstance.getDefault().getTable("limelight").getEntry("thor").getDouble(0);
        tvert = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tvert").getDouble(0);
        getpipe = NetworkTableInstance.getDefault().getTable("limelight").getEntry("getPipe").getDouble(0);
    }
}
