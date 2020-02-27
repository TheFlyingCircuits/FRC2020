package frc.lib.util;

import lombok.Getter;

public class DriveSignal {
    @Getter private final double left, right;
    @Getter private final boolean brakeMode;

    public DriveSignal(double left, double right) {
        this(left, right, false);
    }

    public DriveSignal(double left, double right, boolean brakeMode) {
        this.left = left;
        this.right = right;
        this.brakeMode = brakeMode;
    }

    public static DriveSignal fromControls(double throttle, double turn) {
        return new DriveSignal(throttle - turn, throttle + turn);
    }

    public static final DriveSignal NEUTRAL = new DriveSignal(0, 0);
    public static final DriveSignal BRAKE = new DriveSignal(0, 0, true);

    @Override
    public String toString() {
        return "DriveSignal{" +
                "left=" + left +
                ", right=" + right +
                ", brakeMode=" + brakeMode +
                '}';
    }
}
