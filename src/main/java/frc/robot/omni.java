package frc.robot;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class omni {
    private static final WPI_VictorSPX leftDriveA = new WPI_VictorSPX(2);
    private static final WPI_VictorSPX leftDriveB = new WPI_VictorSPX(4);
    private static final WPI_VictorSPX rightDriveA = new WPI_VictorSPX(13);
    private static final WPI_VictorSPX rightDriveB = new WPI_VictorSPX(14);

    private static final DifferentialDrive drive = new DifferentialDrive(leftDriveA, rightDriveA);

    public static void setup(){
        leftDriveB.follow(leftDriveA);
        rightDriveB.follow(rightDriveA);
    }
    public static void drive(double forward, double turn){
        drive.arcadeDrive(forward, turn);
    }
}
