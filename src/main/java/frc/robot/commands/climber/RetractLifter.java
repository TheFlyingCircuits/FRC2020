package frc.robot.commands.climber;

import frc.lib.command.CommandBase;
import frc.robot.subsystems.Climber;

public class RetractLifter extends CommandBase {

    private final Climber climber = Climber.getInstance();

    public RetractLifter() {
        super(Climber.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        climber.setDeployed(false);
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
