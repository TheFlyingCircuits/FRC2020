package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import frc.lib.command.trigger.JoystickButton;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.Subsystem;
import frc.robot.Constants;
import frc.robot.util.Buttons;
import frc.robot.util.JoystickPosition;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;

public final class Control extends Subsystem {

    private static Control instance;

    public static Control getInstance() {
        if (instance == null) {
            instance = new Control();
        }

        return instance;
    }

    private final HashMap<Integer, JoystickButton> rightButtons = new HashMap<>();
    private final HashMap<Integer, JoystickButton> leftButtons = new HashMap<>();

    @Getter private final Joystick right = new Joystick(Constants.RJS_CH);
    @Getter private final Joystick left = new Joystick(Constants.LJS_CH);

    public Control() {
        super("Control");
    }

    @Deprecated
    public double getDriveY() {
        return right.getY();
    }

    @Deprecated
    public double getDriveX() {
        return right.getX();
    }

    public double getLeftX() {
        return left.getX();
    }

    public double getLeftY() {
        return left.getY();
    }

    public double getLeftTwist() {
        return left.getTwist();
    }

    public double getLeftSlider() {
        return left.getRawAxis(3);
    }

    public double getRightX() {
        return right.getX();
    }

    public double getRightY() {
        return right.getY();
    }

    public double getRightTwist() {
        return right.getTwist();
    }

    public double getRightSlider() {
        return right.getRawAxis(3);
    }

    public JoystickButton getRightButton(int id) {
        if (!rightButtons.containsKey(id)) {
            rightButtons.put(id, new JoystickButton(right, id));
        }

        return rightButtons.get(id);
    }

    public JoystickButton getLeftButton(int id) {
        if (!leftButtons.containsKey(id)) {
            leftButtons.put(id, new JoystickButton(left, id));
        }

        return leftButtons.get(id);
    }

    public JoystickButton getControl(@NonNull JoystickPosition side, @NonNull Buttons button) {
        final int id = button.getId();

        if (side.equals(JoystickPosition.RIGHT)) {
            return getRightButton(id);
        } else {
            return getLeftButton(id);
        }
    }

    @Override
    public void check() {

    }

    @Override
    public void readIO() {

    }

    @Override
    public void writeIO() {

    }

    @Override
    public void updateDashboard() {

    }

    @Override
    public void registerLoops(Scheduler scheduler) {

    }

    private final class IO {
        private double rightX, rightY, rightTwist, rightSlider;
        private double leftX, leftY, leftTwist, leftSlider;
    }

}
