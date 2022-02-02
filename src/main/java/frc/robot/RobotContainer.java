// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.autos.PointToBall;
import frc.robot.commands.DriveBaseTeleopCommand;
import frc.robot.subsystems.DriveBaseSubsystem;
import frc.robot.subsystems.Intake;
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
  private final Joystick driveStick = new Joystick(Constants.Control.DRIVER_STICK_PORT);

  // The robot's subsystems and commands are defined here...
  private final DriveBaseSubsystem drive = new DriveBaseSubsystem();
  private final Intake intake = new Intake();

  private final SettableSendableChooser<Command> autoChooser = new SettableSendableChooser<>();

  public static final Vision vision = new Vision();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    this.autoChooser.setDefaultOption("hello world", new PointToBall(this.drive));

    SmartDashboard.putData("Auto selector", this.autoChooser);

    // Configure the button bindings
    configureButtonBindings();

    vision.init();
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
    JoystickButton button1 = new JoystickButton(this.driveStick, 1);
    JoystickButton button2 = new JoystickButton(this.driveStick, 2);

    button1.whenPressed(() -> autoChooser.setSelected("hello earth"));
    button2.whenPressed(() -> autoChooser.setSelected("hello world"));
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
    return new DriveBaseTeleopCommand(this.drive, this.driveStick);
  }
}
