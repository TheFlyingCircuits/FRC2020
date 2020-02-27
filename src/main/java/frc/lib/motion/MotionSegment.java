package frc.lib.motion;

import lombok.Getter;
import lombok.Setter;

import static frc.lib.Utils.epsilonEquals;

public final class MotionSegment {
    @Getter @Setter private MotionState start, end;

    public MotionSegment(MotionState start, MotionState end) {
        this.start = start;
        this.end = end;
    }

    public boolean containsTime(double t) {
        return t >= start.getTime() && t <= end.getTime();
    }

    public boolean containsPosition(double position) {
        return position >= start.getPosition() && position <= end.getPosition()
                || position <= start.getPosition() && position >= end.getPosition();
    }

    public boolean isValid() {
        if (!epsilonEquals(start.getAcceleration(), end.getAcceleration(), Motion.EPSILON)) {
            System.err.println("Acceleration not consistent");
            return false;
        } else if (Math.signum(start.getVelocity()) * Math.signum(end.getVelocity()) < 0.0
                && !epsilonEquals(start.getVelocity(), 0.0, Motion.EPSILON)
                && !epsilonEquals(end.getVelocity(), 0.0, Motion.EPSILON)) {
            System.err.println("Velocity changes");
            return false;
        } else if (!start.extrapolate(end.getTime()).equals(end)) {
            if (start.getTime() == end.getTime() && Double.isInfinite(start.getAcceleration())) {
                return true;
            }

            System.err.println("position not consistent");
            return false;
        }

        return true;
    }
}
