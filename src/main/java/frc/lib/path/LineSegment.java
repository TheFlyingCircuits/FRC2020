package frc.lib.path;

import frc.lib.geometry.Translation2;
import frc.lib.motion.MotionState;
import lombok.Getter;

public final class LineSegment extends PathSegment {
    @Getter private final Translation2 dStart;

    public LineSegment(Translation2 start, Translation2 end, double maxSpeed, MotionState startingState, double endSpeed) {
        super(start, end, maxSpeed, startingState, endSpeed);
        this.dStart = new Translation2(start, end);
    }

    @Override
    public boolean isLine() {
        return true;
    }

    @Override
    public double getLength() {
        return dStart.norm();
    }

    @Override
    public Translation2 getClosestPoint(Translation2 position) {
        Translation2 delta = new Translation2(start, end);
        double u = ((position.getX() - start.getX()) * delta.getX() + (position.getY() - start.getY()) * delta.getY())
                / (delta.getX() * delta.getX() + delta.getY() * delta.getY());
        if (u >= 0 && u <= 1)
            return new Translation2(start.getX() + u * delta.getX(), start.getY() + u * delta.getY());
        return (u < 0) ? start : end;
    }

    @Override
    protected Translation2 pointByDistance(double distance) {
        return start.translate(dStart.scale(distance / getLength()));
    }


    @Override
    public double getRemainingDistance(Translation2 position) {
        return new Translation2(end, position).norm();
    }
}
