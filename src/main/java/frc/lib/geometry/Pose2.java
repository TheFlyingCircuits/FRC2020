package frc.lib.geometry;

import frc.lib.Utils;

public class Pose2 implements IPose2<Pose2> {
    public static final Pose2 IDENTITY = new Pose2();

    private static final double EPSILON = 1E-9;

    private final Translation2 translation;
    private final Rotation2 rotation;

    public Pose2() {
        this(new Translation2(), new Rotation2());
    }

    public Pose2(Pose2 pose) {
        this(pose.translation, pose.rotation);
    }

    public Pose2(final double x, final double y, Rotation2 rotation) {
        this(new Translation2(x, y), rotation);
    }

    public Pose2(Translation2 translation, Rotation2 rotation) {
        this.translation = translation;
        this.rotation = rotation;
    }

    @Override
    public Translation2 getTranslation() {
        return this.translation;
    }

    @Override
    public Rotation2 getRotation() {
        return this.rotation;
    }

    @Override
    public Pose2 getPose() {
        return this;
    }

    @Override
    public Pose2 transform(final Pose2 other) {
        return new Pose2(this.translation.translate(other.translation.rotate(this.rotation)),
                rotation.rotate(other.rotation));
    }

    public Pose2 inverse() {
        Rotation2 inverseRotation = this.rotation.inverse();
        return new Pose2(this.translation.inverse().rotate(inverseRotation), inverseRotation);
    }

    public Pose2 normal() {
        return new Pose2(this.translation, this.rotation.normal());
    }

    public Translation2 intersection(final Pose2 other) {
        final Rotation2 otherRotation = other.getRotation();

        if (this.rotation.isParallel(otherRotation)) {
            return new Translation2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        if (Math.abs(this.rotation.cos()) < Math.abs(otherRotation.cos())) {
            return intersectionInternal(this, other);
        } else {
            return intersectionInternal(other, this);
        }
    }


    public boolean isColinear(final Pose2 other) {
        if (!this.rotation.isParallel(other.rotation)) {
            return false;
        }

        final Twist2 twist = log(this.inverse().transform(other));
        return Utils.epsilonEquals(twist.deltaY(), 0.0d)
                && Utils.epsilonEquals(twist.deltaTheta(), 0.0d);
    }

    public boolean epsilonEquals(final Pose2 other, double epsilon) {
        return this.translation.epsilonEquals(other.getTranslation(), epsilon)
                && this.rotation.isParallel(other.getRotation());
    }

    public static Pose2 exp(final Twist2 delta) {
        double sin = Math.sin(delta.deltaTheta());
        double cos = Math.cos(delta.deltaTheta());
        double s, c;

        if (Math.abs(delta.deltaTheta()) < Pose2.EPSILON) {
            s = 1.0d - 1.0d/6.0d * delta.deltaTheta() * delta.deltaTheta();
            c = delta.deltaTheta() / 2;
        } else {
            s = sin / delta.deltaTheta();
            c = (1.0d - cos) / delta.deltaTheta();
        }

        return new Pose2(new Translation2(delta.deltaX() * s - delta.deltaY() * c,
                delta.deltaX() * c + delta.deltaY() * s),
                new Rotation2(cos, sin, false));
    }

    public static Twist2 log(final Pose2 transform) {
        final double dtheta = transform.getRotation().getRadians();
        final double half_dtheta = dtheta / 2;
        final double cos_minus_one = transform.getRotation().cos() - 1.0d;

        double htht;

        if (Math.abs(cos_minus_one) < Pose2.EPSILON) {
            htht = 1.0d - 1.0d / 12.0d * dtheta * dtheta;
        } else {
            htht = -(half_dtheta * transform.getRotation().sin()) / cos_minus_one;
        }
        final Translation2 transPart = transform.getTranslation()
                .rotate(new Rotation2(htht, -half_dtheta, false));

        return new Twist2(transPart.getX(), transPart.getY(), dtheta);
    }

    private static Translation2 intersectionInternal(final Pose2 a, final Pose2 b) {
        final Rotation2 ar = a.rotation;
        final Translation2 at = a.translation;
        final Translation2 bt = b.translation;

        final double tanb = b.rotation.tan();
        final double t = ((at.getX() - bt.getX()) * tanb + bt.getY() - at.getY())
                / (ar.sin() - ar.cos() * tanb);

        if (Double.isNaN(t)) {
            return new Translation2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        return at.translate(ar.toTranslation2().scale(t));
    }

    @Override
    public Pose2 mirror() {
        return new Pose2(new Translation2(this.translation.getX(), -this.translation.getY()), this.rotation.inverse());
    }

    @Override
    public Pose2 interpolate(Pose2 other, double p) {
        if (p <= 0) {
            return new Pose2(this);
        } else if (p >= 1) {
            return new Pose2(other);
        }
        final Twist2 twist = Pose2.log(this.inverse().transform(other));
        return this.transform(Pose2.exp(twist.scale(p)));
    }

    @Override
    public String toString() {
        return String.format("(%sin, %sin) with angle %s", this.translation.getX(), this.translation.getY(), this.rotation.toString());
    }

    @Override
    public double distance(Pose2 other) {
        return Pose2.log(this.inverse().transform(other)).norm();
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Pose2)) {
            return false;
        }

        return epsilonEquals((Pose2) other, Utils.EPSILON);
    }

    public Pose2 integrate(Twist2 change) {
        return new Pose2(this.translation.translate(new Translation2(change.deltaX(), change.deltaY())),
                this.rotation.rotate(Rotation2.fromRadians(change.deltaTheta())));
    }
}
