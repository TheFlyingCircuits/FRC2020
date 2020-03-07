package frc.robot.commands.climber;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.subsystems.Climber;

public class StandardClimb extends CommandBase {

    private final Climber climber = Climber.getInstance();

    public StandardClimb() {
        super(Climber.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        climber.setClimb(control.getLeftY());
    }

    @Override
    public void end(boolean interrupted) {
        // stop the climber
        climber.setClimb(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
