// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public final class Drive {
        public static final int DRIVE_LEFT_FRONT = 1;
        public static final int DRIVE_LEFT_REAR = 2;
        public static final int DRIVE_RIGHT_FRONT = 3;
        public static final int DRIVE_RIGHT_REAR = 4;
    }

    public final class PDP {
        public static final int DRIVE_LEFT_FRONT = 1;
        public static final int DRIVE_LEFT_REAR = 1;
        public static final int DRIVE_RIGHT_FRONT = 1;
        public static final int DRIVE_RIGHT_REAR = 1;
    }

    public final class Control {
        public static final int DRIVER_STICK_PORT = 0;
    }
}
