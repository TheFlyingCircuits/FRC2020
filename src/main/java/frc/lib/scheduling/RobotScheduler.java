package frc.lib.scheduling;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import frc.lib.Utils;
import frc.lib.util.RobotRunnable;

import java.util.ArrayList;
import java.util.List;

public final class RobotScheduler implements Scheduler {

    private boolean running = false;

    private final List<Loop> registeredLoops = new ArrayList<>();
    private final Notifier notifier;
    private final Object loopLock = new Object();
    private double timestamp;
    private double dt;

    private final RobotRunnable runnable = new RobotRunnable() {
        @Override
        public void onRun() {
            // Retrieve current timestamp
            final double now = Timer.getFPGATimestamp();

            for (Loop loop : registeredLoops) {
                loop.tick(now);
            }

            // Update timestamp
            dt = now - timestamp;
            timestamp = now;
        }
    };

    public RobotScheduler() {
        this.notifier = new Notifier(runnable);
    }

    @Override
    public synchronized void register(Loop loop) {
        synchronized (loopLock) {
            registeredLoops.add(loop);
        }
    }

    public synchronized void start() {
        if (!running) {
            System.out.println("Starting loops");

            synchronized (loopLock) {
                timestamp = Timer.getFPGATimestamp();

                for (Loop loop : registeredLoops) {
                    loop.onStart(timestamp);
                }

                running = true;
            }

            notifier.startPeriodic(Utils.PERIOD);
        }
    }

    public synchronized void stop() {
        if (running) {
            System.out.println("Stopping loops");

            notifier.stop();

            synchronized (loopLock) {
                running = false;
                timestamp = Timer.getFPGATimestamp();

                for (Loop loop : registeredLoops) {
                    loop.onStop(timestamp);
                }
            }
        }
    }
}
