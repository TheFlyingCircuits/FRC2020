package frc.lib.subsystem;

import frc.lib.command.Command;
import frc.lib.command.CommandScheduler;

public abstract class CommandSubsystem extends Subsystem {

    public CommandSubsystem(String name) {
        super(name);
    }

    public synchronized void setDefaultCommand(Command defaultCommand) {
        CommandScheduler.getInstance().setDefaultCommand(this, defaultCommand);
    }

    public Command getDefaultCommand() {
        return CommandScheduler.getInstance().getDefaultCommand(this);
    }

    public Command getCurrentCommand() {
        return CommandScheduler.getInstance().requiring(this);
    }

}
