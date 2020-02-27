package frc.robot.commands.shooter;

import frc.lib.command.CommandBase;
import frc.robot.subsystems.Control;
import frc.robot.subsystems.Shooter;

public final class NoShoot extends CommandBase {

    Shooter shooter = Shooter.getInstance();

    public NoShoot() {
        super(Shooter.class);
    }

    @Override
    public void init() {
        shooter.setShoot(0.0);
    }

    @Override
    public void tick() {
        shooter.setShoot(0.0);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setShoot(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
