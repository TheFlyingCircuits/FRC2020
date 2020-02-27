package frc.lib.math;

public final class Vision {

    public static double calculateAngleOffset(final double imageWidth, final double deltaX, final double horizontalFOV) {
        return Math.atan2(deltaX * imageWidth / 2.0, Math.tan(horizontalFOV / 2.0));
    }

    public static double calculateDistance(final double cameraHeight, final double targetHeight, final double mountAngle, final double calculatedAngle) {
        return (targetHeight - cameraHeight) / Math.tan(mountAngle + calculatedAngle);
    }

    private Vision() {}
}
