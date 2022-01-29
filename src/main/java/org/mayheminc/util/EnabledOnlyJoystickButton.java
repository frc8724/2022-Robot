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
    private DriverStation ds;

    public EnabledOnlyJoystickButton(GenericHID joystick, int buttonNumber) {
        this.joystick = joystick;
        this.buttonNumber = buttonNumber;
        ds = DriverStation.getInstance();
    }

    public boolean get() {
        return joystick.getRawButton(buttonNumber) && ds.isEnabled();
    }
}
