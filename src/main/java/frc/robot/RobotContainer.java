// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.mayheminc.util.*;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autoroutines.ShootAndMoveForward;
// import frc.robot.autos.PointToTarget;
import frc.robot.commands.DriveBaseTeleopCommand;
import frc.robot.commands.HoodAdjust;
import frc.robot.commands.HoodMove;
import frc.robot.commands.IntakeMoveTo;
import frc.robot.commands.IntakePistonsSet;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.LoaderSetInstant;
import frc.robot.commands.LoaderSetSpeed;
import frc.robot.commands.MagazineSetSpeed;
import frc.robot.commands.ShooterAdjustShooterWheel;
import frc.robot.commands.ShooterSetAccelerator;
import frc.robot.commands.SystemIntakeBalls;
import frc.robot.commands.SystemShootBall;
import frc.robot.commands.SystemZero;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveBaseSubsystem;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.IntakePistons;
import frc.robot.subsystems.IntakeRollers;
import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterAccelerator;
import frc.robot.utils.SettableSendableChooser;
import frc.robot.vision.Vision;
import frc.robot.vision.models.TargetVisionModel;
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
  @SuppressWarnings("PMD.UnusedPrivateField")
  private final MayhemOperatorStick OPERATOR_STICK = new MayhemOperatorStick();

  // The robot's subsystems and commands are defined here...
  private final DriveBaseSubsystem drive = new DriveBaseSubsystem();
  public static final IntakeRollers intakeRollers = new IntakeRollers();
  public static final IntakePistons intakePistons = new IntakePistons();
  public static final Magazine magazine = new Magazine();
  public static final Loader loader = new Loader();
  public static final Shooter shooter = new Shooter();
  public static final ShooterAccelerator accelerator = new ShooterAccelerator();
  public static final Hood hood = new Hood();
  public static final Climber climber = new Climber();

  public static final PidTuner pidTuner = new PidTuner(
      DRIVER_STICK.DRIVER_STICK_BUTTON_FIVE,
      DRIVER_STICK.DRIVER_STICK_BUTTON_SIX,
      DRIVER_STICK.DRIVER_STICK_BUTTON_SEVEN,
      DRIVER_STICK.DRIVER_STICK_BUTTON_EIGHT, shooter);

  private final SettableSendableChooser<Command> autoChooser = new SettableSendableChooser<>();

  public static final Vision vision = new Vision();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    System.out.println("RobotContainer const");

    // this.autoChooser.setDefaultOption("hello world", new
    // PointToTarget(this.drive));


    SmartDashboard.putData("Auto selector", this.autoChooser);

    // Configure the button bindings
    configureButtonBindings();

    vision.init();
    vision.start(new TargetVisionModel());

    SmartDashboard.putNumber("r1", 0);
    SmartDashboard.putNumber("r2", 0);
    SmartDashboard.putNumber("g1", 0);
    SmartDashboard.putNumber("g2", 0);
    SmartDashboard.putNumber("b1", 0);
    SmartDashboard.putNumber("b2", 0);
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

    OPERATOR_PAD.OPERATOR_PAD_BUTTON_ONE.whenPressed(new IntakePistonsSet(IntakePistons.INTAKE_UP));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_TWO.whenPressed(new LoaderSetInstant(0.50));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_THREE.whenHeld(new MagazineSetSpeed(0.50, false));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_FOUR.whenPressed(new IntakePistonsSet(IntakePistons.INTAKE_DOWN));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_FIVE.whenHeld(new IntakeSetRollers());
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_SIX.whenPressed(new ShooterSetAccelerator(0.50));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_SEVEN.whenHeld(new SystemIntakeBalls());
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_EIGHT.whenPressed(new SystemShootBall());

    OPERATOR_PAD.OPERATOR_PAD_D_PAD_UP.whenPressed(new ShooterAdjustShooterWheel(100.0));
    OPERATOR_PAD.OPERATOR_PAD_D_PAD_DOWN.whenPressed(new ShooterAdjustShooterWheel(-100.0));

    OPERATOR_PAD.OPERATOR_PAD_D_PAD_LEFT.whenPressed(new HoodAdjust(-200));
    OPERATOR_PAD.OPERATOR_PAD_D_PAD_RIGHT.whenPressed(new HoodAdjust(200));

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
