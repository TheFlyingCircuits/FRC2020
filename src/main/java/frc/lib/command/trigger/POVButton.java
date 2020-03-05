package frc.lib.command.trigger;

import edu.wpi.first.wpilibj.GenericHID;
import lombok.NonNull;

public class POVButton extends Button {
    private final GenericHID joystick;
    private final int angle;
    private final int povNumber;

    public POVButton(@NonNull GenericHID joystick, int angle, int povNumber) {
        this.joystick = joystick;
        this.angle = angle;
        this.povNumber = povNumber;
    }

    public POVButton(GenericHID joystick, int angle) {
        this(joystick, angle, 0);
    }

    @Override
    public boolean get() {
        return joystick.getPOV(povNumber) == angle;
    }
}
