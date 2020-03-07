package frc.lib.util;

import lombok.Getter;

public class DriveSignal {
    @Getter private final double left, right;
    @Getter private final boolean isBreaking;

    @Deprecated
    public DriveSignal(double left, double right) {
        this(left, right, false);
    }

    public DriveSignal(double left, double right, boolean breaking) {
        this.left = left;
        this.right = right;
        this.isBreaking = breaking;
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
                ", brakeMode=" + isBreaking +
                '}';
    }
}
