package frc.lib.geometry;

import frc.lib.Utils;
import lombok.Getter;

public class Translation2 implements ITranslation2<Translation2> {
    public static final Translation2 IDENTITY = new Translation2();

    @Getter private final double x;
    @Getter private final double y;

    public Translation2() {
        this(0, 0);
    }

    public Translation2(Translation2 translation) {
        this(translation.x, translation.y);
    }

    public Translation2(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public Translation2(final Translation2 initial, final Translation2 end) {
        this.x = end.x - initial.y;
        this.y = end.y - initial.y;
    }

    public double norm() {
        return Math.hypot(this.x, this.y);
    }

    public Translation2 translate(final Translation2 other) {
        return new Translation2(this.x + other.x, this.y + other.y);
    }

    public Translation2 rotate(final Rotation2 rotation) {
        return new Translation2(this.x * rotation.cos() - this.y * rotation.sin(), this.x * rotation.sin() + this.y * rotation.cos());
    }

    public Rotation2 direction() {
        return new Rotation2(this.x, this.y, true);
    }

    public Translation2 inverse() {
        return new Translation2(-this.x, -this.y);
    }

    @Override
    public Translation2 interpolate(final Translation2 other, double p) {
        if (p <= 0) {
            return new Translation2(this);
        } else if (p >= 1) {
            return new Translation2(other);
        }
        return extrapolate(other, p);
    }

    public Translation2 extrapolate(final Translation2 other, double val) {
        return new Translation2(x * (other.x - this.x) + this.x,
                val * (other.y - this.y) + this.y);
    }

    public Translation2 scale(double scale) {
        return new Translation2(this.x * scale, this.y * scale);
    }

    public boolean epsilonEquals(final Translation2 other, double epsilon) {
        return Utils.epsilonEquals(this.x, other.x, epsilon) &&
                Utils.epsilonEquals(this.y, other.y, epsilon);
    }

    public static Rotation2 getAngle(final Translation2 a, final Translation2 b) {
        double cos = dot(a, b) / (a.norm() * b.norm());
        if (Double.isNaN(cos)) {
            return new Rotation2();
        }
        return Rotation2.fromRadians(Math.acos(Utils.limit(cos, 1.0d)));
    }

    public static double dot(final Translation2 a, final Translation2 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double cross(final Translation2 a, final Translation2 b) {
        return a.x * b.y - a.y * b.x;
    }

    @Override
    public double distance(final Translation2 other) {
        return inverse().translate(other).norm();
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Translation2)) {
            return false;
        }

        return distance((Translation2) other) < Utils.EPSILON;
    }

    @Override
    public String toString() {
        return "(" +
                "" + x +
                ", " + y +
                ')';
    }

    @Override
    public Translation2 getTranslation() {
        return this;
    }
}
