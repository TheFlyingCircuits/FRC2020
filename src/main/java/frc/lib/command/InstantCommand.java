package frc.lib.command;

import frc.lib.subsystem.CommandSubsystem;
import lombok.NonNull;

public class InstantCommand extends CommandBase {
    private final Runnable toRun;

    @SafeVarargs
    public InstantCommand(@NonNull Runnable toRun, Class<? extends CommandSubsystem>... requirements) {
        this.toRun = toRun;
        addRequirements(requirements);
    }

    public InstantCommand() {
        toRun = () -> {

        };
    }

    @Override
    public void init() {
        toRun.run();
    }

    @Override
    public final void tick() {}

    @Override
    public void end(boolean interrupted) {}

    @Override
    public final boolean isFinished() {
        return true;
    }
}
