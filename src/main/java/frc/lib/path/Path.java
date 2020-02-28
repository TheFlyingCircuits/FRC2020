package frc.lib.path;

import frc.lib.control.AdaptivePurePursuitController;
import frc.lib.control.AdaptivePurePursuitController.Lookahead;
import frc.lib.geometry.Translation2;
import lombok.Data;

import java.util.List;

public class Path {

    private List<PathSegment> segments;
    private PathSegment previousSegment;

    public Path() {}

    public void addSegment(PathSegment segment) {
        segments.add(segment);
    }

    @Data
    public static final class TargetPointReport {
        private Translation2 closestPoint;
        private double closestPointDistance, closestPointSpeed;
        private Translation2 lookaheadPoint;
        private double lookaheadSpeed;
        private double remainingSegmentDistance, remainingPathDistance;
        private double maxSpeed;
    }

    public TargetPointReport getTargetPoint(Translation2 robot, Lookahead lookahead) {
        TargetPointReport target = new TargetPointReport();
        PathSegment currentSegment = segments.get(0);

        // update closest points
        target.closestPoint = currentSegment.getClosestPoint(robot);
        target.closestPointDistance = new Translation2(robot, target.closestPoint).norm();

        // update remaining
        target.remainingSegmentDistance = currentSegment.getRemainingDistance(target.closestPoint);
        target.remainingPathDistance = target.remainingSegmentDistance;

        for (int i = 1; i < segments.size(); i++) {
            target.remainingPathDistance += segments.get(i).getLength();
        }

        target.closestPointSpeed = currentSegment.getSpeedByDistance(currentSegment.getLength() - target.remainingSegmentDistance);
        double lookaheadDistance = lookahead.getLookaheadBySpeed(target.closestPointSpeed) + target.closestPointDistance;

        if (target.remainingSegmentDistance < lookaheadDistance && segments.size() > 1) {
            lookaheadDistance -= target.remainingSegmentDistance;
            for (int i = 1; i < segments.size(); ++i) {
                currentSegment = segments.get(i);
                final double length = currentSegment.getLength();
                if (length < lookaheadDistance && i < segments.size() - 1) {
                    lookaheadDistance -= length;
                } else {
                    break;
                }
            }
        } else {
            lookaheadDistance += (currentSegment.getLength() - target.remainingSegmentDistance);
        }

        target.maxSpeed = currentSegment.getMaxSpeed();
        target.lookaheadPoint = currentSegment.getPointByDistance(lookaheadDistance);
        target.lookaheadSpeed = currentSegment.getSpeedByDistance(lookaheadDistance);
        checkSegmentDone(target.closestPoint);
        return target;
    }

    public void checkSegmentDone(Translation2 robot) {
        PathSegment currentSegment = segments.get(0);
        double remainingDistance = currentSegment.getRemainingDistance(currentSegment.getClosestPoint(robot));
        final double segmentCompletionTolerance = 0.1; // inches
        if (remainingDistance < segmentCompletionTolerance) {
            removeCurrentSegment();
        }
    }

    private void removeCurrentSegment() {
        previousSegment = segments.remove(0);
    }
}
