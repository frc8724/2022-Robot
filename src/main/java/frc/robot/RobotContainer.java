// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.mayheminc.util.*;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autos.PointToBall;
import frc.robot.commands.DriveBaseTeleopCommand;
import frc.robot.commands.HoodMove;
import frc.robot.commands.ShooterAdjustShooterWheel;
import frc.robot.commands.ShooterSetAccelerator;
import frc.robot.commands.SystemZero;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveBaseSubsystem;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.DriveBaseSubsystem;
import frc.robot.utils.SettableSendableChooser;
import frc.robot.Vision;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Operator Inputs
  public static final MayhemDriverStick DRIVER_STICK = new MayhemDriverStick();
  public static final MayhemDriverPad DRIVER_PAD = new MayhemDriverPad();
  public static final MayhemOperatorPad OPERATOR_PAD = new MayhemOperatorPad();
  private final MayhemOperatorStick OPERATOR_STICK = new MayhemOperatorStick();

  // The robot's subsystems and commands are defined here...
  private final DriveBaseSubsystem drive = new DriveBaseSubsystem();
  // private final Intake intake = new Intake();
  // private final Magazine magazine = new Magazine();
  // private final Climber climber = new Climber();
  public static final Shooter shooter = new Shooter();
  public static final Hood hood = new Hood();

  public static final PidTuner pidTuner = new PidTuner(
      DRIVER_STICK.DRIVER_STICK_BUTTON_FIVE,
      DRIVER_STICK.DRIVER_STICK_BUTTON_SIX,
      DRIVER_STICK.DRIVER_STICK_BUTTON_SEVEN,
      DRIVER_STICK.DRIVER_STICK_BUTTON_EIGHT, shooter);

  private final SettableSendableChooser<Command> autoChooser = new SettableSendableChooser<>();

  // public static final Vision vision = new Vision();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    System.out.println("RobotContainer const");

    this.autoChooser.setDefaultOption("hello world", new PointToBall(this.drive));

    SmartDashboard.putData("Auto selector", this.autoChooser);

    // Configure the button bindings
    configureButtonBindings();

    // vision.init();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    configureDriverPadButtons();
    configureOperatorPadButtons();
    configureDriverStick();

  }

  private void configureDriverStick() {
    DRIVER_STICK.DRIVER_STICK_BUTTON_ONE_DISABLED.whenPressed(new SystemZero());
  }

  private void configureOperatorPadButtons() {
    System.out.println("Operator Pad Buttons.");

    OPERATOR_PAD.OPERATOR_PAD_BUTTON_ONE.whenPressed(new ShooterSetAccelerator(0.0));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_TWO.whenPressed(new ShooterSetAccelerator(0.10));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_THREE.whenPressed(new ShooterSetAccelerator(0.20));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_FOUR.whenPressed(new ShooterSetAccelerator(0.30));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_FIVE.whenPressed(new ShooterSetAccelerator(0.40));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_SIX.whenPressed(new ShooterSetAccelerator(0.50));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_SEVEN.whenPressed(new ShooterSetAccelerator(0.60));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_EIGHT.whenPressed(new ShooterSetAccelerator(0.70));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_NINE.whenPressed(new ShooterSetAccelerator(0.80));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_TEN.whenPressed(new ShooterSetAccelerator(0.90));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_ELEVEN.whenPressed(new ShooterSetAccelerator(1.0));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_TWELVE.whenPressed(new ShooterSetAccelerator(0.0));

    OPERATOR_PAD.OPERATOR_PAD_D_PAD_UP.whenPressed(new ShooterAdjustShooterWheel(100.0));
    OPERATOR_PAD.OPERATOR_PAD_D_PAD_DOWN.whenPressed(new ShooterAdjustShooterWheel(-100.0));

    OPERATOR_PAD.OPERATOR_PAD_D_PAD_LEFT.whenPressed(new HoodMove(2000));
    OPERATOR_PAD.OPERATOR_PAD_D_PAD_RIGHT.whenPressed(new HoodMove(3000));

  }

  private void configureDriverPadButtons() {
    DRIVER_PAD.DRIVER_PAD_RED_BUTTON.whenPressed(new PrintCommand("DRIVER PAD RED"));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return this.autoChooser.getSelected();
  }

  public Command getTeleopCommand() {
    return new DriveBaseTeleopCommand(this.drive, DRIVER_PAD);
  }
}
