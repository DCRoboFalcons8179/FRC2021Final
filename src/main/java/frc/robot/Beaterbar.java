package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
// Inputs
//need joystick to control beater bar
Public class Beaterbar {

    // Declaring variables
    Joystick joy;

    int forward;

    int reverse;

    Public void Beaterbar (Joystick inputJoy, int inputForward, int inputReverse) {
    
      joy = inputJoy;

      forward = inputForward;

      reverse = inputReverse;

    }

    Public void periodicbar () {
        //Find if button for Beater Bar is pressed
        if (joy.getRawButton(forward)) {
   
        }  else if (joy.getRawButton(reverse)) {

        }
    }

}