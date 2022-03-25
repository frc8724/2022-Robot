// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.mayheminc.util.*;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autoroutines.DoNothing;
import frc.robot.autoroutines.DriveForwardShoot2;
import frc.robot.autoroutines.DriveStraightDoNothing;
import frc.robot.autoroutines.FiveBallPath;
import frc.robot.autoroutines.LeftSide3Balls;
import frc.robot.autoroutines.Shoot;
import frc.robot.autoroutines.ShootAndMoveForward;
import frc.robot.autoroutines.TestAutoTargeting;
import frc.robot.autoroutines.ThreeBallPath;
import frc.robot.commands.ClimberExtendFastBottom;
import frc.robot.commands.ClimberExtendFastTop;
import frc.robot.commands.ClimberSetArmLengthTo;
import frc.robot.commands.ClimberSetArmPositionTo;
import frc.robot.commands.ClimberSetVelocity;
import frc.robot.commands.DriveBaseTeleopCommand;
import frc.robot.commands.DriveStraightOnHeading;
import frc.robot.commands.HoodAdjust;
import frc.robot.commands.IntakePistonsSet;
import frc.robot.commands.IntakeReverseRollers;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.SystemShootBall;
import frc.robot.commands.SystemZero;
import frc.robot.subsystems.CameraLights;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ClimberPistons;
import frc.robot.subsystems.DriveBaseSubsystem;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.IntakePistons;
import frc.robot.subsystems.IntakeRollers;
import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.RobotLights;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterAccelerator;
import frc.robot.subsystems.Targeting;
import frc.robot.utils.SettableSendableChooser;
import frc.robot.vision.Vision;
import frc.robot.vision.models.BlueBallVisionModel;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

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
  // private final MayhemOperatorStick OPERATOR_STICK = new MayhemOperatorStick();

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
  public static final ClimberPistons climberPistons = new ClimberPistons();
  public static final CameraLights cameraLights = new CameraLights();
  public static final RobotLights robotLights = new RobotLights();
  public static final Targeting targeting = new Targeting();

  public static final PidTuner pidTuner = new PidTuner(
      DRIVER_STICK.DRIVER_STICK_BUTTON_FIVE,
      DRIVER_STICK.DRIVER_STICK_BUTTON_SIX,
      DRIVER_STICK.DRIVER_STICK_BUTTON_SEVEN,
      DRIVER_STICK.DRIVER_STICK_BUTTON_EIGHT, shooter);

  private final SettableSendableChooser<Command> autoChooser = new SettableSendableChooser<>();

  public static final Vision vision = new Vision(1);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // this.autoChooser.setDefaultOption("Test Auto Target", new
    // TestAutoTargeting());
    this.autoChooser.setDefaultOption("Drive Straight", new DriveStraightDoNothing());
    this.autoChooser.addOption("5 Ball Auto", new FiveBallPath());
    // this.autoChooser.setDefaultOption("2 Ball Auto", new TwoBallPath(true));
    this.autoChooser.addOption("3 Ball Auto", new ThreeBallPath(true));

    this.autoChooser.addOption("Drive Forward Shoot 2", new DriveForwardShoot2());
    this.autoChooser.addOption("Left Side 3 balls.", new LeftSide3Balls());

    this.autoChooser.addOption("Shoot and Move Forward again", new ShootAndMoveForward());

    this.autoChooser.addOption("Do nothing!", new DoNothing());
    // this.autoChooser.addOption("Shoot >>>>", new Shoot());
    // this.autoChooser.addOption("ShortTargetAndShootBall", new
    // FiveBallPath.ShortTargetAndShootBall());

    SmartDashboard.putData("Auto selector", this.autoChooser);

    // Configure the button bindings
    configureButtonBindings();

    vision.init();
    vision.start(new BlueBallVisionModel());

    // CameraServer.startAutomaticCapture(1);

    // SmartDashboard.putNumber("h1", 0);
    // SmartDashboard.putNumber("h2", 0);
    // SmartDashboard.putNumber("s1", 0);
    // SmartDashboard.putNumber("s2", 0);
    // SmartDashboard.putNumber("v1", 0);
    // SmartDashboard.putNumber("v2", 0);

    SmartDashboard.putBoolean("Vision Debug", false);
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

    DRIVER_STICK.DRIVER_STICK_BUTTON_TWO
        .whenPressed(
            new ClimberExtendFastBottom()
        // new ClimberSetArmLengthTo(Climber.MIN_POSITION + Climber.CLOSE_TO_LIMIT)
        );
    DRIVER_STICK.DRIVER_STICK_BUTTON_THREE
        .whenPressed(
            new ClimberExtendFastTop()
        // new ClimberWaitForArmLengthTo(Climber.MAX_POSITION - Climber.CLOSE_TO_LIMIT)
        );

    DRIVER_STICK.DRIVER_STICK_BUTTON_FOUR
        .whenPressed(new ClimberSetArmLengthTo(Climber.MAX_POSITION - 1.75 * Climber.CLOSE_TO_LIMIT));
    // DRIVER_STICK.DRIVER_STICK_BUTTON_FIVE.whenPressed(new
    // ClimberSetArmLengthTo(Climber.TEST_4));

    DRIVER_STICK.DRIVER_STICK_BUTTON_ELEVEN.whenPressed(() -> RobotContainer.hood.adjustHoodClosePosition((+500.0)));
    DRIVER_STICK.DRIVER_STICK_BUTTON_TEN.whenPressed(() -> RobotContainer.hood.adjustHoodClosePosition((-500.0)));

    DRIVER_STICK.DRIVER_STICK_BUTTON_SIX.whenPressed(() -> Shooter.adjustShortShot(+25.0));
    DRIVER_STICK.DRIVER_STICK_BUTTON_SEVEN.whenPressed(() -> Shooter.adjustShortShot(-25.0));

    DRIVER_STICK.DRIVER_STICK_BUTTON_NINE.whenPressed(() -> Shooter.adjustLowGoalShot(+25.0));
    DRIVER_STICK.DRIVER_STICK_BUTTON_EIGHT.whenPressed(() -> Shooter.adjustLowGoalShot(-25.0));

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

    // Low goal
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_ONE.whenPressed(new InstantCommand(() -> {
      shooter.setShooterSpeed(Shooter.getLowGoalShot());
      accelerator.setAcceleratorSpeedVBus(0.4);
    }, shooter, accelerator));

    // High goal
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_THREE.whenPressed(new InstantCommand(() -> {
      shooter.setShooterSpeed(Shooter.getShortShot());
      accelerator.setAcceleratorSpeedVBus(0.4);
    }, shooter, accelerator));

    // Stop the shooter
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_TEN.whenPressed(new InstantCommand(() -> {
      shooter.setShooterSpeedVBus(0);
      accelerator.setAcceleratorSpeedVBus(0);
    }, shooter, accelerator));

    OPERATOR_PAD.OPERATOR_PAD_BUTTON_TWO.whenPressed(new IntakePistonsSet(IntakePistons.INTAKE_DOWN));
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_FOUR.whenPressed(new IntakePistonsSet(IntakePistons.INTAKE_UP));

    OPERATOR_PAD.OPERATOR_PAD_LEFT_Y_AXIS_UP.whileHeld(new ClimberSetVelocity(250));
    OPERATOR_PAD.OPERATOR_PAD_LEFT_Y_AXIS_DOWN.whileHeld(new ClimberSetVelocity(-250));

    // debug
    // OPERATOR_PAD.OPERATOR_PAD_BUTTON_TWO.whenPressed(() ->
    // shooter.setShooterSpeed(500.0));
    // OPERATOR_PAD.OPERATOR_PAD_BUTTON_TWO.whenReleased(() ->
    // shooter.setShooterSpeedVBus(0.0));
    // OPERATOR_PAD.OPERATOR_PAD_BUTTON_THREE.whenPressed(() ->
    // shooter.setShooterSpeed(1000.0));
    // OPERATOR_PAD.OPERATOR_PAD_BUTTON_THREE.whenReleased(() ->
    // shooter.setShooterSpeedVBus(0.0));
    // OPERATOR_PAD.OPERATOR_PAD_BUTTON_FOUR.whenPressed(() ->
    // shooter.setShooterSpeed(1500.0));
    // OPERATOR_PAD.OPERATOR_PAD_BUTTON_FOUR.whenReleased(() ->
    // shooter.setShooterSpeedVBus(0.0));

    OPERATOR_PAD.OPERATOR_PAD_BUTTON_SEVEN
        .whileHeld(new SystemShootBall(() -> Shooter.getLowGoalShot(), () -> hood.getHoodClosePosition()));

    OPERATOR_PAD.OPERATOR_PAD_BUTTON_FIVE
        .whileHeld(new SystemShootBall(() -> Shooter.getShortShot(), () -> hood.getHoodClosePosition()));

    OPERATOR_PAD.OPERATOR_PAD_BUTTON_SIX.whenHeld(new IntakeSetRollers());
    OPERATOR_PAD.OPERATOR_PAD_BUTTON_EIGHT.whenHeld(new IntakeReverseRollers());

    // OPERATOR_PAD.OPERATOR_PAD_BUTTON_NINE
    // .whenPressed(new ClimberWaitForArmLengthTo(Climber.MIN_POSITION +
    // Climber.CLOSE_TO_LIMIT));
    // OPERATOR_PAD.OPERATOR_PAD_BUTTON_TEN.whenPressed(new
    // SystemClimberAttachToNextRung());

    OPERATOR_PAD.OPERATOR_PAD_D_PAD_UP.whenPressed(new ClimberSetArmPositionTo(ClimberPistons.ARMS_UP));
    OPERATOR_PAD.OPERATOR_PAD_D_PAD_DOWN.whenPressed(new ClimberSetArmPositionTo(ClimberPistons.ARMS_DOWN));

    OPERATOR_PAD.OPERATOR_PAD_D_PAD_LEFT.whenPressed(new HoodAdjust(-200));
    OPERATOR_PAD.OPERATOR_PAD_D_PAD_RIGHT.whenPressed(new HoodAdjust(200));
  }

  private void configureDriverPadButtons() {
    DRIVER_PAD.DRIVER_PAD_RED_BUTTON
        .whileHeld(new SystemShootBall(() -> Shooter.LongShot, () -> Hood.LONGEST_SHOT));

    DRIVER_PAD.DRIVER_PAD_GREEN_BUTTON
        .whileHeld(new SystemShootBall(() -> Shooter.getLowGoalShot(), () -> hood.getHoodClosePosition(), true));

    DRIVER_PAD.DRIVER_PAD_LEFT_LOWER_TRIGGER_BUTTON
        .whileHeld(new SystemShootBall(() -> Shooter.getLowGoalShot(), () -> hood.getHoodClosePosition(), true));

    DRIVER_PAD.DRIVER_PAD_BUTTON_FIVE
        .whileHeld(new SystemShootBall(() -> Shooter.getShortShot(), () -> hood.getHoodClosePosition(), true));

    // DRIVER_PAD.DRIVER_PAD_YELLOW_BUTTON.whenPressed( new Hoo
    DRIVER_PAD.DRIVER_PAD_BLUE_BUTTON.whenPressed(new DriveStraightOnHeading(0.2, 12.0));

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
    return new DriveBaseTeleopCommand(RobotContainer.drive, RobotContainer.climber, DRIVER_PAD, OPERATOR_PAD);
  }

}
