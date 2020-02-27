package frc.lib.path;

import frc.lib.geometry.Rotation2;
import frc.lib.geometry.Translation2;
import frc.lib.motion.MotionState;
import lombok.Getter;

public final class ArcSegment extends PathSegment {
    @Getter private final Translation2 center, dStart, dEnd;

    public ArcSegment(Translation2 start, Translation2 end, Translation2 center, double maxSpeed, MotionState startingState, double endSpeed) {
        super(start, end, maxSpeed, startingState, endSpeed);
        this.center = center;
        this.dStart = new Translation2(center, start);
        this.dEnd = new Translation2(center, end);
    }

    @Override
    public boolean isLine() {
        return false;
    }

    @Override
    public double getLength() {
        return dStart.norm() * Translation2.getAngle(dStart, dEnd).getRadians();
    }

    @Override
    public Translation2 getClosestPoint(Translation2 position) {
        Translation2 dPosition = new Translation2(center, position);
        dPosition = dPosition.scale(dStart.norm() / dPosition.norm());
        if (Translation2.cross(dPosition, dStart) * Translation2.cross(dPosition, dEnd) < 0) {
            return center.translate(dPosition);
        } else {
            Translation2 startDist = new Translation2(position, start);
            Translation2 endDist = new Translation2(position, end);
            return (endDist.norm() < startDist.norm()) ? end : start;
        }
    }

    @Override
    protected Translation2 pointByDistance(double distance) {
        double deltaAngle = Translation2.getAngle(dStart, dEnd).getRadians()
                * ((Translation2.cross(dStart, dEnd) >= 0) ? 1 : -1);

        deltaAngle *= distance / getLength();
        Translation2 t = dStart.rotate(Rotation2.fromRadians(deltaAngle));
        return center.translate(t);
    }

    @Override
    public double getRemainingDistance(Translation2 position) {
        Translation2 dPosition = new Translation2(center, position);
        double angle = Translation2.getAngle(dEnd, dPosition).getRadians();
        double totalAngle = Translation2.getAngle(dStart, dEnd).getRadians();
        return angle / totalAngle * getLength();
    }
}
