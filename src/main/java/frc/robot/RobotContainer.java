// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.mayheminc.util.*;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ClimberSetArmLengthPowerTo;
import frc.robot.commands.ClimberSetArmPositionTo;
// import frc.robot.autos.PointToTarget;
import frc.robot.commands.DriveBaseTeleopCommand;
import frc.robot.commands.HoodAdjust;
import frc.robot.commands.IntakePistonsSet;
import frc.robot.commands.IntakeReverseRollers;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.LoaderSetInstant;
import frc.robot.commands.LoaderSetSpeed;
import frc.robot.commands.MagazineSetSpeed;
import frc.robot.commands.ShooterAdjustShooterWheel;
import frc.robot.commands.ShooterSetAccelerator;
import frc.robot.commands.SystemClimberAttachToNextRung;
import frc.robot.commands.SystemClimberInitialClimb;
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
import edu.wpi.first.wpilibj2.command.Command;

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
  public static final DriveBaseSubsystem drive = new DriveBaseSubsystem();
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

  // public static final Vision vision = new Vision();

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

    // vision.init();
    // vision.start(new TargetVisionModel());

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
    // piston down d-pad down and up
    // climb to first rung. btn 9
    // climb to next rung. btn 10

    // intake down - btn 2
    // intake up - btn 4
    // intake rollers in. btn 6
    // intake rollers out. btn 8

    // hood adjust btn d-pad left and right

    OPERATOR_PAD.OPERATOR_PAD_BUTTON_TWO.whenPressed(new IntakePistonsSet(IntakePistons.INTAKE_DOWN));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_FOUR.whenPressed(new IntakePistonsSet(IntakePistons.INTAKE_UP));

    OPERATOR_PAD.OPERATOR_PAD_BUTTON_SIX.whenHeld(new IntakeSetRollers());
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_EIGHT.whenHeld(new IntakeReverseRollers());

    OPERATOR_PAD.OPERATOR_PAD_BUTTON_NINE.whenPressed(new SystemClimberInitialClimb());
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_TEN.whenPressed(new SystemClimberAttachToNextRung());

    OPERATOR_PAD.OPERATOR_PAD_D_PAD_UP.whenPressed(new ClimberSetArmPositionTo(Climber.ARMS_UP));
    OPERATOR_PAD.OPERATOR_PAD_D_PAD_DOWN.whenPressed(new ClimberSetArmPositionTo(Climber.ARMS_DOWN));

    OPERATOR_PAD.OPERATOR_PAD_D_PAD_LEFT.whenPressed(new HoodAdjust(-200));
    OPERATOR_PAD.OPERATOR_PAD_D_PAD_RIGHT.whenPressed(new HoodAdjust(200));
  }

  private void configureDriverPadButtons() {
    DRIVER_PAD.DRIVER_PAD_RED_BUTTON.whileHeld(new SystemShootBall());
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
    return new DriveBaseTeleopCommand(RobotContainer.drive, DRIVER_PAD);
  }

}
