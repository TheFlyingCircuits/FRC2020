package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.Utils;
import frc.lib.command.CommandBase;
import frc.lib.util.PID;
import frc.robot.Constants;
import frc.robot.subsystems.RobotTracker;
import frc.robot.subsystems.Shooter;

public class PIDShoot extends CommandBase {

    private final Shooter shooter = Shooter.getInstance();

    private final Timer shootTimer = new Timer();
    private final Timer rampTimer = new Timer();

    private final PID pid = new PID();

    public PIDShoot() {
        super(Shooter.class);

        // set pid loop constants
        pid.setKP(1.0);
        pid.setKD(0.0);
        pid.setKI(0.0);
    }

    @Override
    public void init() {
        // reset the timers
        shootTimer.reset();
        rampTimer.reset();

        // start the timers
        shootTimer.start();
        rampTimer.start();
    }

    @Override
    public void tick() {
        // get motor rpm
        final double readRpm = shooter.getRate();

        // get target motor rpm
        final double targetRpm = Constants.SHOOTER_MOTOR_RPM;

        // calculate error
        final double error = targetRpm - readRpm;

        // update pid loop
        pid.tick(RobotTracker.getInstance().getDT(), error);

        // get setpoint and scale to a manageable range
        final double setpoint = pid.getSetpoint() / 2500.0;

        // clamp within bounds
        final double output = Utils.clamp(setpoint, -1.0, 1.0);

        // set flywheel output
        shooter.setFlywheelSpeed(output);

        // update SmartDashboard
        SmartDashboard.putNumber("PIDShoot.Setpoint", setpoint);
        SmartDashboard.putNumber("PIDShoot.Error", error);
        SmartDashboard.putNumber("PIDShoot.TargetRPM", targetRpm);
        SmartDashboard.putNumber("PIDShoot.ReadRPM", readRpm);

        if (rampTimer.get() > 1) {
            shooter.setAcceleratorSpeed(-1.0);
        } else {
            shooter.setAcceleratorSpeed(0.0);
        }

        // may be unnecessary
        if (shootTimer.get() > 1) {
            shootTimer.reset();
        }

    }

    @Override
    public void end(boolean interrupted) {
        // stop the motors
        shooter.setAcceleratorSpeed(0.0);
        shooter.setFlywheelSpeed(0.0);

        // stop the timers
        shootTimer.stop();
        rampTimer.stop();

        // reset the timers
        shootTimer.reset();
        rampTimer.reset();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
