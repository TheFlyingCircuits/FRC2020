package frc.lib.motion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static frc.lib.Utils.epsilonEquals;

public final class MotionProfile {
    private final List<MotionSegment> segments;

    public MotionProfile() {
        this(new ArrayList<>());
    }

    public MotionProfile(List<MotionSegment> segments) {
        this.segments = segments;
    }

    public Optional<MotionState> getStateAtTime(double time) {
        // First check if the time would fall outside of the start and end bounds
        if (time < getStartTime() && time + Motion.EPSILON >= getStartTime()) {
            return Optional.of(getStartingState());
        } else if (time > getEndTime() && time - Motion.EPSILON <= getEndTime()) {
            return Optional.of(getEndingState());
        }

        for (MotionSegment segment : segments) {
            if (segment.containsTime(time)) {
                return Optional.of(segment.getStart().extrapolate(time));
            }
        }

        return Optional.empty();
    }

    public Optional<MotionState> getStateAtPosition(double position) {
        for (MotionSegment segment : segments) {
            if (segment.containsPosition(position)) {
                if (epsilonEquals(segment.getStart().getPosition(), position, Motion.EPSILON)) {
                    return Optional.of(segment.getEnd());
                }

                final double time = Math.min(segment.getStart().nextTimeAt(position), segment.getEnd().getTime());
                if (Double.isNaN(time)) {
                    System.err.println("won't reach the position");
                    return Optional.empty();
                }

                return Optional.of(segment.getStart().extrapolate(time));
            }
        }
        // never reach the position
        return Optional.empty();
    }

    public boolean isValid() {
        MotionSegment previous = null;
        for (MotionSegment segment : segments) {
            // If one segment is invalid, the profile is also invalid
            if (!segment.isValid()) {
                return false;
            }

            // Invalid profile if the segments are not continuous
            if (previous != null && !segment.getStart().coincident(previous.getEnd())) {
                System.err.println("segments are not continuous");
                return false;
            }
        }

        return true;
    }

    public void consolidate() {
        for (Iterator<MotionSegment> iterator = segments.iterator(); iterator.hasNext() && segments.size() > 1;) {
            MotionSegment segment = iterator.next();
            if (segment.getStart().coincident(segment.getEnd())) {
                iterator.remove();
            }
        }
    }

    public void appendControl(double acceleration, double dt) {
        if (isEmpty()) {
            System.err.println("cannot append to empty profile");
            return;
        }

        MotionState lastEnd = segments.get(segments.size() - 1).getEnd();
        MotionState newStart = new MotionState(lastEnd.getTime(), lastEnd.getPosition(), lastEnd.getVelocity(), acceleration);
        appendSegment(new MotionSegment(newStart, newStart.extrapolate(newStart.getTime() + dt)));
    }

    public void appendProfile(MotionProfile profile) {
        for (MotionSegment segment : profile.segments) {
            appendSegment(segment);
        }
    }

    public double duration() {
        return getEndTime() - getStartTime();
    }

    public double length() {
        double length = 0.0;
        for (MotionSegment segment : segments) {
            length += Math.abs(segment.getEnd().getPosition() - segment.getStart().getPosition());
        }
        return length;
    }

    public void appendSegment(MotionSegment segment) {
        segments.add(segment);
    }

    public boolean isEmpty() {
        return segments.isEmpty();
    }

    public void clear() {
        this.segments.clear();
    }

    public void reset(MotionState initialState) {
        clear();
        this.segments.add(new MotionSegment(initialState, initialState));
    }

    public MotionState getStartingState() {
        if (isEmpty()) {
            return MotionState.INVALID_STATE;
        }

        return segments.get(0).getStart();
    }

    public MotionState getEndingState() {
        if (isEmpty()) {
            return MotionState.INVALID_STATE;
        }

        return segments.get(segments.size() - 1).getEnd();
    }

    public double getStartTime() {
        return getStartingState().getTime();
    }

    public double getEndTime() {
        return getEndingState().getTime();
    }

    public double getEndPosition() {
        return getEndingState().getPosition();
    }

    public double getStartPosition() {
        return getStartingState().getPosition();
    }

    public double getEndVelocity() {
        return getEndingState().getVelocity();
    }

    public double getStartVelocity() {
        return getStartingState().getVelocity();
    }

    public List<MotionSegment> getSegments() {
        return this.segments;
    }
}
