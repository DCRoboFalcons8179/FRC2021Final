package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.Joystick;
// Inputs
//need joystick to control beater bar
public class Beaterbar {

    // Declaring variables
    Joystick joy;

    int forward;

    int reverse;

    TalonFX bbar;

    //joystick objects are not directly available in main method, get values from controls class
    public Beaterbar (Joystick inputJoy, int inputForward, int inputReverse, TalonFX bbarMotor) {
    
      bbar = bbarMotor;

      joy = inputJoy;

      forward = inputForward;

      reverse = inputReverse;

    }

    //needs forward/reverse signal as method parameter
    public void periodicbar () {
        //Find if button for Beater Bar is pressed
        if (joy.getRawButton(forward)) {

          bbar.set(ControlMode.PercentOutput, .50);
   
        }  else if (joy.getRawButton(reverse)) {

          bbar.set(ControlMode.PercentOutput, -.50);

        } else {

          bbar.set(ControlMode.PercentOutput, 0); 

        }
    }

}