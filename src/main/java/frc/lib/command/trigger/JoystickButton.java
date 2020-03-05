package frc.lib.command.trigger;

import edu.wpi.first.wpilibj.GenericHID;
import lombok.NonNull;

public class JoystickButton extends Button {
    private final GenericHID joystick;
    private final int buttonNumber;

    public JoystickButton(@NonNull GenericHID joystick, int buttonNumber) {
        this.joystick = joystick;
        this.buttonNumber = buttonNumber;
    }

    @Override
    public boolean get() {
        return joystick.getRawButton(buttonNumber);
    }
}
