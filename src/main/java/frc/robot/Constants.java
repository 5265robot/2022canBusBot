/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.XboxController;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
    // on / off switches
    public static boolean currentIntakeState = false;
    public static boolean cameraState = false;
    public static boolean armExtended = false;
    public static boolean winchState = false;
	public static boolean powerState = false;
	
    // XBox mappings
    /*  kBumperLeft(5)  --> 
        kBumperRight(6) --> arm up
        kStickLeft(9)   --> conveyor empty
        kStickRight(10) --> 
        kA(1)           --> intake
        kB(2)           --> 
        kX(3)           --> disable climbarm
        kY(4)           --> switch camera
        kBack(7)        --> intake reverse
        kStart(8)       --> half speed drive
    */
    public static int kIntakeButton = XboxController.Button.kA.value;   
    public static int kConveyorPulseButton = XboxController.Button.kB.value;   
    public static int kClimbOffButton = XboxController.Button.kX.value;
    public static int kSwitchCameraButton = XboxController.Button.kY.value;   
    public static int kSlowDown = XboxController.Button.kStart.value;
    public static int kArmUp = XboxController.Button.kRightBumper.value;
	public static int kIntakeReverseButton = XboxController.Button.kBack.value;
    public static int kConveyorEmptyButton = XboxController.Button.kLeftStick.value;
    
    // thrustmaster buttons
	public static int kThrustPulseButton = 9;

    
    public final class OIConstants{
        public static final int kDriverControllerPort = 0;
        public static final int kTrustMasterPort = 1;
        // d-pad positions
        public static final int kClimbArmUp = 0;
        public static final int kClimbArmExtend = 90;
        public static final int kWinchOnToggle = 180;
        public static final int kClimbArmDownToggle = 270;
		
    }
    public final class AutoConstants {
        public static final double kTimeOut = 1.0;
		public static final double kPower = -0.8;
    }
    public final class DriveConstants{
        public static final int kLeftMotor00CanBusID = 10;
        public static final int kLeftMotor01CanBusID = 11;
        public static final int kRightMotor02CanBusID = 12;
        public static final int kRightMotor03CanBusID = 13;
        public static final double kSlowMaxSpeed = 0.5;
		public static final double kMaxSpeed = 0.6;
    }
    public final class ShooterConstants{
        public static final int kArmMotorPWM = 2;
        public static final int kUpperMotorPWM = 4;
        public static final int kShooterMotorPWM = 1;
        public static final double kArmPower = 0.4; // 0.3
        public static final double kIntakePower = 0.4;
        public static final double kConveyorPulsePower = -0.4;
		public static final double kConveyorFullPower = -0.6; //-0.6
		public static final double kConveyorFiveBallEmpty = 2.0;
        public static final double kConveyorOneBallPulse = 0.13;
		public static final double kUpperLowerRatio = 1.5; // original 1.0, changed to put top spin on ball
        public static final double kLowerTime = 0.15; //0.13
        public static final double kUpperTime = 0.15;
        /*
        public static final double kUpperPower = -0.6;
        public static final double kLowerPower = -0.6; // -0.4 upped for arm passage
		*/
    }

}
