package frc.lib.motion;

public final class Motion {

    public static final double EPSILON = 1e-6;

    private Motion() {}

    protected static MotionProfile generateFlippedProfile(MotionProfileConstraints constraints,
                                                          MotionProfileGoal goal,
                                                          MotionState previousState) {
        MotionProfile profile = generateProfile(constraints, goal.flipped(), previousState.flipped());
        for (MotionSegment segment : profile.getSegments()) {
            segment.setStart(segment.getStart().flipped());
            segment.setEnd(segment.getEnd().flipped());
        }

        return profile;
    }

    public synchronized static MotionProfile generateProfile(MotionProfileConstraints constraints,
                                                             MotionProfileGoal goal,
                                                             MotionState previousState) {
        double dPosition = goal.getPosition() - previousState.getPosition();
        if (dPosition < 0.0 || (dPosition == 0.0 && previousState.getVelocity() < 0.0)) {
            // TODO generate flipped profile
        }

        MotionState start = new MotionState(previousState.getTime(),
                previousState.getPosition(),
                Math.signum(previousState.getVelocity()) * Math.min(Math.abs(previousState.getVelocity()), constraints.getMaxAbsoluteVelocity()),
                Math.signum(previousState.getAcceleration()) * Math.min(Math.abs(previousState.getAcceleration()), constraints.getMaxAbsoluteAcceleration()));

        MotionProfile profile = new MotionProfile();

        // Reset the motion profile with our calculated starting state
        profile.reset(start);

        if (start.getVelocity() < 0.0 && dPosition > 0.0) {
            final double stopTime = Math.abs(start.getVelocity() / constraints.getMaxAbsoluteAcceleration());
            profile.appendControl(constraints.getMaxAbsoluteAcceleration(), stopTime);
            start = profile.getEndingState();
            dPosition = goal.getPosition() - start.getPosition();
        }

        final double minAbsVelAtGoal2 = start.getVelocity() * start.getVelocity() - 2.0 * constraints.getMaxAbsoluteAcceleration() * dPosition;
        final double minAbsVelAtGoal = Math.sqrt(Math.abs(minAbsVelAtGoal2));
        final double maxAbsVelAtGoal = Math.sqrt(start.getVelocity() * start.getVelocity() + 2.0 * constraints.getMaxAbsoluteAcceleration() * dPosition);
        double goalVelocity = goal.getMaxAbsoluteVelocity();
        double maxAcceleration = constraints.getMaxAbsoluteAcceleration();

        if (minAbsVelAtGoal2 > 0.0 && minAbsVelAtGoal > (goal.getMaxAbsoluteVelocity() + goal.getVelocityTolerance())) {
            if (goal.getEndBehavior().equals(MotionProfileGoal.EndBehavior.VIOLATE_VELOCITY)) {
                goalVelocity = minAbsVelAtGoal;
            } else if (goal.getEndBehavior().equals(MotionProfileGoal.EndBehavior.VIOLATE_ACCEL)) {
                if (Math.abs(dPosition) < goal.getPositionTolerance()) {
                    profile.appendSegment(new MotionSegment(
                            new MotionState(profile.getEndTime(), profile.getEndPosition(), profile.getEndVelocity(),
                                    Double.NEGATIVE_INFINITY),
                            new MotionState(profile.getEndTime(), profile.getEndPosition(), goalVelocity, Double.NEGATIVE_INFINITY)
                    ));
                    profile.consolidate();
                    return profile;
                }

                // adjust max acceleration
                maxAcceleration = Math.abs(goalVelocity * goalVelocity - start.getVelocity() * start.getVelocity()) / (2.0 * dPosition);
            } else {
                final double stopTime = Math.abs(start.getVelocity() / constraints.getMaxAbsoluteAcceleration());
                profile.appendControl(-constraints.getMaxAbsoluteAcceleration(), stopTime);
                profile.appendProfile(generateFlippedProfile(constraints, goal, profile.getEndingState()));
                profile.consolidate();
                return profile;
            }
        }

        goalVelocity = Math.min(goalVelocity, maxAbsVelAtGoal);

        final double vMax = Math.min(constraints.getMaxAbsoluteVelocity(), Math.sqrt(
           start.getVelocity() * start.getVelocity() + goalVelocity * goalVelocity
        ) / 2.0 + dPosition * maxAcceleration);

        if (vMax > start.getVelocity()) {
            final double accelerationTime = (vMax - start.getVelocity()) / maxAcceleration;
            profile.appendControl(maxAcceleration, accelerationTime);
            start = profile.getEndingState();
        }

        final double decelerationDistance = Math.max(0.0,
                (start.getVelocity() * start.getVelocity() - goalVelocity * goalVelocity) / (2.0 * constraints.getMaxAbsoluteAcceleration()));
        final double cruiseDistance = Math.max(0.0, goal.getPosition() - start.getPosition() - decelerationDistance);
        if (cruiseDistance > 0.0) {
            final double cruiseTime = cruiseDistance / start.getVelocity();
            profile.appendControl(0.0, cruiseTime);
            start = profile.getEndingState();
        }

        if (decelerationDistance > 0.0) {
            final double decelerationTime = (start.getVelocity() - goalVelocity) / maxAcceleration;
            profile.appendControl(-maxAcceleration, decelerationTime);
        }

        profile.consolidate();
        return profile;
    }
}
