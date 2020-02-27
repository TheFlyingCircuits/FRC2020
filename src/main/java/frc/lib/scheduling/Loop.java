package frc.lib.scheduling;

public interface Loop {
    void onStart(double timestamp);
    void tick(double timestamp);
    void onStop(double timestamp);
}
