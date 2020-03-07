package frc.robot.commands.intake;

import frc.lib.command.CommandBase;
import frc.robot.subsystems.Intake;

public final class RetractIntake extends CommandBase {

    private final Intake intake = Intake.getInstance();

    public RetractIntake() {
        super(Intake.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        intake.setExtended(false);
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
