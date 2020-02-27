package frc.lib.subsystem;

public class SubsystemError extends Error {
    public SubsystemError(String message) {
        super(message);
    }

    public SubsystemError(String message, Throwable cause) {
        super(message, cause);
    }
}
