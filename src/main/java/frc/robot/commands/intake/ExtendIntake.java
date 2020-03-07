package frc.robot.commands.intake;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.subsystems.Intake;

public final class ExtendIntake extends CommandBase {

    private final Intake intake = Intake.getInstance();

    public ExtendIntake() {
        super(Intake.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        intake.setExtended(true);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
