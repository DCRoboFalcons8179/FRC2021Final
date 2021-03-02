/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;




public class Robot extends TimedRobot {

  // BEGIN Declare and Attach CAN IDs to devices
  // PDP
  
  //Large logitech joystick
  public Joystick BigLog = new Joystick(0);


  // Beater Bar
  // Beater Bar Code
  public final WPI_TalonFX bbar_motor = new WPI_TalonFX(11);

  public Beaterbar bbar = new Beaterbar(BigLog, 8, 5, bbar_motor);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    Mecanum.setup();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */

  @Override
  public void robotPeriodic() {
    
// Pull this


  }



  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {

  }

  /**
   * This function is called periodically during autonomous.
   */

  @Override
  public void autonomousPeriodic() {

  }
  /**
   * This function is called periodically during operator control.
   */
  
  @Override
  public void teleopInit() {

  }

  
   @Override
  public void teleopPeriodic() {

    bbar.periodicbar();

    // Mecanum.drive(forward, side);
   }
}
