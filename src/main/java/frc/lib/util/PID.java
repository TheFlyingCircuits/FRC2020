package frc.lib.util;

import lombok.Getter;
import lombok.Setter;

public final class PID {

    @Getter @Setter private double kP;
    @Getter @Setter private double kI;
    @Getter @Setter private double kD;

    private double dError = 0.0, errorIntegral = 0.0, error = 0.0;
    private double lastError = 0.0;

    public PID() {
        this(1.0, 0.0, 0.0);
    }

    public PID(final double kP, final double kI, final double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void tick(final double dt, final double error) {
        // Calculate change
        this.dError = (error - lastError) / dt;
        this.errorIntegral += error;
        this.error = error;
    }

    public double getError() {
        return this.error;
    }

    public double getSetpoint() {
        return kP * error + kI * errorIntegral + kD * dError;
    }
}
