package frc.lib.command;

import frc.lib.subsystem.CommandSubsystem;

import java.util.Set;

public interface Command {

    void init();
    void tick();
    void end(boolean interrupted);
    boolean isFinished();

    Set<CommandSubsystem> getRequirements();

    default void schedule(boolean interruptible) {
        CommandScheduler.getInstance().schedule(interruptible, this);
    }

    default void schedule() {
        schedule(true);
    }

    default void cancel() {
        CommandScheduler.getInstance().cancel(this);
    }

    default boolean isScheduled() {
        return CommandScheduler.getInstance().isScheduled(this);
    }
}

