package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Joystick;

public class driveMotorVelocity {

    static final Gains kGains = new Gains(-0.1, 0.0, 0, 0, 0, 1.0);

    TalonSRX left;
    TalonSRX right;
    VictorSPX leftFollow;
    VictorSPX rightFollow;
    Joystick joy;
    Joystick nudge;
    int channel;

    boolean _firstCall;
    boolean _state;


    driveMotorVelocity(TalonSRX left_in, TalonSRX right_in, VictorSPX left_follow_in,
        VictorSPX right_follow_in,Joystick joy_in, Joystick nudge_in, int channel_in) {

        left = left_in;
        right = right_in;
        leftFollow = left_follow_in;
        rightFollow = right_follow_in;
        joy = joy_in;
        nudge = nudge_in;
        channel = channel_in;

        left.set(ControlMode.PercentOutput, 0);
        right.set(ControlMode.PercentOutput, 0);
        leftFollow.set(ControlMode.PercentOutput, 0);
        rightFollow.set(ControlMode.PercentOutput, 0);


        /* Factory Default all hardware to prevent unexpected behaviour */
        right.configFactoryDefault();
        left.configFactoryDefault();
        
        /* Set neutral modes */
        left.setNeutralMode(NeutralMode.Brake);
        right.setNeutralMode(NeutralMode.Brake);
        
        /** Feedback Sensor Configuration */
        
        /* Configure the left Talon's selected sensor as local QuadEncoder */
        left.configSelectedFeedbackSensor(	FeedbackDevice.QuadEncoder,				// Local Feedback Source
                                                    Constants.PID_PRIMARY,					// PID Slot for Source [0, 1]
                                                    Constants.kTimeoutMs);					// Configuration Timeout

        /* Configure the Remote Talon's selected sensor as a remote sensor for the right Talon */
        right.configRemoteFeedbackFilter(left.getDeviceID(),					// Device ID of Source
                                                RemoteSensorSource.TalonSRX_SelectedSensor,	// Remote Feedback Source
                                                Constants.REMOTE_0,							// Source number [0, 1]
                                                Constants.kTimeoutMs);						// Configuration Timeout
        
        /* Setup Sum signal to be used for Velocity */
        right.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, Constants.kTimeoutMs);     // Feedback Device of Remote Talon
        right.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, Constants.kTimeoutMs);       // Quadrature Encoder of current Talon
        
        /* Configure Sum [Sum of both QuadEncoders] to be used for Primary PID Index */
        right.configSelectedFeedbackSensor(	FeedbackDevice.SensorSum, 
                                                    Constants.PID_PRIMARY,
                                                    Constants.kTimeoutMs);
        
        /* Scale Feedback by 0.5 to half the sum of Velocity */
        right.configSelectedFeedbackCoefficient( 1, 						// Coefficient
                                                        Constants.PID_PRIMARY,		// PID Slot of Source 
                                                        Constants.kTimeoutMs);		// Configuration Timeout
        
        /* Configure output and sensor direction */
        left.setInverted(true);
        left.setSensorPhase(false);
        right.setInverted(true);
        right.setSensorPhase(true);
        leftFollow.setInverted(true);
        rightFollow.setInverted(true);

        /* Set status frame periods to ensure we don't have stale data */
        right.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, Constants.kTimeoutMs);
        right.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 40, Constants.kTimeoutMs);
        left.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, Constants.kTimeoutMs);

        /* Configure neutral deadband */
        right.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);
        left.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);

        /**
         * Max out the peak output (for all modes).  
         * However you can limit the output of a given PID object with configClosedLoopPeakOutput().
         */
        left.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
        left.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);
        right.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
        right.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);
        
        /* FPID Gains for velocity servo */
        right.config_kP(Constants.kSlot_Velocit, kGains.kP, Constants.kTimeoutMs);
        right.config_kI(Constants.kSlot_Velocit, kGains.kI, Constants.kTimeoutMs);
        right.config_kD(Constants.kSlot_Velocit, kGains.kD, Constants.kTimeoutMs);
        right.config_kF(Constants.kSlot_Velocit, kGains.kF, Constants.kTimeoutMs);
        right.config_IntegralZone(Constants.kSlot_Velocit, kGains.kIzone, Constants.kTimeoutMs);
        right.configClosedLoopPeakOutput(Constants.kSlot_Velocit, kGains.kPeakOutput, Constants.kTimeoutMs);
        right.configAllowableClosedloopError(Constants.kSlot_Velocit, 0, Constants.kTimeoutMs);
            
        /**
         * 1ms per loop.  PID loop can be slowed down if need be.
         * For example,
         * - if sensor updates are too slow
         * - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
         * - sensor movement is very slow causing the derivative error to be near zero.
         */
        int closedLoopTimeMs = 4;
        right.configClosedLoopPeriod(0, closedLoopTimeMs, Constants.kTimeoutMs);
        right.configClosedLoopPeriod(1, closedLoopTimeMs, Constants.kTimeoutMs);

        // Allowable Error
        // right.configClosedLoop


        /* Initialize */
        _firstCall = true;
        _state = false;
        zeroSensors();

                
    }

    public boolean rpm_set_mode = true;
    private double max_rpm = 100;
    public double actual_RPM;
    public double set_rpm;

    public void velocityControlPeriodic(double tx) {
		/* Gamepad processing */
		double forward = getForward();
		double turn = getTurn();

            
		/* Button processing for state toggle and sensor zeroing */
        boolean _state = false;
        
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
            if (set_rpm < 0) {
                go = false;
            }
            
            target_RPM = set_rpm/2.1;
        }


        double target_unitsPer100ms = target_RPM * 1024 / 600.0;	//RPM -> Native units
        double feedFwdTerm = turn * 0.10;	// Percentage added to the close loop output
        
        if (Math.round(nudge.getRawAxis(channel)) != 0) {
            _state = true;
        } 
        else {
            _state = false;
        }
        if ((tx > 1.25 || tx < 0) && joy.getRawButton(8)){
            _state = true;

        }


        if (_state) {
            if((nudge.getRawAxis(channel) == -1) || tx < 0) {
                left.set(ControlMode.PercentOutput, -0.15);
                right.set(ControlMode.PercentOutput, 0.15);
            } 
            else if ((nudge.getRawAxis(channel) == +1) || tx > 1.25) {
                left.set(ControlMode.PercentOutput,   0.15);
                right.set(ControlMode.PercentOutput, -0.15);
            }

        }
		else{
			if (_firstCall) {
				// System.out.println("This is Arcade drive.\n");
			
			left.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn);
			right.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, -turn);
			
			/* Uncomment to view velocity native units */
		}else if (_firstCall) {
				// System.out.println("This is Velocity Closed Loop with an Arbitrary Feed Forward.");
				// System.out.println("Travel [-500, 500] RPM while having the ability to add a FeedForward with joyX ");
				
				/* Determine which slot affects which PID */
                right.selectProfileSlot(Constants.kSlot_Velocit, Constants.PID_PRIMARY);
                right.set(ControlMode.Velocity, target_unitsPer100ms, DemandType.ArbitraryFeedForward, feedFwdTerm);
                
                left.follow(right);

			}
			

		}
        
        rightFollow.follow(right);
        leftFollow.follow(left);
        
        
        //System.out.println("Right RPM: " + right.getSelectedSensorVelocity() + "\n Left RPM: " + left.getSelectedSensorVelocity());
        //System.out.println("Forward:" + forward);

        /* Uncomment to view RPM in Driver Station */
        actual_RPM = (left.getSelectedSensorVelocity() / (double)1024 * -600f); //4/3 scaling factor
        double actual_RPM2 = actual_RPM - (right.getSelectedSensorVelocity() / (double)1024 * -600f); //4/3 scaling factor
        
        // System.out.println("Vel[RPM]: " + actual_RPM + "," + actual_RPM2);
        // System.out.println("Vel[RPM]: " + actual_RPM2);


    }


    private double getForward() {
        double scale;
        if (joy.getRawButton(1)) {
            scale = 1;
        } else {
            scale = 0.6;
        }
        double forward = -1 * scale * Deadband(joy.getY());

        
        return forward;
    }

    private double getTurn() {
        double turn = 0.4 * Deadband(joy.getTwist());


        return turn;
    }

    private void zeroSensors() {
		left.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
		right.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
		System.out.println("[Quadrature Encoders] All sensors are zeroed.\n");
	}
    

    private double Deadband(double value) {
		/* Upper deadband */
		if (value >= +0.1) 
			return value;
		
		/* Lower deadband */
		if (value <= -0.1)
			return value;
		
		/* Outside deadband */
		return 0;
	}
}
