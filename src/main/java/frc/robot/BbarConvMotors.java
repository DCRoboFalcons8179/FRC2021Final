package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
// Inputs
//need joystick to control beater bar
public class BbarConvMotors {

    // Declaring variables
    Joystick joy;

    int bbarForward;

    int bbarReverse;

    int bbarForwardContinous;

    int bbarReverseContinous;

    TalonFX bbar;

    VictorSPX conveyor;

    int convForward;

    int convReverse;

    int convForwardContinous;

    int convReverseContinous;

    //joystick objects are not directly available in main method, get values from controls class
    public BbarConvMotors(Joystick inputJoy, int bForward, int bReverse, TalonFX bbarMotor, 
        int ccForward, int ccReverse, int ccForwardCont, int ccReverseCont, VictorSPX convMotor) {
    
      bbar = bbarMotor;

      joy = inputJoy;

      bbarForward = bForward;

      bbarReverse = bReverse;

      convForward = ccForward;

      convReverse = ccReverse;

      convForwardContinous = ccForwardCont;

      convReverseContinous = ccReverseCont;

      conveyor = convMotor;

    }

    //needs forward/reverse signal as method parameter
    public void periodic_bar_conv () {
        //Find if button for Beater Bar is pressed; set beater bar output
        if (joy.getRawButton(bbarForward)) {

          bbar.set(ControlMode.PercentOutput, .50);
   
        }  else if (joy.getRawButton(bbarReverse)) {

          bbar.set(ControlMode.PercentOutput, -.50);
         }
        
        else {

          bbar.set(ControlMode.PercentOutput, 0); 

        }


        if (joy.getRawButtonPressed(convForward)) {

          conveyor.set(ControlMode.PercentOutput, .75);

        }
        else if (joy.getRawButtonPressed(convReverse)){

          conveyor.set(ControlMode.PercentOutput, -.75);
          
        } else if (joy.getRawButton(convForwardContinous)) {

          conveyor.set(ControlMode.PercentOutput, .75);

        } else if (joy.getRawButton(convReverseContinous)) {

          conveyor.set(ControlMode.PercentOutput, -.75);

        }  else {

          conveyor.set(ControlMode.PercentOutput, 0);

        }
      }
}