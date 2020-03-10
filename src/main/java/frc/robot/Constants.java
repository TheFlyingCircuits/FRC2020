/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    /* DRIVE TRAIN */
    public static final int R_CH_1 = 13;
    public static final int R_CH_2 = 14;
    public static final int R_CH_3 = 15;
    public static final int L_CH_1 = 20;
    public static final int L_CH_2 = 1;
    public static final int L_CH_3 = 2;

    /* SHOOTER */
    public static final int SHOOTER_CH_1 = 3;
    public static final int SHOOTER_CH_2 = 12;
    public static final int ACCEL_CH = 10;
    public static final int HOOD_CH = 11;
    public static final double HOOD_MIN_ANGLE = 30; // degrees
    public static final double HOOD_MAX_ANGLE = 60; // degrees
    public static final double HOOD_TICK_RANGE = -0.4632568 - 0.0181884; // encoder position
    public static final double LIMELIGHT_MOUNT_ANGLE = 25; // degrees
    public static final double LIMELIGHT_HEIGHT = 17; // inches
    public static final double FLYWHEEL_RPS = 51000 / 60.0; // rotations per second
    public static final double SHOOTER_MOTOR_RPM = 4900.0;

    /* INTAKE */
    public static final int INTAKE_INNER_CH = 5;
    public static final int INTAKE_FRONT_CH = 6;
    public static final int INTAKE_EXTEND_CH = 2;
    public static final int INTAKE_RETRACT_CH = 3;

    /* CLIMBER */
    public static final int CLIMB_CH_1 = 9;
    public static final int CLIMB_CH_2 = 7;
    public static final int CLIMB_ENGAGE_CH = 0;
    public static final int CLIMB_DISENGAGE_CH = 1;

    /* FIELD CONSTANTS */
    public static final double OUTER_PORT_CENTER_HEIGHT = 12.0 * 8.0 + 2.25; // inches

    /* OTHER CONSTANTS */
    public static final int CURRENT_LIMIT = 40;
    /**
     * The current, in Amps, that we use to calibrate the hood at its minimum position
     */
    public static final double HOOD_CURRENT_STOP = 2.0;

    /* JOYSTICKS */
    public static final int LJS_CH = 1;
    public static final int RJS_CH = 0;

    /* JOYSTICK BUTTONS */
    public static final int QUICKTURN_CH = 2; // TODO correct channel

    /* ROBOT */
    public static final double OUTER_TRACK_WIDTH = 30.0; // inches
    public static final double INNER_TRACK_WIDTH = 26.0; // inches
    public static final double TRACK_WIDTH = (OUTER_TRACK_WIDTH + INNER_TRACK_WIDTH) / 2; // inches
    public static final double DRIVE_WHEEL_DIAMETER = 6.0; // inches
    public static final double DRIVE_WHEEL_CIRCUMFERENCE = Math.PI * DRIVE_WHEEL_DIAMETER; // inches

    public static final double encoderPerRotation = (5.35 + 5.38) / 2.0;
    public static final double wheelDiameterA = 4.0;
    public static final double wheelCircumferenceA = wheelDiameterA * Math.PI;
    public static final double positionFactorA = wheelCircumferenceA / encoderPerRotation;
    public static final double velocityFactorA = 10.0 / 240.0;

    /* ENCODERS */
    public static final double TPR = 4096.0;
    public static final double VTR = (5.0/7.11)/238.0;
    public static final double IPT = (4.0 * Math.PI)/5.45;
    public static final double MAX_ACCEL = 10.0;
    public static final double RPT = 1 / 8.0;


    private Constants() {
    }
}