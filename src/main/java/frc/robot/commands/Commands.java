package frc.robot.commands;

import frc.lib.command.Command;
import frc.lib.command.CommandGroupBase;
import frc.robot.commands.climber.DeployLifter;
import frc.robot.commands.climber.IdleClimber;
import frc.robot.commands.climber.RetractLifter;
import frc.robot.commands.climber.StandardClimb;
import frc.robot.commands.drive.*;
import frc.robot.commands.hood.CalibrateHood;
import frc.robot.commands.hood.IdleHood;
import frc.robot.commands.hood.ManualSpeedHood;
import frc.robot.commands.hood.TargetHood;
import frc.robot.commands.intake.ExtendIntake;
import frc.robot.commands.intake.ForwardIntake;
import frc.robot.commands.intake.RetractIntake;
import frc.robot.commands.intake.ReverseIntake;
import frc.robot.commands.shooter.ShooterIdle;
import frc.robot.commands.shooter.StandardShoot;
import frc.robot.commands.vision.FaceTarget;
import frc.robot.commands.vision.FaceTargetThenStop;
import frc.robot.commands.vision.VisionIdle;

public final class Commands {
    private Commands() {}

    /* DRIVE COMMANDS */
    public static final Command CURVATURE_DRIVE = new TeleopCurvatureDrive();
    public static final Command ARCADE_DRIVE_STANDARD = new TeleopArcadeDrive();
    public static final Command ARCADE_DRIVE_SQUARE = new TeleopArcadeSquareDrive();
    public static final Command STATIONARY_DRIVE = new StationaryDrive();
    public static final Command ROTATE_TO_TARGET = new FaceTarget();

    /* SHOOTER COMMANDS */
    public static final Command STANDARD_SHOOT = new StandardShoot();

    /* HOOD COMMANDS */
    public static final Command CALIBRATE_HOOD = new CalibrateHood();
    public static final Command MANUAL_SPEED_HOOD = new ManualSpeedHood();
    public static final Command VISION_TARGET_HOOD = new TargetHood();

    /* INTAKE COMMANDS */
    public static final Command EXTEND_INTAKE = new ExtendIntake();
    public static final Command RETRACT_INTAKE = new RetractIntake();
    public static final Command FORWARD_INTAKE = new ForwardIntake();
    public static final Command REVERSE_INTAKE = new ReverseIntake();

    /* CLIMBER COMMANDS */
    public static final Command DEPLOY_LIFTER = new DeployLifter();
    public static final Command RETRACT_LIFTER = new RetractLifter();
    public static final Command CLIMB = new StandardClimb();
    public static final Command CLIMB_SEQUENCE = CommandGroupBase.sequence(DEPLOY_LIFTER, CLIMB);

    /* IDLE COMMANDS */
    public static final Command IDLE_CLIMBER = new IdleClimber();
    public static final Command IDLE_HOOD = new IdleHood();
    public static final Command IDLE_SHOOTER = new ShooterIdle();
    public static final Command IDLE_LIMELIGHT = new VisionIdle();
    public static final Command IDLE_DRIVE = new IdleDrive();
}
