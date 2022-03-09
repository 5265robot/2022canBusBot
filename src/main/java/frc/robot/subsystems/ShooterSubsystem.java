/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.playingwithfusion.CANVenom;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;

/**
 * Add your docs here.
 */
public class ShooterSubsystem extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private final Spark m_intakeSpark = new Spark(ShooterConstants.kShooterMotorPWM);
  private final CANVenom m_arm = new CANVenom(ShooterConstants.kArmMotorPWM);
  private final Spark m_upperSpark = new Spark(ShooterConstants.kUpperMotorPWM);

  public ShooterSubsystem() {
    // initialization methods here
  }

  // on/off switch for the intake
  public void intakeOn(double power, boolean state){
    if (!state) {
      m_intakeSpark.set(power);}
    else m_intakeSpark.set(0.0);
    Constants.currentIntakeState = !state;
  }

  // powers the upper conveyor belt
  public void upperOn(double power){
    m_upperSpark.set(power);
  }
  // raises lift
  public void armUp(double power){
    m_arm.set(power);
  }


  /*

  // powers both conveyor belts,
  // giving the upper belt a bit more power
  public void upperAndLowerOn(double power){
    // can we put a timeout here
    upperOn(power*ShooterConstants.kUpperLowerRatio);
    armUp(power);
  }
  // turns off the conveyor belts
  public void upperAndLowerOff(){
    upperOn(0.0);
    armUp(0.0);
  }

  */

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
