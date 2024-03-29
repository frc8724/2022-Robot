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
    public final class Talon {
        public static final int DRIVE_LEFT_TOP = 1; // falcons FX
        public static final int DRIVE_LEFT_BOTTOM = 2; // falcons FX
        public static final int DRIVE_RIGHT_TOP = 3; // falcons FX
        public static final int DRIVE_RIGHT_BOTTOM = 4; // falcons FX

        public static final int INTAKE_ROLLERS = 5; // Victor

        public static final int MAGAZINE_ROLLERS = 6; // Victor
        public static final int LOADER_ROLLER = 7; // Victor

        public static final int ACCELERATOR_WHEEL_L = 8; // Talon SRX
        public static final int ACCELERATOR_WHEEL_R = 9; // Talon SRX

        public static final int SHOOTER_WHEEL_L = 10; // Falcon
        public static final int SHOOTER_WHEEL_R = 11; // Falcon

        public static final int CLIMBER_L = 12; // Falcon
        public static final int CLIMBER_R = 13; // Flacon

        public static final int HOOD = 14; // Talon SRX
        public static final int INTAKE_ROLLERS_2 = 15; // Talon SRX

    }

    public final class Solenoid {
        public static final int INTAKE = 0;
        public static final int CLIMBER = 1;
        public static final int CAMERA_LIGHTS = 2;
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

    public final class DigitalInput {
        public static final int RIGHT_CLIMBER_BOTTOM_LIMIT = 1;
        public static final int RIGHT_CLIMBER_TOP_LIMIT = 0;

        public static final int LEFT_CLIMBER_TOP_LIMIT = 2;
        public static final int LEFT_CLIMBER_BOTTOM_LIMIT = 3;
    }

    public final class Lights {
        public static final int LIGHTS_PORT = 0;
    }

    public final class PdpPorts {
        public static final int DriveBaseLeft1 = 0;
        public static final int DriveBaseLeft2 = 1;
        public static final int ShooterFalconX = 2;
        public static final int ClimberLeft = 3;
        public static final int Intake = 5;
        public static final int UnusedTalon = 6;
        public static final int Maggie = 7;
        public static final int Loader = 9;
        public static final int Shooter775X = 10;
        public static final int Shooter775Y = 11;
        public static final int ClimberR = 12;
        public static final int ShooterRight = 13;
        public static final int DriveBaseRight1 = 14;
        public static final int DriveBaseRight2 = 15;
    }
}
