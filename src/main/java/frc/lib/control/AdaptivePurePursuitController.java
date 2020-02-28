package frc.lib.control;

import frc.lib.geometry.Pose2;
import frc.lib.geometry.Rotation2;
import frc.lib.geometry.Translation2;
import frc.lib.geometry.Twist2;
import frc.lib.path.Path;
import lombok.Getter;

public class AdaptivePurePursuitController {

    private static final double BIG_NUMBER = 1E6;

    @Getter private final Path path;
    @Getter private final boolean reversed;
    @Getter private final Lookahead lookahead;
    @Getter private boolean atEndOfPath = false;

    public AdaptivePurePursuitController(Path path, boolean reversed, Lookahead lookahead) {
        this.path = path;
        this.reversed = reversed;
        this.lookahead = lookahead;
    }

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

        public Command(Twist2 delta, double crossTrackError, double maxVelocity, double endVelocity, Translation2 lookaheadPoint, double remainingPathLength) {
            this.delta = delta;
            this.crossTrackError = crossTrackError;
            this.maxVelocity = maxVelocity;
            this.endVelocity = endVelocity;
            this.remainingPathLength = remainingPathLength;
            this.lookaheadPoint = lookaheadPoint;
        }
    }

    public Command update(Pose2 robotPose) {
        if (reversed) {
            robotPose = new Pose2(robotPose.getTranslation(),
                    robotPose.getRotation().rotate(Rotation2.fromRadians(Math.PI)));
        }

        final Path.TargetPointReport target = path.getTargetPoint(robotPose.getTranslation(), lookahead);

        if (isFinished()) {
            // stop moving
            return new Command(Twist2.IDENTITY, target.getClosestPointDistance(), target.getMaxSpeed(),
                    0.0, target.getLookaheadPoint(), target.getRemainingPathDistance());
        }

        final Arc arc = new Arc(robotPose, target.getLookaheadPoint());
        double scaleFactor = 1.0;
        if (target.getLookaheadSpeed() < 1E-6 && target.getRemainingPathDistance() < arc.length) {
            scaleFactor = Math.max(0.0, target.getRemainingPathDistance() / arc.length);
            atEndOfPath = true;
        } else {
            atEndOfPath = false;
        }

        if (reversed) {
            scaleFactor *= -1;
        }

        return new Command(
                new Twist2(
                        scaleFactor * arc.length, 0.0,
                        arc.length * getDirection(robotPose, target.getLookaheadPoint()) * Math.abs(scaleFactor) / arc.radius
                ),
                target.getClosestPointDistance(),
                target.getMaxSpeed(),
                target.getLookaheadSpeed() * Math.signum(scaleFactor),
                target.getLookaheadPoint(),
                target.getRemainingPathDistance()
        );
    }

    public static class Arc {
        private Translation2 center;
        private double radius;
        private double length;

        public Arc(Pose2 pose, Translation2 point) {
            center = AdaptivePurePursuitController.getCenter(pose, point);
            radius = new Translation2(center, point).norm();
            length = AdaptivePurePursuitController.getLength(pose, point, center, radius);
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

    public static double getLength(Pose2 pose, Translation2 point) {
        final double radius = getRadius(pose, point);
        final Translation2 center = getCenter(pose, point);
        return getLength(pose, point, center, radius);
    }

    public static int getDirection(Pose2 pose, Translation2 point) {
        Translation2 poseToPoint = new Translation2(pose.getTranslation(), point);
        Translation2 robot = pose.getRotation().toTranslation2();
        double cross = robot.getX() * poseToPoint.getY() - robot.getY() * poseToPoint.getX();
        return (cross < 0) ? -1 : 1;
    }

    public boolean isFinished() {
        return atEndOfPath;
    }
}
