package frc.lib.motion;

import lombok.Getter;

import java.util.Objects;

public final class MotionProfileConstraints {
    @Getter private double maxAbsoluteVelocity = Double.POSITIVE_INFINITY, maxAbsoluteAcceleration = Double.POSITIVE_INFINITY;

    public MotionProfileConstraints(double maxAbsoluteVelocity, double maxAbsoluteAcceleration) {
        this.maxAbsoluteVelocity = maxAbsoluteVelocity;
        this.maxAbsoluteAcceleration = maxAbsoluteAcceleration;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotionProfileConstraints that = (MotionProfileConstraints) o;
        return Double.compare(that.maxAbsoluteVelocity, maxAbsoluteVelocity) == 0 &&
                Double.compare(that.maxAbsoluteAcceleration, maxAbsoluteAcceleration) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxAbsoluteVelocity, maxAbsoluteAcceleration);
    }
}
