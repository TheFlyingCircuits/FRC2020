package frc.robot.commands.climber;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.subsystems.Climber;

public class IdleClimber extends CommandBase {

    private final Climber climber = Climber.getInstance();

    public IdleClimber() {
        super(Climber.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        climber.setClimb(0.0);
        climber.setDeployed(false);
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
