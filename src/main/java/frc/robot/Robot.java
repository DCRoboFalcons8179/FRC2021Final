/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.shuffleboard.*;

public class Robot extends TimedRobot {

  // BEGIN Declare and Attach CAN IDs to devices
  // PDP
  private final PowerDistributionPanel PDP = new PowerDistributionPanel(0);
  private TalonFX tilt_motor = new WPI_TalonFX(6);
	private TalonSRX leftTurret = new  WPI_TalonSRX(7);
	private TalonSRX rightTurret = new WPI_TalonSRX(8);

	// Drive Motors
	private TalonSRX leftDrive = new WPI_TalonSRX(2);
	private TalonSRX rightDrive = new WPI_TalonSRX(4);
	private VictorSPX leftFollow = new WPI_VictorSPX(13);
	private VictorSPX rightFollow = new WPI_VictorSPX(14);
	
	private Joystick _gamepad = new Joystick(3);

	private driveMotorVelocity vroom = new driveMotorVelocity(leftDrive, rightDrive, leftFollow, rightFollow,_gamepad);


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


		tilt = new tiltcontrol(tilt_motor,_gamepad);
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
		shooterSpeed = new velocityControl(leftTurret,rightTurret,_gamepad,3,3500);
		shooterSpeed.throttleType = true;
  }

  
   @Override
   public void teleopPeriodic() {
		tilt.updateSensors();
		tilt_deg = tilt.tilt_degrees;

		shooterSpeed.velocityControlPeriodic();
		tilt.tiltcontrolPeriodic();

		// CONTROLLING THE SHOOTER WHEELS

		// Computer Control
		if(computer_rpm_enable) {
			shooterSpeed.set_rpm = computer_rpm;
			shooterSpeed.rpm_set_mode = true;
		} 

		else {
			
			shooterSpeed.rpm_set_mode = false;
			
		}
		shooter_rpm = shooterSpeed.actual_RPM;
	
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
		if (_gamepad.getRawButton(5)) {
			tilt.resetSensors();
		}

		// CONTROLLING THE WHEELS

		vroom.velocityControlPeriodic();



   }
}
