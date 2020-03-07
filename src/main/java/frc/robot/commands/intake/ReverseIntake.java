package frc.robot.commands.intake;

import frc.lib.command.CommandBase;
import frc.robot.subsystems.Intake;

public final class ReverseIntake extends CommandBase {

    private final Intake intake = Intake.getInstance();

    public ReverseIntake() {
        super(Intake.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        intake.setIntakeSpeed(-1.0);
    }

    @Override
    public void end(boolean interrupted) {
        intake.setIntakeSpeed(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
