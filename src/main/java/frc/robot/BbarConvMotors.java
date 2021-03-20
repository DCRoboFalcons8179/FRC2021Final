package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.Joystick;
// Inputs
//need joystick to control beater bar
public class BbarConvMotors {

    // Declaring variables
    Joystick joy;

    int forward;

    int reverse;

    TalonFX bbar;

    TalonFX conveyor;

    int convForward;

    int convReverse;

    //joystick objects are not directly available in main method, get values from controls class
    public BbarConvMotors(Joystick inputJoy, int inputForward, int inputReverse, TalonFX bbarMotor, 
        int ccForward, int ccReverse, TalonFX convMotor) {
    
      bbar = bbarMotor;

      joy = inputJoy;

      forward = inputForward;

      reverse = inputReverse;

      convForward = ccForward;

      convReverse = ccReverse;

      conveyor = convMotor;

    }

    //needs forward/reverse signal as method parameter
    public void periodic_bar_conv () {
        //Find if button for Beater Bar is pressed; set beater bar output
        if (joy.getRawButton(forward)) {

          bbar.set(ControlMode.PercentOutput, .50);
   
        }  else if (joy.getRawButton(reverse)) {

          bbar.set(ControlMode.PercentOutput, -.50);

        } else {

          bbar.set(ControlMode.PercentOutput, 0); 

        }
        if (joy.getRawButtonPressed(convForward)) {

          conveyor.set(ControlMode.PercentOutput, .75);

        }
        else if (joy.getRawButtonPressed(convReverse)){

          conveyor.set(ControlMode.PercentOutput, -.75);
          
        }

        else {

          conveyor.set(ControlMode.PercentOutput, 0);

        }
      }
}