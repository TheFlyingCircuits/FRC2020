package frc.lib.command;

import lombok.Getter;

public class CommandState {

    @Getter private final boolean interruptible;

    public CommandState(boolean interruptible) {
        this.interruptible = interruptible;
    }


}
