package frc.robot.commands.intake;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.subsystems.Control;
import frc.robot.subsystems.Intake;

public final class ForwardIntake extends CommandBase {

    private final Intake intake = Intake.getInstance();

    public ForwardIntake() {
        super(Intake.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        intake.setIntakeSpeed(1.0);
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
