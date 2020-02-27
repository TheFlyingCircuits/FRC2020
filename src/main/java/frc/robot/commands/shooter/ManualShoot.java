package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.subsystems.Control;
import frc.robot.subsystems.Shooter;

public final class ManualShoot extends CommandBase {

    Shooter shooter = Shooter.getInstance();

    public ManualShoot() {
        super(Shooter.class);
    }

    @Override
    public void init() {
        shooter.setShoot(0.0);
    }

    @Override
    public void tick() {
        shooter.setShoot(Control.getInstance().getLeft().getY());
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
