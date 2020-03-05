package frc.lib.command.trigger;

import frc.lib.command.Command;
import frc.lib.subsystem.CommandSubsystem;
import frc.lib.subsystem.Subsystem;

import java.util.function.BooleanSupplier;

public class Button extends Trigger {
    public Button() {}

    public Button(BooleanSupplier isPressed) {
        super(isPressed);
    }

    public Button whenPressed(final Command command, boolean interruptible) {
        whenActive(command, interruptible);
        return this;
    }

    public Button whenPressed(final Command command) {
        whenActive(command);
        return this;
    }

    public Button whenPressed(final Runnable toRun, Class<? extends CommandSubsystem>... requirements) {
        whenActive(toRun, requirements);
        return this;
    }

    public Button whileHeld(final Command command, boolean interruptible) {
        whileActiveContinuous(command, interruptible);
        return this;
    }

    public Button whileHeld(final Command command) {
        whileActiveContinuous(command);
        return this;
    }

    public Button whileHeld(final Runnable toRun, Class<? extends CommandSubsystem>... requirements) {
        whileActiveContinuous(toRun, requirements);
        return this;
    }

    public Button whenReleased(final Command command, boolean interruptible) {
        whenInactive(command, interruptible);
        return this;
    }

    public Button whenReleased(final Command command) {
        whenInactive(command);
        return this;
    }

    public Button whileReleased(final Runnable toRun, Class<? extends CommandSubsystem>... requirements) {
        whenInactive(toRun, requirements);
        return this;
    }
}
