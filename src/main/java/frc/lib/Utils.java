package frc.lib;

public final class Utils {
    public static final double EPSILON = 1E-12;

    public static final double PERIOD = 0.01;

    private Utils() {}

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double deadzone(double value) {
        return deadzone(value, EPSILON);
    }

    public static double deadzone(double value, double epsilon) {
        if (Math.abs(value) < epsilon)
            return 0;
        else return value;
    }

    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }

    public static boolean epsilonEquals(double a, double b) {
        return epsilonEquals(a, b, EPSILON);
    }

    public static double limit(double d, double magnitude) {
        return limit(d, -magnitude, magnitude);
    }

    public static double limit(double d, double min, double max) {
        return Math.min(max, Math.max(min, d));
    }
}
