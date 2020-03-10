/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.lib.command.CommandScheduler;
import frc.lib.scheduling.RobotScheduler;
import frc.robot.commands.Commands;
import frc.robot.commands.hood.ManualSpeedHood;
import frc.robot.commands.hood.TargetHood;
import frc.robot.commands.vision.VisionOn;
import frc.robot.subsystems.*;
import frc.lib.subsystem.SubsystemManager;
import frc.robot.util.Buttons;
import frc.robot.util.JoystickPosition;
import frc.robot.util.LogFormatter;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private static final Logger logger = Logger.getLogger("Robot");
  /* SCHEDULER */
  private final RobotScheduler enabledScheduler = new RobotScheduler();
  private final RobotScheduler disabledScheduler = new RobotScheduler();
  private final CommandScheduler commandScheduler = CommandScheduler.getInstance();

  /* SUBSYSTEMS */
  private final SubsystemManager subsystemManager = SubsystemManager.getInstance();
  private final DriveTrain driveTrain = DriveTrain.getInstance();
  private final Control control = Control.getInstance();
  private final Shooter shooter = Shooter.getInstance();
  private final RobotTracker robotTracker = RobotTracker.getInstance();
  private final Climber climber = Climber.getInstance();
  private final Limelight limelight = Limelight.getInstance();
  private final AimingHood hood = AimingHood.getInstance();
  private final Intake intake = Intake.getInstance();

  /* JOYSTICKS */
  private final Joystick rightJoystick = control.getRight();
  private final Joystick left = control.getLeft();

//  private final Intake intake = Intake.

  public static Logger getLogger() {
    return Robot.logger;
  }

  public static void log(String message) {
    logger.log(Level.INFO, message);
  }

  public static void log(Level level, String message) {
    logger.log(level, message);
  }

  public static void log(String message, Object... args) {
    logger.log(Level.INFO, String.format(message, args));
  }

  public static void log(Level level, String message, Object... args) {
    logger.log(level, String.format(message, args));
  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // setup logger
    // TODO better logging system
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new LogFormatter());
    logger.addHandler(consoleHandler);

    // load subsystems
    subsystemManager.loadSubsystem(driveTrain);
    subsystemManager.loadSubsystem(robotTracker);
    subsystemManager.loadSubsystem(shooter);
    subsystemManager.loadSubsystem(control);
    subsystemManager.loadSubsystem(climber);
    subsystemManager.loadSubsystem(limelight);
    subsystemManager.loadSubsystem(hood);
    subsystemManager.loadSubsystem(intake);

    // scheduling
    subsystemManager.registerLoops(enabledScheduler, disabledScheduler);

    // default commands, everything idle
    driveTrain.setDefaultCommand(Commands.IDLE_DRIVE);
    shooter.setDefaultCommand(Commands.IDLE_SHOOTER);
    limelight.setDefaultCommand(Commands.IDLE_LIMELIGHT);
    hood.setDefaultCommand(Commands.IDLE_HOOD);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // run the command scheduler
    commandScheduler.tick();

    // Push updates to smart dashboard
    subsystemManager.updateDashboard();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
    // handle schedulers
    enabledScheduler.stop();
    disabledScheduler.start();

    // cancel commands
    commandScheduler.cancelAll();
  }

  @Override
  public void disabledPeriodic() {

  }

  @Override
  public void autonomousInit() {
    // cancel running commands
    commandScheduler.cancelAll();

    // handle schedulers
    disabledScheduler.stop();
    enabledScheduler.start();
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // cancel running commands
    commandScheduler.cancelAll();

    // update default commands
    driveTrain.setDefaultCommand(Commands.IDLE_DRIVE);
    limelight.setDefaultCommand(new VisionOn());

    // calibrate the hood
    commandScheduler.schedule(Commands.CALIBRATE_HOOD);

    // apply bindings
    control.getControl(JoystickPosition.LEFT, Buttons.TOP_LEFT).whenPressed(Commands.EXTEND_INTAKE);
    control.getControl(JoystickPosition.LEFT, Buttons.TOP_RIGHT).whenPressed(Commands.RETRACT_INTAKE);
    control.getControl(JoystickPosition.RIGHT, Buttons.TRIGGER).whileHeld(Commands.FORWARD_INTAKE);
    control.getControl(JoystickPosition.RIGHT, Buttons.TOP_LEFT).whileHeld(Commands.REVERSE_INTAKE);
    control.getControl(JoystickPosition.RIGHT, Buttons.TOP_CENTER).whileHeld(Commands.STANDARD_SHOOT);
    control.getControl(JoystickPosition.LEFT, Buttons.TRIGGER).whenPressed(Commands.CLIMB_SEQUENCE);
    control.getLeftButton(5).whenPressed(Commands.RETRACT_LIFTER);
    control.getLeftButton(6).whileHeld(Commands.MANUAL_SPEED_HOOD);
    control.getLeftButton(7).whenPressed(Commands.CALIBRATE_HOOD);
    control.getLeftButton(8).whileHeld(Commands.VISION_TARGET_HOOD);
    control.getLeftButton(9).whileHeld(Commands.ROTATE_TO_TARGET);

    // handle schedulers
    disabledScheduler.stop();
    enabledScheduler.start();

    // reset drive and tracking
    driveTrain.reset();
    robotTracker.reset();
  }
  
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // cancel running commands
    commandScheduler.cancelAll();

    // handle schedulers
    disabledScheduler.stop();
    enabledScheduler.stop();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
