package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.math.Vision;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.Constants;
import lombok.Getter;

public class Limelight extends CommandSubsystem {

    private static Limelight instance;

    public static Limelight getInstance() {
        if (instance == null) {
            instance = new Limelight();
        }

        return instance;
    }

    private final NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    /* NETWORK TABLE ENTRIES */
    private final NetworkTableEntry tx = limelightTable.getEntry("tx");
    private final NetworkTableEntry ty = limelightTable.getEntry("ty");
    private final NetworkTableEntry ta = limelightTable.getEntry("ta");

    /* CONFIG TABLE ENTRIES */
    private final NetworkTableEntry ledMode = limelightTable.getEntry("ledMode");
    private final NetworkTableEntry camMode = limelightTable.getEntry("camMode");

    @Getter private final IO io = new IO();

    private Limelight() {
        super("Limelight 1");
    }

    @Override
    public void check() {

    }

    @Override
    public void readIO() {
        io.tx = tx.getDouble(0.0);
        io.ty = ty.getDouble(0.0);
        io.ta = ta.getDouble(0.0);
    }

    @Override
    public void writeIO() {
        ledMode.setNumber(io.ledMode.getMode());
        camMode.setNumber(io.camMode.getMode());
    }

    @Override
    public void updateDashboard() {
        SmartDashboard.putNumber("tx", io.tx);
        SmartDashboard.putNumber("ty", io.ty);
        SmartDashboard.putNumber("ta", io.ta);
        SmartDashboard.putString("ledMode", io.ledMode.toString());
    }

    @Override
    public void registerLoops(Scheduler scheduler) {

    }

    public void setLEDMode(LEDMode mode) {
        io.ledMode = mode;
    }

    public void setCamMode(CamMode mode) {
        io.camMode = mode;
    }

    public double getDistanceToTarget(double targetHeight) {
        return Vision.calculateDistance(Constants.LIMELIGHT_HEIGHT, targetHeight, Math.toRadians(Constants.LIMELIGHT_MOUNT_ANGLE), Math.toRadians(io.ty));
    }

    public double getTargetHorizontalOffset() {
        return Math.toRadians(io.tx);
    }

    public double getTargetVerticalOffset() {
        return Math.toRadians(io.ty);
    }

    public double getTargetArea() {
        return io.ta;
    }

    @Getter private final class IO {
        private double tx, ty, ta;
        private LEDMode ledMode = LEDMode.PIPELINE;
        private CamMode camMode = CamMode.VISION;
    }

    public static enum LEDMode {
        /**
         * Use the LED mode set in the current pipeline
         */
        PIPELINE(0),
        /**
         * Force the LED to off
         */
        OFF(1),
        /**
         * Force the LED to blink
         */
        BLINK(2),
        /**
         * Force the LED to on
         */
        ON(3);

        @Getter private final int mode;

        LEDMode(int mode) {
            this.mode = mode;
        }

    }

    public static enum CamMode {
        /**
         * Use the Limelight for vision processing
         */
        VISION(0),
        /**
         * Use the Limelight as a plain camera, without vision processing
         */
        CAMERA(1);

        @Getter private final int mode;

        CamMode(int mode) {
            this.mode = mode;
        }

    }
}
