package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.geometry.Pose2;
import frc.lib.geometry.Rotation2;
import frc.lib.geometry.Twist2;
import frc.lib.scheduling.Loop;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.Subsystem;
import frc.robot.Constants;
import frc.robot.RobotState;
import lombok.Getter;

public class RobotTracker extends Subsystem {

    private static RobotTracker instance;

    private final RobotState state;

    private final DriveTrain driveTrain = DriveTrain.getInstance();
    private final AHRS navX;

    private double lastLeftDistance = 0.0;
    private double lastRightDistance = 0.0;
    private double lastTimestamp = 0.0;
    private Rotation2 lastHeading = Rotation2.IDENTITY;
    private Rotation2 offset = Rotation2.IDENTITY;

    public static RobotTracker getInstance() {
        if (instance == null) {
            instance = new RobotTracker();
        }

        return instance;
    }

    @Getter private final IO io;

    private RobotTracker() {
        super("Path Tracking");
        this.io = new IO();

        // Initialize navX
        this.navX = new AHRS();

        // initialize robot state
        this.state = new RobotState();

        // initialize offset
        offset = Rotation2.IDENTITY.rotate(Rotation2.fromDegrees(navX.getFusedHeading()).inverse());
    }

    @Override
    public void readIO() {
        // Update gyro values
        io.heading = Rotation2.fromDegrees(navX.getFusedHeading()).rotate(offset);
    }

    @Override
    public void writeIO() {

    }

    @Override
    public void updateDashboard() {
        SmartDashboard.putString("heading", io.heading.toString());
        SmartDashboard.putString("pose", state.getLatestPose().getValue().toString());
        SmartDashboard.putNumber("calcLeft", io.calcLeft);
        SmartDashboard.putNumber("calcRight", io.calcRight);
        SmartDashboard.putNumber("dt", io.dt);
    }

    @Override
    public void registerLoops(Scheduler scheduler) {
        scheduler.register(new TrackingLoop());
    }

    public void tick() {
        DriveTrain driveTrain = DriveTrain.getInstance();
    }

    @Override
    public void check() {

    }

    @Getter private static final class IO {

        private Rotation2 heading = Rotation2.IDENTITY;
        private double calcLeft = 1.0, calcRight = 1.0;
        private double dt = 1.0;

        private IO() {}

    }

    public void reset() {
        lastLeftDistance = 0.0;
        lastRightDistance = 0.0;
        lastHeading = Rotation2.IDENTITY;
        offset = Rotation2.IDENTITY.rotate(Rotation2.fromDegrees(navX.getFusedHeading()).inverse());
        state.reset(Timer.getFPGATimestamp(), Pose2.IDENTITY);
    }

    public double getDT() {
        return io.dt;
    }

    private final class TrackingLoop implements Loop {

        @Override
        public void onStart(double timestamp) {
            lastRightDistance = driveTrain.getRightDistance();
            lastLeftDistance = driveTrain.getLeftDistance();
            lastTimestamp = timestamp;
        }

        @Override
        public void tick(double timestamp) {
            final double dt = timestamp - lastTimestamp;
            final double right = driveTrain.getRightDistance();
            final double left = driveTrain.getLeftDistance();
            final double dRight = right - lastRightDistance;
            final double dLeft = left - lastLeftDistance;
            final double vR = driveTrain.getRightVelocity();
            final double vL = driveTrain.getLeftVelocity();
            final double tR = driveTrain.getIO().getRightVelocity();
            final double tL = driveTrain.getIO().getLeftVelocity();
            final Rotation2 theta = io.heading;

            final double dTheta = lastHeading.inverse().rotate(theta).getRadians();

            io.dt = dt;

            final Twist2 change = new Twist2((vL + vR) / 2, 0.0, dTheta);

            state.addObservation(timestamp, change, theta, dt);

            lastRightDistance = right;
            lastLeftDistance = left;
            lastTimestamp = timestamp;
            lastHeading = theta;
        }

        @Override
        public void onStop(double timestamp) {}
    }
}
