package frc.robot.commands.climber;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.subsystems.Climber;

public class DeployLifter extends CommandBase {

    private final Climber climber = Climber.getInstance();

    public DeployLifter() {
        super(Climber.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        climber.setDeployed(true);
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
