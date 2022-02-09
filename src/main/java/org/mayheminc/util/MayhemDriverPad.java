package org.mayheminc.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class MayhemDriverPad {

        public final class GAMEPAD_BUTTION {
                public static final int GAMEPAD_F310_A_BUTTON = 1;
                public static final int GAMEPAD_F310_B_BUTTON = 2;
                public static final int GAMEPAD_F310_X_BUTTON = 3;
                public static final int GAMEPAD_F310_Y_BUTTON = 4;
                public static final int GAMEPAD_F310_LEFT_BUTTON = 5;
                public static final int GAMEPAD_F310_RIGHT_BUTTON = 6;
                public static final int GAMEPAD_F310_BACK_BUTTON = 7;
                public static final int GAMEPAD_F310_START_BUTTON = 8;
                public static final int GAMEPAD_F310_LEFT_STICK_BUTTON = 9;
                public static final int GAMEPAD_F310_RIGHT_STICK_BUTTON = 10;
        }

        public final class GAMEPAD_AXIS {
                public static final int GAMEPAD_F310_LEFT_X_AXIS = 0;
                public static final int GAMEPAD_F310_LEFT_Y_AXIS = 1;
                public static final int GAMEPAD_F310_LEFT_TRIGGER = 2;
                public static final int GAMEPAD_F310_RIGHT_TRIGGER = 3;
                public static final int GAMEPAD_F310_RIGHT_X_AXIS = 4;
                public static final int GAMEPAD_F310_RIGHT_Y_AXIS = 5;
        }

        public final Joystick DRIVER_PAD = new Joystick(Joysticks.DRIVER_GAMEPAD);;

        public final Button DRIVER_PAD_BUTTON_FIVE = new JoystickButton(DRIVER_PAD, 5); // Left Top Trigger;
        public final Button DRIVER_PAD_BUTTON_SIX = new JoystickButton(DRIVER_PAD, 6); // Right Top Trigger;
        public final Button DRIVER_PAD_LEFT_STICK_BUTTON = new JoystickButton(DRIVER_PAD,
                        GAMEPAD_BUTTION.GAMEPAD_F310_LEFT_STICK_BUTTON); // Left;
        public final Button DRIVER_PAD_RIGHT_STICK_BUTTON = new JoystickButton(DRIVER_PAD,
                        GAMEPAD_BUTTION.GAMEPAD_F310_RIGHT_STICK_BUTTON); // Right;

        public final JoystickPOVButton DRIVER_PAD_D_PAD_UP = new JoystickPOVButton(DRIVER_PAD, 0);
        public final JoystickPOVButton DRIVER_PAD_D_PAD_RIGHT = new JoystickPOVButton(DRIVER_PAD, 90);
        public final JoystickPOVButton DRIVER_PAD_D_PAD_DOWN = new JoystickPOVButton(DRIVER_PAD, 180);
        public final JoystickPOVButton DRIVER_PAD_D_PAD_LEFT = new JoystickPOVButton(DRIVER_PAD, 270);
        public final Button CLIMB_L3_BUTTON_PAIR = new AndJoystickButton(DRIVER_PAD,
                        GAMEPAD_BUTTION.GAMEPAD_F310_Y_BUTTON, DRIVER_PAD, GAMEPAD_BUTTION.GAMEPAD_F310_LEFT_BUTTON); // Y=Yellow,
                                                                                                                      // plus
                                                                                                                      // left
                                                                                                                      // top
                                                                                                                      // trigger
        public final Button CLIMB_L2_BUTTON_PAIR = new AndJoystickButton(DRIVER_PAD,
                        GAMEPAD_BUTTION.GAMEPAD_F310_A_BUTTON, DRIVER_PAD, GAMEPAD_BUTTION.GAMEPAD_F310_LEFT_BUTTON);// A=Green,
                                                                                                                     // plus
                                                                                                                     // left
                                                                                                                     // top
                                                                                                                     // trigger

        // Driver Control Modes
        public final Button TOGGLE_CLOSED_LOOP_MODE_BUTTON = new DisabledOnlyJoystickButton(DRIVER_PAD, 7);
        public final Button TOGGLE_FOD_BUTTON = new DisabledOnlyJoystickButton(DRIVER_PAD, 8);

        // NOTE: DRIVER_PAD_RIGHT_UPPER_TRIGGER_BUTTON is "QUICKTURN" in Drive.java - DO
        // NOT USE HERE!!!
        public final Button DRIVER_PAD_LEFT_UPPER_TRIGGER_BUTTON = new EnabledOnlyJoystickButton(DRIVER_PAD,
                        GAMEPAD_BUTTION.GAMEPAD_F310_LEFT_BUTTON);
        public final JoystickAxisButton DRIVER_PAD_LEFT_LOWER_TRIGGER_BUTTON = new JoystickAxisButton(DRIVER_PAD,
                        GAMEPAD_AXIS.GAMEPAD_F310_LEFT_TRIGGER, JoystickAxisButton.Direction.POSITIVE_ONLY);
        public final JoystickAxisButton DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON = new JoystickAxisButton(DRIVER_PAD,
                        GAMEPAD_AXIS.GAMEPAD_F310_RIGHT_TRIGGER, JoystickAxisButton.Direction.POSITIVE_ONLY);

        public final Button DRIVER_PAD_BACK_BUTTON = new JoystickButton(DRIVER_PAD,
                        GAMEPAD_BUTTION.GAMEPAD_F310_BACK_BUTTON);
        public final Button DRIVER_PAD_START_BUTTON = new JoystickButton(DRIVER_PAD,
                        GAMEPAD_BUTTION.GAMEPAD_F310_START_BUTTON);

        public final Button DRIVER_PAD_GREEN_BUTTON = new JoystickButton(DRIVER_PAD, 1);;
        public final Button DRIVER_PAD_RED_BUTTON = new JoystickButton(DRIVER_PAD, 2); // RED 'B" button
        public final Button DRIVER_PAD_BLUE_BUTTON = new JoystickButton(DRIVER_PAD, 3); // BLUE 'X' button
        public final Button DRIVER_PAD_YELLOW_BUTTON = new JoystickButton(DRIVER_PAD, 4); // YELLOW 'Y' button

        public MayhemDriverPad() {
        }

        private static final double THROTTLE_DEAD_ZONE_PERCENT = 0.05;
        private static final double MIN_THROTTLE_FOR_MOVEMENT = 0.02;  // what is the min throttle for Movement
        private static final double NORMAL_MAX_THROTTLE = 1.00;      // maximum speed is normally 100%
        private static final double SLOW_MODE_MAX_THROTTLE = 0.30;   // maximum throttle in "slow mode" is 30%

        public double driveThrottle() {
                // the driveThrottle is the "Y" axis of the Driver Gamepad.
                // However, the joysticks give -1.0 on the Y axis when pushed forward
                // This method reverses that, so that positive numbers are forward
                double throttleVal = -DRIVER_PAD.getY();
                double throttleAbs = Math.abs(throttleVal);
                double maxPercentThrottle = NORMAL_MAX_THROTTLE;

                if (DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON.get()) {
                        // check for "slow mode" and if so, constrain maxPercentThrottle to "SLOW_MODE_MAX_PERCENT"
                        maxPercentThrottle = SLOW_MODE_MAX_THROTTLE;
                } 

                // compute a scaled throttle magnitude, which will always be positive
                double throttleMag = MIN_THROTTLE_FOR_MOVEMENT + (maxPercentThrottle - MIN_THROTTLE_FOR_MOVEMENT) * throttleAbs * throttleAbs;

                if (Math.abs(throttleVal) < THROTTLE_DEAD_ZONE_PERCENT) {
                        // check for throttle being in "dead zone" and if so, set throttle to zero
                        throttleVal = 0.0;
                } else {
                        // make sure to preserve sign of throttle
                        if (throttleVal >= 0.0) {
                          throttleVal = throttleMag;
                        } else {
                          throttleVal = -throttleMag;
                        }
                }

                return (throttleVal);
        }

        private static final double STEERING_DEAD_ZONE_PERCENT = 0.05;
        private static final double MIN_STEERING_FOR_MOVEMENT = 0.02;
        private static final double NORMAL_MAX_STEERING = 1.00;
        private static final double SLOW_MODE_MAX_STEERING = 0.80;   // maximum steering in "slow mode" is 50%

        public double steeringX() {
                // SteeringX is the "X" axis of the right stick on the Driver Gamepad.
                double steeringVal = DRIVER_PAD.getRawAxis(GAMEPAD_AXIS.GAMEPAD_F310_RIGHT_X_AXIS);
                double steeringAbs = Math.abs(steeringVal);
                double maxPercentSteering = NORMAL_MAX_STEERING;

                if (DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON.get()) {
                        // check for "slow mode" and if so, constrain maxPercentSteering to "SLOW_MODE_MAX_PERCENT"
                        maxPercentSteering = SLOW_MODE_MAX_STEERING;
                } 

                // compute a scaled steering magnitude, which will always be positive
                double steeringMag = MIN_STEERING_FOR_MOVEMENT + (maxPercentSteering - MIN_STEERING_FOR_MOVEMENT) * steeringAbs;

                if (Math.abs(steeringVal) < STEERING_DEAD_ZONE_PERCENT) {
                        // check for steering being in "dead zone" and if so, set steering to zero
                        steeringVal = 0.0;
                } else {
                        // make sure to preserve sign of steering
                        if (steeringVal >= 0.0) {
                                steeringVal = steeringMag;
                        } else {
                                steeringVal = -steeringMag;
                        }
                }

                return (steeringVal);
        }

        public boolean quickTurn() {
                return (DRIVER_PAD.getRawButton(GAMEPAD_BUTTION.GAMEPAD_F310_RIGHT_BUTTON));
        }
}