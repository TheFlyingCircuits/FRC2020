package frc.lib.command;

import java.util.ArrayList;
import java.util.List;

public class SequentialCommandGroup extends CommandGroupBase {
    private final List<Command> commands = new ArrayList<>();
    private int currentCommandIndex = -1;

    public SequentialCommandGroup(Command... commands) {
        addCommand(commands);
    }

    @Override
    public void addCommand(Command... commands) {
        requireUngrouped(commands);

        if (currentCommandIndex != -1) {
            throw new IllegalStateException("Commands cannot be added to a CommandGroup while the group is running");
        }

        registerGroupedCommands(commands);

        for (Command command : commands) {
            this.commands.add(command);
            requirements.addAll(command.getRequirements());
        }
    }

    @Override
    public void init() {
        currentCommandIndex = 0;

        if (!commands.isEmpty()) {
            commands.get(0).init();
        }
    }

    @Override
    public void tick() {
        if (commands.isEmpty()) {
            return;
        }

        Command currentCommand = commands.get(currentCommandIndex);

        currentCommand.tick();

        if (currentCommand.isFinished()) {
            currentCommand.end(false);

            currentCommandIndex++;
            if (currentCommandIndex < commands.size()) {
                commands.get(currentCommandIndex).init();;
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted && !commands.isEmpty() && currentCommandIndex > -1
            && currentCommandIndex < commands.size()) {
            commands.get(currentCommandIndex).end(true);
        }
        currentCommandIndex = -1;
    }

    @Override
    public boolean isFinished() {
        return currentCommandIndex == commands.size();
    }
}
