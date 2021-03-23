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
    public BbarConvMotors(Joystick inputJoy, int bForward, int bReverse, int bForwardContinous, int bReverseContinous, TalonFX bbarMotor, 
        int ccForward, int ccReverse, int ccForwardCont, int ccReverseCont, VictorSPX convMotor) {
    
      bbar = bbarMotor;

      joy = inputJoy;

      bbarForward = bForward;

      bbarReverse = bReverse;

      bbarForwardContinous = bForwardContinous;

      bbarReverseContinous = bReverseContinous;

      convForward = ccForward;

      convReverse = ccReverse;

      convForwardContinous = ccForwardCont;

      convReverseContinous = ccReverseCont;

      conveyor = convMotor;

    }

    private boolean go = false;
    private boolean up = true;

    //needs forward/reverse signal as method parameter
    public void periodic_bar_conv () {
        //Find if button for Beater Bar is pressed; set beater bar output
        if (joy.getRawButtonPressed(bbarForward)) {

          go = !go;
          up = true;
   
        }  else if (joy.getRawButtonPressed(bbarReverse)) {

          go = !go;
          up = false;

        } else  {
          
          if (joy.getRawButton(bbarForwardContinous)) {

            bbar.set(ControlMode.PercentOutput, .50);
            go = false;

          }
          else if (joy.getRawButton(bbarReverseContinous)){

            bbar.set(ControlMode.PercentOutput, -.50);
            go = false;
            
          } else {
            bbar.set(ControlMode.PercentOutput, 0);
          }
        }
        if (go) {
          if (up) {

            bbar.set(ControlMode.PercentOutput, .50);

          } else {

            bbar.set(ControlMode.PercentOutput, -.50);

          }
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