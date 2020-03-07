package frc.robot.commands.shooter;

import frc.lib.command.CommandBase;
import frc.robot.subsystems.Shooter;

public final class ShooterIdle extends CommandBase {

    Shooter shooter = Shooter.getInstance();

    public ShooterIdle() {
        super(Shooter.class);
    }

    @Override
    public void init() {
        shooter.setFlywheelSpeed(0.0);
    }

    @Override
    public void tick() {
        shooter.setFlywheelSpeed(0.0);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setFlywheelSpeed(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
