package frc.lib.command.trigger;

import frc.lib.command.Command;
import frc.lib.command.CommandScheduler;
import frc.lib.command.InstantCommand;
import frc.lib.subsystem.CommandSubsystem;
import lombok.NonNull;

import java.util.function.BooleanSupplier;

public class Trigger {

    private final BooleanSupplier isActive;

    public Trigger(BooleanSupplier isActive) {
        this.isActive = isActive;
    }

    public Trigger() {
        isActive = () -> false;
    }

    public boolean get() {
        return isActive.getAsBoolean();
    }

    public Trigger whenActive(@NonNull final Command command, boolean interruptible) {
        CommandScheduler.getInstance().addButton(new Runnable() {
            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (pressedLast && pressed) {
                    CommandScheduler.getInstance().schedule(interruptible, command);
                }

                pressedLast = pressed;
            }
        });

        return this;
    }

    public Trigger whenActive(final Command command) {
        return whenActive(command, true);
    }

    public Trigger whenActive(final Runnable toRun, Class<? extends CommandSubsystem>... requirements) {
        return whenActive(new InstantCommand(toRun, requirements));
    }

    public Trigger whileActiveContinuous(@NonNull final Command command, boolean interruptible) {
         // add a "button" to the command scheduler to run while this trigger is held
        CommandScheduler.getInstance().addButton(new Runnable() {

            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (pressed) {
                    command.schedule(interruptible);
                } else if (pressedLast) {
                    command.cancel();
                }

                pressedLast = pressed;
            }
        });

        return this;
    }

    public Trigger whileActiveContinuous(final Command command) {
        return whileActiveContinuous(command, true);
    }

    public Trigger whileActiveContinuous(final Runnable toRun, Class<? extends CommandSubsystem>... requirements) {
        return whileActiveContinuous(new InstantCommand(toRun, requirements));
    }

    public Trigger whileActiveOnce(@NonNull final Command command, boolean interruptible) {

        CommandScheduler.getInstance().addButton(new Runnable() {

            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!pressedLast && pressed) {
                    command.schedule(interruptible);
                } else if (pressedLast && !pressed) {
                    command.cancel();
                }

                pressedLast = pressed;
            }
        });

        return this;
    }

    public Trigger whileActiveOnce(final Command command) {
        return whileActiveOnce(command, true);
    }

    public Trigger whenInactive(@NonNull final Command command, boolean interruptible) {

        CommandScheduler.getInstance().addButton(new Runnable() {

            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (pressedLast && !pressed) {
                    command.schedule(interruptible);
                }

                pressedLast = pressed;
            }
        });

        return this;
    }

    public Trigger whenInactive(final Command command) {
        return whenInactive(command, true);
    }

    public Trigger whenInactive(final Runnable toRun, Class<? extends CommandSubsystem>... requirements) {
        return whenInactive(new InstantCommand(toRun, requirements));
    }

    public Trigger toggleWhenActive(@NonNull final Command command, boolean interruptible) {

        CommandScheduler.getInstance().addButton(new Runnable() {

            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!pressedLast && pressed) {
                    if (command.isScheduled()) {
                        command.cancel();
                    } else {
                        command.schedule(interruptible);
                    }
                }

                pressedLast = pressed;
            }
        });

        return this;
    }

    public Trigger toggleWhenActive(final Command command) {
        return toggleWhenActive(command, true);
    }

    public Trigger cancelWhenActive(@NonNull final Command command) {

        CommandScheduler.getInstance().addButton(new Runnable() {

            private boolean pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!pressedLast && pressed) {
                    command.cancel();
                }

                pressedLast = pressed;
            }
        });

        return this;
    }

    public Trigger and(Trigger trigger) {
        return new Trigger(() ->  get() && trigger.get());
    }

    public Trigger or(Trigger trigger) {
        return new Trigger(() -> get() || trigger.get());
    }

    public Trigger negate() {
        return new Trigger(() -> !get());
    }

}
