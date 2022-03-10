/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;

import java.lang.reflect.Executable;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.Dunks;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // drn -- robot's subsystems and commands are defined here...

  // drn -- drive & intake & arm subsystem declarations
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();

  private final ShooterSubsystem m_shooter = new ShooterSubsystem();


  /*
   * removed for CAN issue private final IntakeSubsystem m_intake = new
   * IntakeSubsystem();
   */
  // drn --- declaring an instance of the XBox controller
  private final XboxController m_xboxController = new XboxController(OIConstants.kDriverControllerPort);
  //private final Joystick m_thrustMaster = new Joystick(OIConstants.kTrustMasterPort);

  // drn -- A chooser for autonomous commands
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();

  // drn -- shuffleboard tabs
  private final ShuffleboardTab sbConfig = Shuffleboard.getTab("Config");

  // Camera
  private UsbCamera camera01;
  private UsbCamera camera02;
  private VideoSink videoServer;

  // drn -- while driving diagnostics
  public final PowerDistribution pdp = new PowerDistribution(0,ModuleType.kCTRE);
  // drn -- simple autonomous driving
  // use ()-> to specify the start and end commands
  private final Command m_simpleDriveForward = new RunCommand(() -> m_robotDrive.arcadeDrive(AutoConstants.kPower, 0.0),
      m_robotDrive).withTimeout(AutoConstants.kTimeOut);

  private final Command m_simpleDriveReverse = new StartEndCommand(() -> m_robotDrive.arcadeDrive(-AutoConstants.kPower, 0.0), () -> m_robotDrive.arcadeDrive(0.0, 0.0),
      m_robotDrive).withTimeout(AutoConstants.kTimeOut);

  private final Command m_simpleShoot = new RunCommand(() -> m_shooter.intakeOn(-ShooterConstants.kIntakePower, false)); //needs to be false
  
  

  
/*
  private Command AutoDunk(DriveSubsystem drive, ShooterSubsystem intake) {
    addCommands(
        new RunCommand(() -> m_shooter.intakeOn(-ShooterConstants.kIntakePower, false), m_shooter),
        new RunCommand(() -> m_robotDrive.arcadeDrive(-AutoConstants.kPower, 0.0), m_robotDrive)
    );
}
*/
/*
   m_simpleShoot(() -> m_shooterMotor.intakeOn(ShooterConstants.kIntakePower, true), m_shooterMotor), 
   m_robotDrive.arcadeDrive(() -> m_robotDrive.arcadeDrive(-AutoConstants.kPower, 0.0), () -> m_robotDrive.arcadeDrive(0.0, 0.0),
   m_robotDrive).withTimeout(AutoConstants.kTimeOut)
    .withTimeout(AutoConstants.kTimeOut));*/
 
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    // prepare the camera
    // removed for characterization
    cameraInit();

    // drive command to split-stick arcade drive
    // split stick is left and right sticks on the XBox
    

    m_robotDrive
        .setDefaultCommand(new RunCommand(() -> m_robotDrive.arcadeDrive(-m_xboxController.getLeftX(),
            -m_xboxController.getRightY()), m_robotDrive));

    // drn -- sets up the driver's station to have options for autonomous
    m_chooser.addOption("Forward Auto", m_simpleDriveForward);
    m_chooser.addOption("Reverse Auto", m_simpleDriveReverse);
    m_chooser.addOption("Shoot Auto", m_simpleShoot);
    m_chooser.addOption("Auto Dunks", new Dunks(m_robotDrive, m_shooter));
    m_chooser.setDefaultOption("Forward Auto", m_simpleDriveForward);
    sbConfig.add(m_chooser).withSize(3, 1).withPosition(0, 0);

    // drn -- put power onto shuffleboard
    sbConfig.add("PDP voltage", pdp.getVoltage()).withSize(1, 1).withPosition(8, 0);
    // drn -- put camera on shuffleboard
    // sbConfig.add(camera01).withSize(6, 5).withPosition(2, 0);

  } // end RobotContainer initialization methods

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // arm
    final JoystickButton armUp = new JoystickButton(m_xboxController, Constants.kArmUp);
    armUp.whenPressed(() -> m_shooter.armUp(ShooterConstants.kArmPower, Constants.armUp));
    final JoystickButton armDown = new JoystickButton(m_xboxController, Constants.kArmDown);
    armDown.whileHeld(() -> m_shooter.armUp(-ShooterConstants.kArmPower, Constants.armUp));

    // intake/outake
    final JoystickButton intakeOn = new JoystickButton(m_xboxController, Constants.kIntakeButton);
    intakeOn.whenPressed(() -> m_shooter.intakeOn(ShooterConstants.kIntakePower, Constants.currentIntakeState)); //need to test on/off and length of on time
    final  JoystickButton intakeReverse = new JoystickButton(m_xboxController, Constants.kIntakeReverseButton);
    intakeReverse.whenPressed(() -> m_shooter.intakeOn(-ShooterConstants.kOutTakePower, Constants.currentIntakeState));


/*
    /* conveyor
    final JoystickButton conveyorButton = new JoystickButton(m_xboxController, Constants.kConveyorPulseButton);
    conveyorButton.whileHeld(new RunCommand(() -> m_intake.upperAndLowerOn(IntakeConstants.kConveyorPulsePower))
        .withTimeout(IntakeConstants.kConveyorOneBallPulse).andThen(new RunCommand(() -> m_intake.upperAndLowerOff())));

    final JoystickButton conveyorFiveBallEmpty = new JoystickButton(m_xboxController, Constants.kConveyorEmptyButton);
    conveyorFiveBallEmpty.whenPressed(() -> m_intake.upperAndLowerOn(IntakeConstants.kConveyorFullPower))
        .whenReleased(() -> m_intake.upperAndLowerOff());

    final JoystickButton conveyorThrustButton = new JoystickButton(m_thrustMaster, Constants.kThrustPulseButton);
    conveyorThrustButton.whileHeld(new RunCommand(() -> m_intake.upperAndLowerOn(IntakeConstants.kConveyorPulsePower))
        .withTimeout(IntakeConstants.kConveyorOneBallPulse).andThen(new RunCommand(() -> m_intake.upperAndLowerOff())));
*/
    /*
     * final (new StartEndCommand(() ->
     * m_intake.upperAndLowerOn(IntakeConstants.kConveyorFullPower))
     * .withTimeout(IntakeConstants.kConveyorFiveBallEmpty) ->
     * m_intake.upperAndLowerOff());
     * 
     * this button times out when held, runs constantly when tapped
     * testMotorButton.whenHeld(new RunCommand(() ->
     * m_miscDrive.misc01Drive(MiscConstants.kmisc01Power)) .withTimeout(0.3)
     * .andThen(new RunCommand(() -> m_miscDrive.misc01Drive(0.0))));
     * 
     * conveyorOneBallPulse.whenPressed(new RunCommand(() ->
     * m_intake.upperAndLowerOn(IntakeConstants.kLowerPower))
     * .withTimeout(IntakeConstants.kConveyorOneBallPulse) .andThen(new
     * RunCommand(() -> m_intake.upperAndLowerOff())));
     * 
     * conveyorFiveBallEmpty.whenPressed(new RunCommand(() ->
     * m_intake.upperAndLowerOn(IntakeConstants.kLowerPower))
     * .withTimeout(IntakeConstants.kConveyorFiveBallEmpty) .andThen(new
     * RunCommand(() -> m_intake.upperAndLowerOff())));
     */

    //camera
    final JoystickButton switchCamera = new JoystickButton(m_xboxController, Constants.kSwitchCameraButton);
    switchCamera.whenPressed(() -> cameraSwitch());

    /* arm
    // D-Pad UP
    final POVButton climbArmUp = // raises arm via PID
        new POVButton(m_xboxController, OIConstants.kClimbArmUp);
    climbArmUp.whenPressed(new InstantCommand(m_armPID::enable, m_armPID));
    // D-Pad RIGHT
    final POVButton climbArmExtend = // extends elbow
        new POVButton(m_xboxController, OIConstants.kClimbArmExtend);
    climbArmExtend.whenPressed(() -> m_armPID.fireElbow(), m_armPID);
    // release arm and webbing
    final JoystickButton releaseArmAndWebbing = new JoystickButton(m_xboxController, Constants.kElbowExtend);
    releaseArmAndWebbing.whenPressed(() -> m_armPID.firePinch());
    // D-Pad DOWN
    final POVButton winchOn = // toggle that turns the winch on
        new POVButton(m_xboxController, OIConstants.kWinchOnToggle);
    winchOn.whenPressed(() -> m_armPID.winchOn(ArmConstants.kWinchPower));
    // D-Pad LEFT
    final POVButton climbArmDown = // toggle that drops the arm
        new POVButton(m_xboxController, OIConstants.kClimbArmDownToggle);
    climbArmDown.whenPressed(new InstantCommand(m_armPID::armDown, m_armPID).withTimeout(0.5));
    // kX button
    final JoystickButton climbArmOff = // stops PID, resets
        new JoystickButton(m_xboxController, Constants.kClimbOffButton);
    climbArmOff.whenPressed(() -> m_armPID.testArmOff());
*/
    // speed
    final JoystickButton slowDown = new JoystickButton(m_xboxController, Constants.kSlowDown);
    slowDown.whenPressed(() -> m_robotDrive.halfPower());

    /*
     * .whenPressed(() -> m_robotDrive.setMax(DriveConstants.kSlowMaxSpeed))
     * .whenReleased(() -> m_robotDrive.setMax(DriveConstants.kMaxSpeed));
     */

  } // end configureButtonBindins

  // Starting and adjusting camera
  private void cameraInit() {
    camera01 = CameraServer.startAutomaticCapture(0);
    videoServer = CameraServer.getServer();
    camera01.setResolution(320, 240);
    camera01.setFPS(15);
    videoServer.setSource(camera01);
  } // end cameraInit

  // toggle switch for changing cameras
  private void cameraSwitch() {
    if (Constants.cameraState) {
      videoServer.setSource(camera01);
    } else {
      videoServer.setSource(camera02);
    }
    Constants.cameraState = !Constants.cameraState;
  } // end cameraSwitch

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_chooser.getSelected();
  } // end getAutonomous

} // end RobotContainer
