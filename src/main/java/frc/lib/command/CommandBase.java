package frc.lib.command;

import frc.lib.subsystem.CommandSubsystem;
import frc.lib.subsystem.Subsystem;

import java.util.HashSet;
import java.util.Set;

public abstract class CommandBase implements Command {

    protected Set<CommandSubsystem> requirements = new HashSet<>();

    @SafeVarargs
    public CommandBase(Class<? extends CommandSubsystem>... requirements) {
        addRequirements(requirements);
    }

    @SafeVarargs
    protected final void addRequirements(Class<? extends CommandSubsystem>... requirements) {
        for (Class<? extends CommandSubsystem> requirement : requirements) {
            this.requirements.add(Subsystem.getInstance(requirement));
        }
    }

    @Override
    public final Set<CommandSubsystem> getRequirements() {
        return this.requirements;
    }
}
