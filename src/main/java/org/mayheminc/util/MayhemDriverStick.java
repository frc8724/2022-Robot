package org.mayheminc.util;

import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;

public class MayhemDriverStick {

    public final Joystick Joystick = new Joystick(Joysticks.DRIVER_JOYSTICK);

    public final Button DRIVER_STICK_BUTTON_ONE_DISABLED = new DisabledOnlyJoystickButton(Joystick, 1);
    public final Button DRIVER_STICK_BUTTON_ONE_ENABLED = new EnabledOnlyJoystickButton(Joystick, 1);
    public final Button DRIVER_STICK_BUTTON_TWO = new JoystickButton(Joystick, 2);
    public final Button DRIVER_STICK_BUTTON_THREE = new JoystickButton(Joystick, 3);
    public final Button DRIVER_STICK_BUTTON_FOUR = new JoystickButton(Joystick, 4);
    public final Button DRIVER_STICK_BUTTON_FIVE = new JoystickButton(Joystick, 5);
    public final Button DRIVER_STICK_BUTTON_SIX = new JoystickButton(Joystick, 6);
    public final Button DRIVER_STICK_BUTTON_SEVEN = new JoystickButton(Joystick, 7);
    public final Button DRIVER_STICK_BUTTON_EIGHT = new JoystickButton(Joystick, 8);
    public final Button DRIVER_STICK_BUTTON_NINE = new JoystickButton(Joystick, 9);
    public final Button DRIVER_STICK_BUTTON_TEN = new JoystickButton(Joystick, 10);
    public final Button DRIVER_STICK_BUTTON_ELEVEN = new JoystickButton(Joystick, 11);

    public final Button DRIVER_STICK_ENA_BUTTON_SIX = new EnabledOnlyJoystickButton(Joystick, 6);
    public final Button DRIVER_STICK_ENA_BUTTON_SEVEN = new EnabledOnlyJoystickButton(Joystick, 7);
    public final Button DRIVER_STICK_ENA_BUTTON_TEN = new EnabledOnlyJoystickButton(Joystick, 10);
    public final Button DRIVER_STICK_ENA_BUTTON_ELEVEN = new EnabledOnlyJoystickButton(Joystick, 11);

    public final int DRIVER_STICK_X_AXIS = 0;
    public final int DRIVER_STICK_Y_AXIS = 1;

    public MayhemDriverStick() {

    }
}