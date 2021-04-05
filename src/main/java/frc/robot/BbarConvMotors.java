package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
// Inputs
//need joystick to control beater bar
public class BbarConvMotors {

    // Declaring variables
    Joystick dashboard;

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
    public BbarConvMotors(Joystick inputdashboard, int bForward, int bReverse, int bForwardContinous, int bReverseContinous, TalonFX bbarMotor, 
        int ccForward, int ccReverse, int ccForwardCont, int ccReverseCont, VictorSPX convMotor, Joystick Joy_in) {
    
      bbar = bbarMotor;

      dashboard = inputdashboard;

      bbarForward = bForward;

      bbarReverse = bReverse;

      bbarForwardContinous = bForwardContinous;

      bbarReverseContinous = bReverseContinous;

      convForward = ccForward;

      convReverse = ccReverse;

      convForwardContinous = ccForwardCont;

      convReverseContinous = ccReverseCont;

      conveyor = convMotor;

      joy = Joy_in;

    }

    private boolean go = false;
    private boolean up = true;

    private double speed = 0.75;

    //needs forward/reverse signal as method parameter
    public void periodic_bar_conv (boolean b1, boolean b2, boolean b3) {
        
      
      
      
      //Find if button for Beater Bar is pressed; set beater bar output
        if (dashboard.getRawButtonPressed(bbarForward)) {

          go = !go;
          up = true;
   
        }  else if (dashboard.getRawButtonPressed(bbarReverse)) {

          go = !go;
          up = false;

        } else  {
          
          if (dashboard.getRawButton(bbarForwardContinous)) {

            bbar.set(ControlMode.PercentOutput, .20);
            go = false;

          }
          else if (dashboard.getRawButton(bbarReverseContinous)){

            bbar.set(ControlMode.PercentOutput, -.20);
            go = false;
            
          } else {
            bbar.set(ControlMode.PercentOutput, 0);
          }
        }
        if (go) {
          if (up) {

            bbar.set(ControlMode.PercentOutput, .20);

          } else {

            bbar.set(ControlMode.PercentOutput, -.20);

          }
        } 
        // ON UNTIL BUTTON 2
        if (joy.getRawButtonPressed(2)) {
          speed = 0.75;
        }
        if (joy.getRawButton(2)) {

          bbar.set(ControlMode.PercentOutput, .20);
          
          if (!b2) {
            speed = 0;
          }
          else if (!b1) {

            speed = 0.4;

          }
          conveyor.set(ControlMode.PercentOutput, speed);

        }else {
          speed = 0.75;
          // NORMAL OPERATIONS
          if (dashboard.getRawButtonPressed(convForward)) {

            conveyor.set(ControlMode.PercentOutput, .75);

          }
          else if (dashboard.getRawButtonPressed(convReverse)){

            conveyor.set(ControlMode.PercentOutput, -.75);
            
          } else if (dashboard.getRawButton(convForwardContinous)) {

              conveyor.set(ControlMode.PercentOutput, .75);

          } else if (dashboard.getRawButton(convReverseContinous)) {

              conveyor.set(ControlMode.PercentOutput, -.75);

          }  else {

              conveyor.set(ControlMode.PercentOutput, 0);

          }
        }
      }
    }