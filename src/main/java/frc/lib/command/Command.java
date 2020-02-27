package frc.lib.command;

import frc.lib.subsystem.CommandSubsystem;

import java.util.Set;

public interface Command {

    void init();
    void tick();
    void end(boolean interrupted);
    boolean isFinished();

    Set<CommandSubsystem> getRequirements();

}
