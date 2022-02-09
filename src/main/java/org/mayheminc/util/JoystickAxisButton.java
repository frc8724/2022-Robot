/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mayheminc.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;

/**
 *
 * @author Team1519
 */
public class JoystickAxisButton extends Button {
    enum Direction {
        BOTH_WAYS,
        POSITIVE_ONLY,
        NEGATIVE_ONLY
    }

    private static final double AXIS_THRESHOLD = 0.2;

    private Joystick joystick;
    private Joystick.AxisType axis;
    private int axisInt;
    private Direction direction;

    private double getAxis(Joystick.AxisType axis) {
        switch (axis) {
        case kX:
            return joystick.getX();
        case kY:
            return joystick.getY();
        case kZ:
            return joystick.getZ();
        case kThrottle:
            return joystick.getThrottle();
        case kTwist:
            return joystick.getTwist();
        default:
            // Unreachable
            return 0.0;
        }
    }

    public JoystickAxisButton(Joystick stick, Joystick.AxisType axis) {
        this(stick, axis, Direction.BOTH_WAYS);
    }

    public JoystickAxisButton(Joystick stick, Joystick.AxisType axis, Direction direction) {
        joystick = stick;
        this.axis = axis;
        this.direction = direction;
    }

    public JoystickAxisButton(Joystick stick, int axis) {
        this(stick, axis, Direction.BOTH_WAYS);
    }

    public JoystickAxisButton(Joystick stick, int axis, Direction direction) {
        joystick = stick;
        axisInt = axis;
        this.direction = direction;
    }

    @Override
    public boolean get() {
        switch (direction) {
        case BOTH_WAYS:
            if (axis != null) {
                return Math.abs(getAxis(axis)) > AXIS_THRESHOLD;
            } else {
                return Math.abs(joystick.getRawAxis(axisInt)) > AXIS_THRESHOLD;
            }

        case POSITIVE_ONLY:
            if (axis != null) {
                return getAxis(axis) > AXIS_THRESHOLD;
            } else {
                return joystick.getRawAxis(axisInt) > AXIS_THRESHOLD;
            }

        case NEGATIVE_ONLY:
            if (axis != null) {
                return getAxis(axis) < -AXIS_THRESHOLD;
            } else {
                return joystick.getRawAxis(axisInt) < -AXIS_THRESHOLD;
            }
        }

        return false;
    }
}
