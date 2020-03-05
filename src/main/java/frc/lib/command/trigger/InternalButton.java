package frc.lib.command.trigger;

import lombok.Setter;

public class InternalButton extends Button {
    @Setter private boolean pressed;
    @Setter private boolean inverted;

    public InternalButton() {
        this(false);
    }

    public InternalButton(boolean inverted) {
        pressed = this.inverted = inverted;
    }

    public boolean get() {
        return pressed ^ inverted;
    }

}
