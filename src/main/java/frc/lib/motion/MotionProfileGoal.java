package frc.lib.motion;

import lombok.Getter;

import static frc.lib.Utils.epsilonEquals;

public final class MotionProfileGoal {

    public enum EndBehavior {
        OVERSHOOT,
        VIOLATE_ACCEL,
        VIOLATE_VELOCITY
    }

    @Getter protected double position, maxAbsoluteVelocity, positionTolerance, velocityTolerance;
    @Getter protected EndBehavior endBehavior;

    public MotionProfileGoal(double position) {
        this(position, 0.0);
    }

    public MotionProfileGoal(double position, double maxAbsoluteVelocity) {
        this(position, maxAbsoluteVelocity, EndBehavior.OVERSHOOT);
    }

    public MotionProfileGoal(double position, double maxAbsoluteVelocity, EndBehavior endBehavior) {
        this(position, maxAbsoluteVelocity, endBehavior, 1E-3, 1E-2);
    }

    public MotionProfileGoal(double position, double maxAbsoluteVelocity, EndBehavior endBehavior, double positionTolerance, double velocityTolerance) {
        this.position = position;
        this.maxAbsoluteVelocity = maxAbsoluteVelocity;
        this.endBehavior = endBehavior;
        this.positionTolerance = positionTolerance;
        this.velocityTolerance = velocityTolerance;
        if (endBehavior == EndBehavior.OVERSHOOT && maxAbsoluteVelocity > velocityTolerance) {
            endBehavior = EndBehavior.VIOLATE_ACCEL;
        }
    }

    public MotionProfileGoal(MotionProfileGoal other) {
        this(other.position, other.maxAbsoluteVelocity, other.endBehavior, other.positionTolerance, other.velocityTolerance);
    }

    public boolean atGoalState(MotionState state) {
        return atGoalPosition(state.getPosition()) &&
                (Math.abs(state.getVelocity()) < (maxAbsoluteVelocity + velocityTolerance) || endBehavior == EndBehavior.VIOLATE_VELOCITY);
    }

    public boolean atGoalPosition(double position) {
        return epsilonEquals(position, this.position, positionTolerance);
    }

    public MotionProfileGoal flipped() {
        return new MotionProfileGoal(-position, maxAbsoluteVelocity, endBehavior, positionTolerance, velocityTolerance);
    }

}
