package frc.lib.command;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class CommandGroupBase extends CommandBase implements Command {
    private static final Set<Command> groupedCommands = Collections.newSetFromMap(new WeakHashMap<>());

    public abstract void addCommand(Command... commands);

    static void registerGroupedCommands(Command... commands) {
        groupedCommands.addAll(Set.of(commands));
    }

    public static void clearGroupedCommands() {
        groupedCommands.clear();
    }

    public static void clearGroupedCommand(Command command) {
        groupedCommands.remove(command);
    }

    public static void requireUngrouped(Command... commands) {
        requireUngrouped(Set.of(commands));
    }

    public static void requireUngrouped(Collection<Command> commands) {
        if (!Collections.disjoint(commands, getGroupedCommands())) {
            throw new IllegalArgumentException("Commands cannot be added to more than one CommandGroup");
        }
    }

    static Set<Command> getGroupedCommands() {
        return groupedCommands;
    }

    public static CommandGroupBase sequence(Command... commands) {
        return new SequentialCommandGroup(commands);
    }
}
