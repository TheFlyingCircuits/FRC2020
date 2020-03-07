package frc.robot.commands.shooter;

import frc.lib.command.CommandBase;
import frc.robot.subsystems.Control;
import frc.robot.subsystems.Shooter;

public final class ManualShoot extends CommandBase {

    Shooter shooter = Shooter.getInstance();

    public ManualShoot() {
        super(Shooter.class);
    }

    @Override
    public void init() {
        shooter.setFlywheelSpeed(0.0);
    }

    @Override
    public void tick() {
        shooter.setFlywheelSpeed(Control.getInstance().getLeft().getY());
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
