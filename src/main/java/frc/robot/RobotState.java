package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.geometry.Pose2;
import frc.lib.geometry.Rotation2;
import frc.lib.geometry.Translation2;
import frc.lib.geometry.Twist2;
import frc.lib.util.InterpolatingDouble;
import frc.lib.util.InterpolatingTreeMap;

import java.util.Map;

public class RobotState {

    private InterpolatingTreeMap<InterpolatingDouble, Pose2> poses;

    private Twist2 measuredVelocity, predictedVelocity;
    private double distance;

    public RobotState() {
        reset(0.0d, Pose2.IDENTITY);
    }

    public void reset(double time, Pose2 pose) {
        poses = new InterpolatingTreeMap<>(100);
        poses.put(new InterpolatingDouble(time), pose);
        measuredVelocity = Twist2.IDENTITY;
        predictedVelocity = Twist2.IDENTITY;
        distance = 0.0;
    }

    public Map.Entry<InterpolatingDouble, Pose2> getLatestPose() {
        return poses.lastEntry();
    }

    public void addObservation(final double timestamp, final Twist2 change, final Rotation2 theta, final double dt) {
        Pose2 last = getLatestPose().getValue();
        Translation2 lastT = last.getTranslation();
        Rotation2 lastR = last.getRotation();

        final double dx = Math.cos(theta.getRadians()) * change.deltaX() * dt;
        final double dy = Math.sin(theta.getRadians()) * change.deltaX() * dt;
        final double dTheta = change.deltaTheta();

        poses.put(new InterpolatingDouble(timestamp), last.integrate(new Twist2(dx, dy, dTheta)));
    }
}
