/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.lib.command.CommandScheduler;
import frc.lib.scheduling.RobotScheduler;
import frc.robot.commands.drive.Stopped;
import frc.robot.commands.drive.TeleopArcadeDrive;
import frc.robot.commands.drive.TeleopCurvatureDrive;
import frc.robot.commands.shooter.ManualShoot;
import frc.robot.commands.shooter.NoShoot;
import frc.robot.commands.vision.VisionIdle;
import frc.robot.subsystems.*;
import frc.lib.subsystem.SubsystemManager;
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
  /* SUBSYSTEMS */
  private final SubsystemManager subsystemManager = SubsystemManager.getInstance();
  private final DriveTrain driveTrain = DriveTrain.getInstance();
  private final Control control = Control.getInstance();
  private final Shooter shooter = Shooter.getInstance();
  private final RobotTracker robotTracker = RobotTracker.getInstance();
  private final Climber climber = Climber.getInstance();
  private final Limelight limelight = Limelight.getInstance();
  public static Logger getLogger() {
    return Robot.logger;
  }

  /* COMMANDS */
  private final TeleopArcadeDrive arcadeDrive = new TeleopArcadeDrive();
  private final TeleopCurvatureDrive curvatureDrive = new TeleopCurvatureDrive();

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

    // scheduling
    subsystemManager.registerLoops(enabledScheduler, disabledScheduler);
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


  }

  @Override
  public void disabledPeriodic() {

  }

  @Override
  public void autonomousInit() {
    // handle schedulers
    disabledScheduler.stop();
    enabledScheduler.start();
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // update default commands
    driveTrain.setDefaultCommand(curvatureDrive);
    shooter.setDefaultCommand(new ManualShoot());
    limelight.setDefaultCommand(new VisionIdle());

    // handle schedulers
    disabledScheduler.stop();
    enabledScheduler.start();

    // reset drive and tracking
    driveTrain.reset();
    robotTracker.reset();
  }
  
  @Override
  public void teleopPeriodic() {
    CommandScheduler.getInstance().tick();
  }

  @Override
  public void testInit() {
    // handle schedulers
    disabledScheduler.stop();
    enabledScheduler.stop();

    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
