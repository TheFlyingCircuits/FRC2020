package frc.lib.command;

import edu.wpi.first.wpilibj.RobotState;
import frc.lib.subsystem.CommandSubsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CommandScheduler {

    private static CommandScheduler instance;

    public static synchronized CommandScheduler getInstance() {
        if (instance == null) {
            instance = new CommandScheduler();
        }

        return instance;
    }

    private final Map<Command, CommandState> scheduledCommands = new LinkedHashMap<>();
    private final Map<CommandSubsystem, Command> currentRequirements = new LinkedHashMap<>();
    private final Map<CommandSubsystem, Command> defaultCommands = new LinkedHashMap<>();

    /* SCHEDULING */
    private final Map<Command, Boolean> toSchedule = new LinkedHashMap<>();
    private final List<Command> toCancel = new ArrayList<>();
    private boolean runningCommands = false;

    public CommandScheduler() {
    }

    public void tick() {

        // TODO grouped command stuff

        // Run scheduled commands, remove finished commands
        runningCommands = true;
        for (Iterator<Command> iterator = scheduledCommands.keySet().iterator(); iterator.hasNext();) {
            final Command command = iterator.next();

            if (RobotState.isDisabled()) {
                // interrupt command
                command.end(true);
                // TODO actions on any command interrupt
                // remove active command requirements
                currentRequirements.keySet().removeAll(command.getRequirements());
                // remove command from iterator
                iterator.remove();
                continue;
            }

            // run the command
            command.tick();

            // end finished command
            if (command.isFinished()) {
                command.end(false);

                // remove command
                iterator.remove();
                currentRequirements.keySet().removeAll(command.getRequirements());
            }
        }
        runningCommands = false;

        // Schedule commands from the queue
        for (Map.Entry<Command, Boolean> commandInterruptable : toSchedule.entrySet()) {
            schedule(commandInterruptable.getValue(), commandInterruptable.getKey());
        }

        // Cancel commands from the queue
        for (Command command : toCancel) {
            cancel(command);
        }

        // Reset queues
        toSchedule.clear();
        toCancel.clear();

        // Schedule default commands for subsystems without an active command
        for (Map.Entry<CommandSubsystem, Command> defaultCommand : defaultCommands.entrySet()) {
            if (!currentRequirements.containsKey(defaultCommand.getKey())
                && defaultCommand.getValue() != null) {
                schedule(defaultCommand.getValue());
            }
        }
    }


    public void schedule(boolean canInterrupt, Command command) {
        if (runningCommands) {
            // add to the scheduling queue
            toSchedule.put(command, canInterrupt);
            return;
        }

        // ignore if robot is disabled or command is already scheduled
        if (RobotState.isDisabled() || scheduledCommands.containsKey(command)) {
            return;
        }

        // command requirements
        Set<CommandSubsystem> cmdRequirements = command.getRequirements();

        // continue with scheduling if requirements are not in use
        if (Collections.disjoint(this.currentRequirements.keySet(), cmdRequirements)) {
            initCommand(command, canInterrupt, cmdRequirements);
        } else {
            // check if all the commands active in requirements can be interrupted
            for (CommandSubsystem subsystem : cmdRequirements) {
                if (this.currentRequirements.containsKey(subsystem)
                && !scheduledCommands.get(this.currentRequirements.get(subsystem)).isInterruptible()) {
                    return;
                }
            }

            for (CommandSubsystem subsystem : cmdRequirements) {
                if (this.currentRequirements.containsKey(subsystem)) {
                    cancel(this.currentRequirements.get(subsystem));
                }
            }

            initCommand(command, canInterrupt, cmdRequirements);
        }

        // TODO grouped commands

    }

    public void schedule(boolean canInterrupt, Command... commands) {
        for (Command command : commands) {
            schedule(canInterrupt, command);
        }
    }

    public void schedule(Command... commands) {
        schedule(true, commands);
    }

    public void cancel(Command... commands) {
        if (runningCommands) {
            toCancel.addAll(List.of(commands));
            return;
        }

        // cancel the commands
        for (Command command : commands) {
            if (!scheduledCommands.containsKey(command)) {
                continue;
            }

            // interrupt the command
            command.end(true);

            // remove the command
            scheduledCommands.remove(command);
            currentRequirements.keySet().removeAll(command.getRequirements());
        }


    }

    public void cancelAll() {
        for (Command command : scheduledCommands.keySet().toArray(new Command[0])) {
            cancel(command);
        }
    }

    public Command requiring(CommandSubsystem subsystem) {
        return currentRequirements.get(subsystem);
    }

    public boolean isScheduled(Command... commands) {
        return scheduledCommands.keySet().containsAll(Set.of(commands));
    }

    private void initCommand(Command command, boolean canInterrupt, Set<CommandSubsystem> requirements) {
        // initialize the command
        command.init();

        CommandState state = new CommandState(canInterrupt);
        scheduledCommands.put(command, state);

        for (CommandSubsystem subsystem : requirements) {
            currentRequirements.put(subsystem, command);
        }
    }

    public void setDefaultCommand(CommandSubsystem subsystem, Command defaultCommand) {
        if (!defaultCommand.getRequirements().contains(subsystem)) {
            throw new IllegalArgumentException("Default commands must require their subsystem!");
        }

        if (defaultCommand.isFinished()) {
            throw new IllegalArgumentException("Default commands should not end!");
        }

        defaultCommands.put(subsystem, defaultCommand);
    }

    public Command getDefaultCommand(CommandSubsystem subsystem) {
        return defaultCommands.get(subsystem);
    }

    public void registerSubsystem(CommandSubsystem... subsystems) {
        for (CommandSubsystem subsystem : subsystems) {
            defaultCommands.put(subsystem, null);
        }
    }

}
