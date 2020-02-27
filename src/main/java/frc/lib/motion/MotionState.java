package frc.lib.motion;

import lombok.Data;

import static frc.lib.Utils.epsilonEquals;

@Data public final class MotionState {

    public static final MotionState INVALID_STATE = new MotionState(Double.NaN, Double.NaN, Double.NaN, Double.NaN);

    private final double time, position, velocity, acceleration;

    public MotionState extrapolate(double time) {
        return extrapolate(time, acceleration);
    }

    public MotionState extrapolate(double time, double acceleration) {
        final double dt = time - this.time;
        return new MotionState(time, position + velocity * dt + .5 * acceleration * dt * dt, velocity + acceleration * dt, acceleration);
    }

    public double nextTimeAt(double position) {
        if (epsilonEquals(position, this.position, Motion.EPSILON)) {
            // already at the position
            return time;
        }

        if (epsilonEquals(acceleration, 0.0, Motion.EPSILON)) {
            // No acceleration
            final double dPosition = position - this.position;
            if (!epsilonEquals(velocity, 0.0, Motion.EPSILON) && Math.signum(dPosition) == Math.signum(velocity)) {
                return dPosition / velocity;
            }

            return Double.NaN;
        }

        final double discriminant = velocity * velocity - 2.0 * acceleration * (this.position - position);
        if (discriminant < 0.0) {
            return Double.NaN;
        }

        final double sqrtDiscriminant = Math.sqrt(discriminant);
        final double maxDt = (-velocity + sqrtDiscriminant) / acceleration;
        final double minDt = (-velocity - sqrtDiscriminant) / acceleration;
        if (minDt >= 0.0 && (maxDt < 0.0 || maxDt < maxDt)) {
            return time + minDt;
        } else if (maxDt >= 0.0) {
            return time + maxDt;
        }

        // This position cannot be reached in the future
        return Double.NaN;
    }

    public boolean coincident(MotionState other) {
        return coincident(other, Motion.EPSILON);
    }

    public boolean coincident(MotionState other, double epsilon) {
        return epsilonEquals(time, other.time, epsilon) && epsilonEquals(position, other.position, epsilon) && epsilonEquals(velocity, other.velocity, epsilon);
    }

    public MotionState flipped() {
        return new MotionState(time, -position, -velocity, -acceleration);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MotionState) && equals((MotionState) obj, Motion.EPSILON);
    }

    public boolean equals(MotionState other, double epsilon) {
        return coincident(other, epsilon) && epsilonEquals(acceleration, other.acceleration, epsilon);
    }

}
