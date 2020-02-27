package frc.lib.path;

import frc.lib.geometry.Translation2;
import frc.lib.motion.*;
import frc.robot.Constants;
import lombok.Getter;

import java.util.Optional;

public abstract class PathSegment {
    @Getter protected final Translation2 start, end;
    @Getter protected double maxSpeed;
    @Getter protected final MotionProfile profile;
    private boolean extrapolateLookahead = false;

    PathSegment(Translation2 start, Translation2 end, double maxSpeed, MotionState startingState, double endSpeed) {
        this.start = start;
        this.end = end;
        this.maxSpeed = maxSpeed;
        this.profile = createMotionProfiler(startingState, endSpeed);
    }

    public abstract boolean isLine();
    public abstract double getLength();

    public abstract Translation2 getClosestPoint(Translation2 position);
    protected abstract Translation2 pointByDistance(double distance);

    public final Translation2 getPointByDistance(double distance) {
        double length = getLength();
        if (extrapolateLookahead && distance > length) {
            distance = length;
        }

        return pointByDistance(distance);
    }

    public abstract double getRemainingDistance(Translation2 position);

    public final double getDistanceTravelled(Translation2 robotPosition) {
        Translation2 pathPosition = getClosestPoint(robotPosition);
        double remaining = getRemainingDistance(pathPosition);
        return getLength() - remaining;
    }

    public double getSpeedByDistance(double distance) {
        if (distance < profile.getStartPosition()) {
            distance = profile.getStartPosition();
        } else if (distance > profile.getEndPosition()) {
            distance = profile.getEndPosition();
        }

        Optional<MotionState> state = profile.getStateAtPosition(distance);
        if (state.isPresent()) {
            return state.get().getVelocity();
        } else {
            System.out.println("velocity doesn't exist at that position");
            return 0.0;
        }
    }

    private MotionProfile createMotionProfiler(MotionState startState, double endSpeed) {
        MotionProfileConstraints constraints = new MotionProfileConstraints(maxSpeed, /* MARK constant */ Constants.MAX_ACCEL);
        MotionProfileGoal goal = new MotionProfileGoal(getLength(), endSpeed);
        return Motion.generateProfile(constraints, goal, startState);
    }

    public final double getSpeedByClosestPoint(Translation2 point) {
        return getSpeedByDistance(getDistanceTravelled(point));
    }

    public final MotionState getEndingState() {
        return profile.getEndingState();
    }

    public final MotionState getStartingState() {
        return profile.getStartingState();
    }

}
