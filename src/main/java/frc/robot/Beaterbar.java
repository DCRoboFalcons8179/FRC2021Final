package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
// Inputs
//need joystick to control beater bar
public class Beaterbar {

    // Declaring variables
    Joystick joy;

    int forward;

    int reverse;

    //joystick objects are not directly available in main method, get values from controls class
    public Beaterbar (Joystick inputJoy, int inputForward, int inputReverse) {
    
      joy = inputJoy;

      forward = inputForward;

      reverse = inputReverse;

    }

    //needs forward/reverse signal as method parameter
    public void periodicbar () {
        //Find if button for Beater Bar is pressed
        if (joy.getRawButton(forward)) {
   
        }  else if (joy.getRawButton(reverse)) {

        }
    }

}