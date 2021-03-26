package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.*;

public class velocityControl {

    	/** Hardware */
	private TalonSRX _leftMaster;
	private TalonSRX _rightMaster;
    private Joystick _gamepad;
    
    // Control Mapping
    private int axis;
    public boolean throttleType = false;

    // RPM Mapping
    public double max_rpm;

    public boolean rpm_set_mode = false;
    public double set_rpm;

	
	// /** Latched values to detect on-press events for buttons and POV */
	// private boolean[] _btns = new boolean[Constants.kNumButtonsPlusOne];
	// private boolean[] btns = new boolean[Constants.kNumButtonsPlusOne];
	
	/** Tracking variables */
	private boolean _firstCall = false;
    private boolean _state = false;
    
    public double actual_RPM;


    public velocityControl(TalonSRX _leftMasterInput, TalonSRX _rightMasterInput, Joystick _gamepadInput, int axis_input, int rpm_input) {
        // Initalize the Motors and Joysticks

        _leftMaster = _leftMasterInput;
        _rightMaster = _rightMasterInput;
        _gamepad = _gamepadInput;
        axis = axis_input;
        max_rpm = rpm_input;



        	/* Disable all motors */
		_rightMaster.set(ControlMode.PercentOutput, 0);
        _leftMaster.set(ControlMode.PercentOutput,  0);
        
        /* Factory Default all hardware to prevent unexpected behaviour */
        _rightMaster.configFactoryDefault();
        _leftMaster.configFactoryDefault();
		
		/* Set neutral modes */
		_leftMaster.setNeutralMode(NeutralMode.Coast);
		_rightMaster.setNeutralMode(NeutralMode.Coast);
		
		/** Feedback Sensor Configuration */
		
		/* Configure the left Talon's selected sensor as local QuadEncoder */
		_leftMaster.configSelectedFeedbackSensor(	FeedbackDevice.QuadEncoder,				// Local Feedback Source
													Constants.PID_PRIMARY,					// PID Slot for Source [0, 1]
													Constants.kTimeoutMs);					// Configuration Timeout

		/* Configure the Remote Talon's selected sensor as a remote sensor for the right Talon */
		_rightMaster.configRemoteFeedbackFilter(_leftMaster.getDeviceID(),					// Device ID of Source
												RemoteSensorSource.TalonSRX_SelectedSensor,	// Remote Feedback Source
												Constants.REMOTE_0,							// Source number [0, 1]
												Constants.kTimeoutMs);						// Configuration Timeout
		
		/* Setup Sum signal to be used for Velocity */
		_rightMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, Constants.kTimeoutMs);     // Feedback Device of Remote Talon
		_rightMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, Constants.kTimeoutMs);       // Quadrature Encoder of current Talon
		
		/* Configure Sum [Sum of both QuadEncoders] to be used for Primary PID Index */
		_rightMaster.configSelectedFeedbackSensor(	FeedbackDevice.SensorSum, 
													Constants.PID_PRIMARY,
													Constants.kTimeoutMs);
		
		/* Scale Feedback by 0.5 to half the sum of Velocity */
		_rightMaster.configSelectedFeedbackCoefficient( 1, 						// Coefficient
														Constants.PID_PRIMARY,		// PID Slot of Source 
														Constants.kTimeoutMs);		// Configuration Timeout
		
		/* Configure output and sensor direction */
		_leftMaster.setInverted(false);
		_leftMaster.setSensorPhase(true);
		_rightMaster.setInverted(true);
		_rightMaster.setSensorPhase(false);
		
		/* Set status frame periods to ensure we don't have stale data */
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, Constants.kTimeoutMs);
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, Constants.kTimeoutMs);
		_leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.kTimeoutMs);

		/* Configure neutral deadband */
		_rightMaster.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);
		_leftMaster.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);

		/**
		 * Max out the peak output (for all modes).  
		 * However you can limit the output of a given PID object with configClosedLoopPeakOutput().
		 */
		_leftMaster.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
		_leftMaster.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);
		_rightMaster.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
		_rightMaster.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);
		
		/* FPID Gains for velocity servo */
		_rightMaster.config_kP(Constants.kSlot_Velocit, Constants.kGains_Velocit.kP, Constants.kTimeoutMs);
		_rightMaster.config_kI(Constants.kSlot_Velocit, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
		_rightMaster.config_kD(Constants.kSlot_Velocit, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);
		_rightMaster.config_kF(Constants.kSlot_Velocit, Constants.kGains_Velocit.kF, Constants.kTimeoutMs);
		_rightMaster.config_IntegralZone(Constants.kSlot_Velocit, Constants.kGains_Velocit.kIzone, Constants.kTimeoutMs);
		_rightMaster.configClosedLoopPeakOutput(Constants.kSlot_Velocit, Constants.kGains_Velocit.kPeakOutput, Constants.kTimeoutMs);
		_rightMaster.configAllowableClosedloopError(Constants.kSlot_Velocit, 0, Constants.kTimeoutMs);
		_leftMaster.configAllowableClosedloopError(0, 0, Constants.kTimeoutMs);
			
		/**
		 * 1ms per loop.  PID loop can be slowed down if need be.
		 * For example,
		 * - if sensor updates are too slow
		 * - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
		 * - sensor movement is very slow causing the derivative error to be near zero.
		 */
        int closedLoopTimeMs = 1;
        _rightMaster.configClosedLoopPeriod(0, closedLoopTimeMs, Constants.kTimeoutMs);
		_rightMaster.configClosedLoopPeriod(1, closedLoopTimeMs, Constants.kTimeoutMs);

		// Allowable Error
		// _rightMaster.configClosedLoop


		/* Initialize */
		_firstCall = true;
		_state = false;
		zeroSensors();
        
    }



    public void velocityControlPeriodic() {
		/* Gamepad processing */
		double forward = getForward();
		// double turn = _gamepad.getTwist();
		double turn = 0; 
		forward = Deadband(forward);
        turn = Deadband(turn);


            
		/* Button processing for state toggle and sensor zeroing */
        _state = true;
		
		if(!_state){
			if (_firstCall)
				// System.out.println("This is Arcade drive.\n");
			
			_leftMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn);
			_rightMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, -turn);
			
			/* Uncomment to view velocity native units */
		}else{
			if (_firstCall) {
				System.out.println("This is Velocity Closed Loop with an Arbitrary Feed Forward.");
				System.out.println("Travel [-500, 500] RPM while having the ability to add a FeedForward with joyX ");
				zeroSensors();
				
				/* Determine which slot affects which PID */
				_rightMaster.selectProfileSlot(Constants.kSlot_Velocit, Constants.PID_PRIMARY);
			}
			
            /* Calculate targets from gamepad inputs */
            
            boolean go = true;

            double target_RPM;

            if (!rpm_set_mode){
                target_RPM = forward * max_rpm;	// +- 1000 RPM?
                if (forward == 0) {
                   go = false;
                }
            }
            else {
                if (set_rpm < 1000) {
                    go = false;
                }
                
                target_RPM = set_rpm/2.1;
            }


			double target_unitsPer100ms = target_RPM * Constants.kSensorUnitsPerRotation / 600.0;	//RPM -> Native units
			double feedFwdTerm = turn * 0.10;	// Percentage added to the close loop output
            
            if (go) {
            
                /* Configured for Velocity Closed Loop on Quad Encoders' Sum and Arbitrary FeedForward on joyX */
                _rightMaster.set(ControlMode.Velocity, target_unitsPer100ms, DemandType.ArbitraryFeedForward, feedFwdTerm);
         } else {
                _rightMaster.set(ControlMode.PercentOutput, 0);
            }

            _leftMaster.follow(_rightMaster);

			

		}
        _firstCall = false;
        //System.out.println("Right RPM: " + _rightMaster.getSelectedSensorVelocity() + "\n Left RPM: " + _leftMaster.getSelectedSensorVelocity());
        //System.out.println("Forward:" + forward);

        /* Uncomment to view RPM in Driver Station */
        actual_RPM = (_leftMaster.getSelectedSensorVelocity() / (double)Constants.kSensorUnitsPerRotation * -600f); //4/3 scaling factor
        // System.out.println("Vel[RPM]: " + actual_RPM);


    }

    	/* Zero all sensors on Talons */
	private void zeroSensors() {
		_leftMaster.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
		_rightMaster.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
		System.out.println("[Quadrature Encoders] All sensors are zeroed.\n");
	}
	
	/** Deadband 5 percent, used on the gamepad (To be added to Framework?) */
	private double Deadband(double value) {
		/* Upper deadband */
		if (value >= +0.05) 
			return value;
		
		/* Lower deadband */
		if (value <= -0.05)
			return value;
		
		/* Outside deadband */
		return 0;
	}
	
	/** Gets all buttons from gamepad */
	// private void getButtons(boolean[] btns, Joystick gamepad) {
	// 	for (int i = 1; i < Constants.kNumButtonsPlusOne; ++i) {
	// 		btns[i] = gamepad.getRawButton(i);
	// 	}
    // }
    
    private double getForward() {
        double out;
        if (throttleType == false) {
            out = -1 * _gamepad.getRawAxis(axis);
        }
        else {
            out = (((_gamepad.getRawAxis(axis) * -1) +1)/2);
        }

        out = Math.pow(out, 0.25);

        return out;

    }






    
}
