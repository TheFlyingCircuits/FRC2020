package frc.lib.geometry;

import frc.lib.Utils;

public class Rotation2 implements IRotation2<Rotation2> {
    public static final Rotation2 IDENTITY = new Rotation2();

    private double x = Double.NaN;
    private double y = Double.NaN;
    private double theta = Double.NaN;

    private Rotation2(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public Rotation2() {
        this(1.0d, 1.0d, 0.0d);
    }

    public Rotation2(double x, double y, boolean unit) {
        if (unit) {
            // TODO precision level
            final double magnitude = Math.hypot(x, y);
            this.x = x / magnitude;
            this.y = y / magnitude;
        } else {
            this.x = x;
            this.y = y;
        }
    }

    public Rotation2(double theta, boolean clamp) {
        if (clamp) {
            this.theta = clampAngle(theta);
        }

        this.theta = theta;
    }

    public Rotation2(Rotation2 other) {
        x = other.x;
        y = other.y;
        theta = other.theta;
    }

    public Rotation2(final Translation2 direction, boolean unit) {
        this(direction.getX(), direction.getY(), unit);
    }

    public double getRadians() {
        computeAngle();
        return this.theta;
    }

    public double getDegrees() {
        return getRadians() * 180 / Math.PI;
    }

    public Rotation2 rotate(final Rotation2 rotation) {
        if (this.hasTrig() && rotation.hasTrig()) {
            return new Rotation2(this.x * rotation.x - this.y * rotation.y,
                    this.x * rotation.y + this.y * rotation.x, true);
        } else {
            return fromRadians(this.getRadians() + rotation.getRadians());
        }
    }

    public Rotation2 normal() {
        if (hasTrig()) {
            return new Rotation2(-y, x, false);
        } else {
            return fromRadians(getRadians() - Math.PI / 2.0d);
        }
    }

    public Rotation2 inverse() {
        if (hasTrig()) {
            return new Rotation2(x, -y, false);
        } else {
            return fromRadians(-getRadians());
        }
    }

    public boolean isParallel(final Rotation2 rotation) {
        if (hasAngle() && rotation.hasAngle()) {
            return Utils.epsilonEquals(this.theta, rotation.theta) ||
                    Utils.epsilonEquals(this.theta, clampAngle(rotation.theta + Math.PI));
        } else if (hasTrig() && rotation.hasTrig()) {
            return Utils.epsilonEquals(this.x, rotation.x) && Utils.epsilonEquals(this.y, rotation.y);
        } else {
            return Utils.epsilonEquals(this.getRadians(), rotation.getRadians()) ||
                    Utils.epsilonEquals(this.theta, clampAngle(rotation.theta));
        }
    }

    public Translation2 toTranslation2() {
        computeTrig();
        return new Translation2(x, y);
    }

    public static Rotation2 fromRadians(double radians) {
        return new Rotation2(radians, true);
    }

    public static Rotation2 fromDegrees(double degrees) {
        return fromRadians(degrees * Math.PI/180.0);
    }

    @Override
    public String toString() {
        return String.format("%sº", getDegrees());
    }

    protected double clampAngle(double angle) {
        final double TAU = 2.0 * Math.PI;
        angle = angle % TAU;
        angle = (angle + TAU) % TAU;
        if (angle > Math.PI) {
            angle -= TAU;
        }
        return angle;
    }

    private boolean hasTrig() {
        return !Double.isNaN(this.x) && !Double.isNaN(y);
    }

    private boolean hasAngle() {
        return !Double.isNaN(this.theta);
    }

    private void computeTrig() {
        if (!hasTrig()) {
            if (Double.isNaN(this.theta)) {
                throw new UnsupportedOperationException("no angle");
            }

            // calculate the trig
            this.x = Math.cos(this.theta);
            this.y = Math.sin(this.theta);
        }
    }

    private void computeAngle() {
        if (!hasAngle()) {
            if (Double.isNaN(this.x) || Double.isNaN(this.y)) {
                throw new UnsupportedOperationException("no components");
            }

            // calculate angle
            this.theta = Math.atan2(this.x, this.y);
        }
    }

    public double cos() {
        computeTrig();
        return x;
    }

    public double sin() {
        computeTrig();
        return y;
    }

    public double tan() {
        computeTrig();
        if (Math.abs(this.x) < Utils.EPSILON) {
            if (this.y >= 0.0d) {
                return Double.POSITIVE_INFINITY;
            } else {
                return Double.NEGATIVE_INFINITY;
            }
        }

        return this.y / this.x;
    }


    @Override
    public Rotation2 getRotation() {
        return this;
    }

    @Override
    public Rotation2 interpolate(Rotation2 other, double x) {
        if (x <= 0.0) {
            return new Rotation2(this);
        } else if (x >= 1.0) {
            return new Rotation2(other);
        }
        double angle_diff = inverse().rotate(other).getRadians();
        return this.rotate(Rotation2.fromRadians(angle_diff * x));
    }
}
