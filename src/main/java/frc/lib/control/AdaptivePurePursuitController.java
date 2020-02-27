package frc.lib.control;

import frc.lib.geometry.Pose2;
import frc.lib.geometry.Rotation2;
import frc.lib.geometry.Translation2;
import frc.lib.geometry.Twist2;
import lombok.Getter;

public class AdaptivePurePursuitController {

    private static final double BIG_NUMBER = 1E6;

//    @Getter private final Lookahead lookahead;

    public AdaptivePurePursuitController() {}


    public static final class Lookahead {
        @Getter private final double minDistance, maxDistance;
        @Getter private final double minSpeed, maxSpeed;

        @Getter private final double deltaDistance, deltaSpeed;

        public Lookahead(double minDistance, double maxDistance, double minSpeed, double maxSpeed) {
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.minSpeed = minSpeed;
            this.maxSpeed = maxSpeed;
            deltaDistance = maxDistance - minDistance;
            deltaSpeed = maxSpeed - minSpeed;
        }

        public double getLookaheadBySpeed(double speed) {
            double lookahead = deltaDistance * (speed - minSpeed) / deltaSpeed + minDistance;
            return Double.isNaN(lookahead) ? minDistance : Math.max(minDistance, Math.min(maxDistance, lookahead));
        }
    }

    @Getter public static final class Command {
        private final Twist2 delta;
        private final double crossTrackError, maxVelocity, endVelocity, remainingPathLength;
        private final Translation2 lookaheadPoint;

        public Command(Twist2 delta, double crossTrackError, double maxVelocity, double endVelocity, double remainingPathLength, Translation2 lookaheadPoint) {
            this.delta = delta;
            this.crossTrackError = crossTrackError;
            this.maxVelocity = maxVelocity;
            this.endVelocity = endVelocity;
            this.remainingPathLength = remainingPathLength;
            this.lookaheadPoint = lookaheadPoint;
        }
    }

    public static Translation2 getCenter(Pose2 pose, Translation2 point) {
        final Translation2 halfway = pose.getTranslation().interpolate(point, 0.5);
        final Rotation2 normal = pose.getTranslation().inverse().translate(halfway).direction().normal();
        final Pose2 bisector = new Pose2(halfway, normal);
        final Pose2 normalPose = new Pose2(pose.getTranslation(), pose.getRotation().normal());
        if (normalPose.isColinear(bisector.normal())) {
            return halfway;
        } else {
            return normalPose.intersection(bisector);
        }
    }

    public static double getRadius(Pose2 pose, Translation2 point) {
        Translation2 center = getCenter(pose, point);
        return new Translation2(center, point).norm();
    }

    public static double getLength(Pose2 pose, Translation2 point, Translation2 center, double radius) {
        if (radius < BIG_NUMBER) {
            final Translation2 centerToPoint = new Translation2(center, point);
            final Translation2 centerToPose = new Translation2(center, pose.getTranslation());

            // if the point is behind the pose, flip the angle
            final boolean behind = Math.signum(
                    Translation2.cross(pose.getRotation().normal().toTranslation2(),
                            new Translation2(pose.getTranslation(), point))) > 0.0;

            final Rotation2 angle = Translation2.getAngle(centerToPose, centerToPoint);
            return radius * (behind ? 2.0 * Math.PI - Math.abs(angle.getRadians()) : Math.abs(angle.getRadians()));
        } else {
            return new Translation2(pose.getTranslation(), point).norm();
        }
    }
}
