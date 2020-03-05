package frc.lib.math;

public final class Vision {

    public static double calculateAngleOffset(final double imageWidth, final double deltaX, final double horizontalFOV) {
        return Math.atan2(deltaX * imageWidth / 2.0, Math.tan(horizontalFOV / 2.0));
    }


    public static double calculateDistance(final double cameraHeight, final double targetHeight, final double mountAngle, final double calculatedAngle) {
        return (targetHeight - cameraHeight) / Math.tan(mountAngle + calculatedAngle);
    }

    public static double calculateAngleFromDistance(final double targetHeight, final double sourceHeight,
                                                    final double distance, final double initialVelocity) {
        final double g = -9.8 * 39.370079; // in/s

        final double v0 = initialVelocity;
        final double d = distance;
        final double hR = sourceHeight;
        final double hT = targetHeight;

        final double A = (d * d + hR * hR - 2 * hR * hT + hT * hT);
        final double B = Math.sqrt(-g * g * d * d - 2 * g * hR * v0 * v0 + v0 * v0 * v0 * v0);
        final double C = (-g * hR + g * hT + v0 * v0);

        final double D = Math.sqrt(
                (C + B) / A
        );

        return Math.atan2( ( (1 / ( 2.0 * A )) * ((C + B) * (-2 * hR + 2 * hT)) - g) / (
                D * v0
                ), D * d / v0);
    }

    private Vision() {}
}
