package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.Subsystem;
import frc.robot.Constants;
import lombok.Getter;

import java.util.HashMap;

public final class Control extends Subsystem {

    private static Control instance;

    public static Control getInstance() {
        if (instance == null) {
            instance = new Control();
        }

        return instance;
    }

    @Getter private final Joystick right = new Joystick(Constants.RJS_CH);
    @Getter private final Joystick left = new Joystick(Constants.LJS_CH);

    public Control() {
        super("Control");
    }

    public double getDriveY() {
        return right.getY();
    }

    public double getDriveX() {
        return right.getX();
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
}
