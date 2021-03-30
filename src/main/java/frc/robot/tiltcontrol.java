package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice; 
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.Joystick;

public class tiltcontrol {

    static final Gains kGains = new Gains(0.05, 0.002, 1.5, 0, 0, 1.0);

    private TalonFX tilt;

    public double tilt_reading;
    public double tilt_degrees;

    private double ktranslate = 569;

    final int kUnitisPerRev = 2048;

    Joystick joy;
    public double setpoint;
    public int y = 1;

    private int green_zone = 12;
    private int yellow_zone = 2;
    private int blue_zone = 4;
    private int red_zone = 8;


    public tiltcontrol(TalonFX tilt_input, Joystick joy_input) {

        tilt = tilt_input;
        joy = joy_input;

        // Falcon Configuration
        tilt.configFactoryDefault();
        tilt.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0,Constants.kTimeoutMs);


        tilt.configNominalOutputForward(0,Constants.kTimeoutMs);
        tilt.configNominalOutputReverse(0,Constants.kTimeoutMs);
        tilt.configPeakOutputForward(0.7,Constants.kTimeoutMs);
        tilt.configPeakOutputReverse(-0.5,Constants.kTimeoutMs);
        
        tilt.configAllowableClosedloopError(0, 50, Constants.kTimeoutMs);

        tilt.setSensorPhase(false);
        tilt.setInverted(false);

        tilt.config_kF(0, kGains.kF,Constants.kTimeoutMs);
        tilt.config_kP(0, kGains.kP,Constants.kTimeoutMs);
        tilt.config_kI(0, kGains.kI,Constants.kTimeoutMs);
        tilt.config_kD(0, kGains.kD,Constants.kTimeoutMs);

        tilt.configClearPositionOnLimitR(true, 100);

        tilt.setNeutralMode(NeutralMode.Brake);

        tilt.setSelectedSensorPosition(0, 0, Constants.kTimeoutMs);
        tilt.setSelectedSensorPosition(0);

        setpoint = 0;
    }

    public void updateSensors() {
        tilt_reading = tilt.getSelectedSensorPosition(0);
        tilt_degrees = tilt_reading / ktranslate;
    }

    public void resetSensors() {
        // tilt.setSelectedSensorPosition(0);
        setpoint = 0;

    }

    public void tiltcontrolPeriodic() {

        if (joy.getRawAxis(y) == -1) {
            tilt.set(ControlMode.PercentOutput, 0.1);
            setpoint = tilt.getSelectedSensorPosition() / ktranslate;
            // System.out.println("Percent"); 

        }else if (joy.getRawAxis(y) == 1) {
            tilt.set(ControlMode.PercentOutput, -0.07);
            setpoint = tilt.getSelectedSensorPosition() / ktranslate;
               
        
        } else {
            tilt.set(ControlMode.Position, (setpoint * ktranslate));

            // System.out.println("PID");

        }
    }

    public void zonetiltcontrol() {

        if (joy.getRawButtonPressed(green_zone)) {
            setpoint = 0;
            } else if(joy.getRawButtonPressed(yellow_zone)) {
                setpoint = 0;
            } else if(joy.getRawButtonPressed(blue_zone)){
                setpoint = 0;
            } else if(joy.getRawButtonPressed(red_zone)){
                setpoint = 0;
            } else {
                setpoint = 0;
            }

        }
        
        // System.out.println(setpoint);
        // System.out.println(tilt.getClosedLoopError(0));
        // System.out.println(tilt_reading);

        // if (tilt.isRevLimitSwitchClosed()==1) {
        //     tilt.setSelectedSensorPosition(0, 0, Constants.kTimeoutMs);
        // }

       
    }
