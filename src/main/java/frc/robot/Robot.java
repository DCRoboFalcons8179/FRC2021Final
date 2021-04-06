/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.shuffleboard.*;
import java.util.Scanner;
import java.io.File;  

public class Robot extends TimedRobot {

  // BEGIN Declare and Attach CAN IDs to devices
  // PDP
  	// private final PowerDistributionPanel PDP = new PowerDistributionPanel(0);
  	private TalonFX tilt_motor = new WPI_TalonFX(6);
  	private TalonSRX leftTurret = new  WPI_TalonSRX(7);
  	private TalonSRX rightTurret = new WPI_TalonSRX(8);
  


	// Drive Motors
	private TalonSRX leftDrive = new WPI_TalonSRX(2);
	private TalonSRX rightDrive = new WPI_TalonSRX(4);
	private VictorSPX leftFollow = new WPI_VictorSPX(13);
	private VictorSPX rightFollow = new WPI_VictorSPX(14);
	
	private Joystick _gamepad = new Joystick(3);

  private Limelight limelight = new Limelight();

	private Joystick dashboard = new Joystick(4);

  private driveMotorVelocity vroom = new driveMotorVelocity(leftDrive, rightDrive, leftFollow, 
    rightFollow,_gamepad, dashboard, 0);
  
  BbarConvMotors bbar;
  TalonFX bbar_motor;
  TalonFX conv_motor;

  velocityControl shooterSpeed;
	double shooter_rpm;
	double computer_rpm;
	boolean computer_set_rpm = false;
	boolean computer_rpm_enable = false;

	double tilt_deg;
	double computer_tilt;
	boolean computer_set_tilt = false;
	boolean computer_tilt_enable = false;

  tiltcontrol tilt;
  
  private ShuffleboardTab tab = Shuffleboard.getTab("Turret");
	private NetworkTableEntry computer_rpm_table;
	private NetworkTableEntry computer_rpm_set_table;
	private NetworkTableEntry computer_rpm_reading;

	private NetworkTableEntry computer_tilt_set_table;
	private NetworkTableEntry computer_tilt_table;
	private NetworkTableEntry computer_tilt_reading;

  DigitalInput button1 = new DigitalInput(1);
  DigitalInput button2 = new DigitalInput(2);
  DigitalInput button3 = new DigitalInput(3);

  private NetworkTableEntry button1_network_table;
  private NetworkTableEntry button2_network_table;
  private NetworkTableEntry button3_network_table;


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    computer_rpm_reading = tab.add("RPM Reading", 0).getEntry();
		computer_rpm_table = tab.add("RPM Set Value", 0).getEntry();
		computer_rpm_set_table = tab.add("RPM Set Enable", false).getEntry();
		

		computer_tilt_reading = tab.add("Tilt Reading", 0).getEntry();
		computer_tilt_table = tab.add("Tilt Set Value", 0).getEntry();
		computer_tilt_set_table = tab.add("Tilt Set Enable", false).getEntry();


    button1_network_table = tab.add("Conveyor Index 1", false).getEntry();
    button2_network_table = tab.add("Conveyor Index 2", false).getEntry();
    button3_network_table = tab.add("Conveyor Index 3", false).getEntry();


    tilt = new tiltcontrol(tilt_motor,dashboard);


    final TalonFX bbar_motor = new TalonFX(11);
    final VictorSPX conv_motor = new WPI_VictorSPX(3);

    bbar = new BbarConvMotors(dashboard, 7, 5, 6, 11, bbar_motor, 1, 9, 10, 3, conv_motor,_gamepad);


    try
    {
        Logging.CustomLogger.setup();
    }
    catch (Throwable e) { Logging.logException(e); }
    
    Logging.consoleLog();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */


   boolean b1;
   boolean b2;
   boolean b3;


  @Override
  public void robotPeriodic() {
    b1 = button1.get();
    b2 = button2.get();
    b3 = button3.get();

    limelight.refreshValues();

  
    computer_rpm_reading.setDouble(shooter_rpm);
    computer_rpm = computer_rpm_table.getDouble(0);
    computer_rpm_enable = computer_rpm_set_table.getBoolean(false);


    
    computer_tilt_reading.setDouble(tilt.tilt_degrees);
    computer_tilt = computer_tilt_table.getDouble(0);
    computer_tilt_enable = computer_tilt_set_table.getBoolean(false);
    
    button1_network_table.setBoolean(b1);
    button2_network_table.setBoolean(b2);
    button3_network_table.setBoolean(b3);


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
  Scanner inputData;
  
  @Override
  public void autonomousInit() {
    File inputs = new File("/home/lvuser/deploy/log.csv");
    try{
      inputData = new Scanner(inputs);
    }
    catch(Exception e){}
  }

  /**
   * This function is called periodically during autonomous.
   */

  @Override
  public void autonomousPeriodic() {
    String[] controls;
    if(inputData != null){
      String rawInput = inputData.nextLine();
      controls = rawInput.split(",");
      System.out.println(controls[0]);
    }
    else{
      controls = new String[]{"0","0", "false"};
    }

    vroom.velocityControlPeriodic(limelight.tx, Double.parseDouble(controls[0]), Double.parseDouble(controls[1]), Boolean.parseBoolean(controls[2]));
  }
  /**
   * This function is called periodically during operator control.
   */
  
  @Override
  public void teleopInit() {
		shooterSpeed = new velocityControl(leftTurret,rightTurret,_gamepad,3,3500);
		shooterSpeed.throttleType = true;
  }

  
  zone green = new zone(0,1350);
  zone yellow = new zone(16,6100);
  zone blue = new zone(8.5,6100);
  zone red = new zone(6,6000);

   @Override
   public void teleopPeriodic() {



    boolean gb = dashboard.getRawButton(12);
    boolean yb = dashboard.getRawButton(2);
    boolean bb = dashboard.getRawButton(4);
    boolean rb = dashboard.getRawButton(8);

    double buttonspeed = 0;
    double buttontilt = 0;


		tilt.updateSensors();
    tilt_deg = tilt.tilt_degrees;



		//beaterbar and conveyor
		bbar.periodic_bar_conv(b1,b2,b3);


    // BUTTON OVERRIDE
    if (gb||yb||bb||rb) {
      if (gb) {
        buttonspeed = green.rpm;
        buttontilt = green.tilt;
      }
      else if (yb) {
        buttonspeed = yellow.rpm;
        buttontilt = yellow.tilt;
      }
      else if (bb) {
        buttonspeed = blue.rpm;
        buttontilt = blue.tilt;
      }
      else if (rb) {
        buttonspeed = red.rpm;
        buttontilt = red.tilt;
      }
      tilt.zonetiltcontrol(buttontilt);
      shooterSpeed.set_rpm = buttonspeed;
      shooterSpeed.rpm_set_mode = true;
    }
    else {
      // CONTROLLING THE SHOOTER WHEELS

      buttonspeed = 0;
      buttontilt = 0;

      // Computer Control
      if(computer_rpm_enable) {
        shooterSpeed.set_rpm = computer_rpm;
        shooterSpeed.rpm_set_mode = true;
      } 
      else {
        
        shooterSpeed.rpm_set_mode = false;
        
      }
    
      // CONTROLLING THE TILT CONTROL

      if(computer_tilt_enable) {
        tilt.setpoint = computer_tilt;
      } else {
        if (_gamepad.getRawButtonPressed(3)) {
          tilt.setpoint = 10;
        } else if (_gamepad.getRawButtonPressed(4)){
          tilt.setpoint = 20;
        }
      }

    }

    shooter_rpm = shooterSpeed.actual_RPM;

    shooterSpeed.velocityControlPeriodic();
    tilt.tiltcontrolPeriodic();

    
    if (_gamepad.getRawButton(5)) {
      tilt.resetSensors();
    }

    // CONTROLLING THE WHEELS
    Logging.consoleLog();
    Logging.consoleLog(Double.toString(_gamepad.getTwist()));
    Logging.consoleLog(Double.toString(_gamepad.getY()));
    Logging.consoleLog(Boolean.toString(_gamepad.getRawButton(1)));

		vroom.velocityControlPeriodic(limelight.tx, _gamepad.getTwist(), _gamepad.getY(), _gamepad.getRawButton(1));

    
   }
}
