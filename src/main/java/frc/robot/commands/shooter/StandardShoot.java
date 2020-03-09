package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import frc.lib.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

public class StandardShoot extends CommandBase {

    private final Shooter shooter = Shooter.getInstance();

    private final Timer shootTimer = new Timer();
    private final Timer rampTimer = new Timer();

    public StandardShoot() {
        super(Shooter.class);
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
        // if the flywheel is spinning slower than we want, spin the motor at max speed
        // if the flywheel is spinning faster than we want, stop spinning the motor
        if (Math.abs(shooter.getRate()) < Constants.SHOOTER_MOTOR_RPM) {
            shooter.setFlywheelSpeed(1.0);
        } else {
            shooter.setFlywheelSpeed(0.0);
        }

        if (rampTimer.get() > 1) {
            shooter.setAcceleratorSpeed(-1.0);
        } else {
            shooter.setAcceleratorSpeed(0.0);
        }

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
