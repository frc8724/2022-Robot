package org.mayheminc.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.*;

/**
 *
 * @author Team1519
 */
public class EnabledOnlyJoystickButton extends Button {

    private GenericHID joystick;
    private int buttonNumber;

    public EnabledOnlyJoystickButton(GenericHID joystick, int buttonNumber) {
        this.joystick = joystick;
        this.buttonNumber = buttonNumber;
    }

    @Override
    public boolean get() {
        return joystick.getRawButton(buttonNumber) && DriverStation.isEnabled();
    }
}
