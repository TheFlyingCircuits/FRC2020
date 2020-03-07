package frc.robot.commands;

import frc.lib.command.Command;
import frc.lib.command.CommandGroupBase;
import frc.lib.command.InstantCommand;
import frc.robot.commands.climber.DeployLifter;
import frc.robot.commands.climber.IdleClimber;
import frc.robot.commands.climber.RetractLifter;
import frc.robot.commands.climber.StandardClimb;
import frc.robot.commands.drive.IdleDrive;
import frc.robot.commands.drive.StationaryDrive;
import frc.robot.commands.drive.TeleopArcadeDrive;
import frc.robot.commands.drive.TeleopCurvatureDrive;
import frc.robot.commands.hood.CalibrateHood;
import frc.robot.commands.hood.IdleHood;
import frc.robot.commands.intake.ExtendIntake;
import frc.robot.commands.intake.ForwardIntake;
import frc.robot.commands.intake.RetractIntake;
import frc.robot.commands.intake.ReverseIntake;
import frc.robot.commands.shooter.ShooterIdle;
import frc.robot.commands.shooter.StandardShoot;
import frc.robot.commands.vision.VisionIdle;
import frc.robot.subsystems.Climber;

public final class Commands {
    private Commands() {}

    /* DRIVE COMMANDS */
    public static final Command CURVATURE_DRIVE = new TeleopCurvatureDrive();
    public static final Command ARCADE_DRIVE_STANDARD = new TeleopArcadeDrive();
    public static final Command STATIONARY_DRIVE = new StationaryDrive();

    /* SHOOTER COMMANDS */
    public static final Command STANDARD_SHOOT = new StandardShoot();

    /* HOOD COMMANDS */
    public static final Command CALIBRATE_HOOD = new CalibrateHood();

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
