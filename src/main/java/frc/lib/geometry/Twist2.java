package frc.lib.geometry;

import frc.lib.Utils;

public class Twist2 {
    public static final Twist2 IDENTITY = new Twist2(0.0d, 0.0d, 0.0d);

    private final double dx;
    private final double dy;
    private final double dtheta;

    public Twist2(double dx, double dy, double dtheta) {
        this.dx = dx;
        this.dy = dy;
        this.dtheta = dtheta;
    }

    public Twist2 scale(double scale) {
        return new Twist2(this.dx * scale, this.dy * scale, this.dtheta * scale);
    }

    public double norm() {
        if (dy == 0.0) {
            return Math.abs(this.dx);
        }

        return Math.hypot(this.dx, this.dy);
    }

    public double curvature() {
        if (Math.abs(this.dtheta) < Utils.EPSILON && this.norm() < Utils.EPSILON) {
            return 0.0d;
        }

        return this.dtheta / this.norm();
    }

    public double deltaX() {
        return this.dx;
    }

    @Override
    public String toString() {
        return "Twist2{" +
                "dx=" + dx +
                ", dy=" + dy +
                ", dtheta=" + dtheta +
                '}';
    }

    public double deltaY() {
        return this.dy;
    }

public double deltaTheta() {
        return this.dtheta;
    }
}
