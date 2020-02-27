package frc.lib.subsystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.command.CommandScheduler;
import frc.lib.scheduling.Loop;
import frc.lib.scheduling.Scheduler;
import frc.robot.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class SubsystemManager implements Scheduler {

    private static SubsystemManager instance;

    public static SubsystemManager getInstance() {
        if (instance == null) {
            instance = new SubsystemManager();
        }

        return instance;
    }

    private final List<Subsystem> subsystems = new ArrayList<>();
    private final List<Loop> loops = new ArrayList<>();

    private SubsystemManager() {}

    public void loadSubsystem(Subsystem subsystem) {
        Robot.log(Level.INFO, "Loading subsystem %s...", subsystem);

        try {
            // check subsystem status
            subsystem.check();

            // add to list of loaded subsystems
            subsystems.add(subsystem);
        } catch (Exception e) {
            Robot.log(Level.SEVERE, "Failed to load subsystem %s!", subsystem.getName());
        }

        if (subsystem instanceof CommandSubsystem) {
            CommandScheduler.getInstance().registerSubsystem((CommandSubsystem) subsystem);
        }
    }

    public void writeIO() {
        for (Subsystem subsystem : subsystems) {
            subsystem.writeIO();
        }
    }

    public void readIO() {
        for (Subsystem subsystem : subsystems) {
            subsystem.readIO();
        }
    }

    public void updateDashboard() {
        // test update
        SmartDashboard.putNumber("timestampy", Timer.getFPGATimestamp());

        for (Subsystem subsystem : subsystems) {
            subsystem.updateDashboard();
        }
    }

    @Override
    public void register(Loop loop) {
        loops.add(loop);
    }

    public void registerLoops(Scheduler enabledScheduler, Scheduler disabledScheduler) {
        subsystems.forEach(subsystem -> subsystem.registerLoops(this));
        enabledScheduler.register(new EnabledLoop());
        disabledScheduler.register(new DisabledLoop());
    }

    private final class EnabledLoop implements Loop {

        @Override
        public void onStart(double timestamp) {
            loops.forEach(loop -> loop.onStart(timestamp));
        }

        @Override
        public void tick(double timestamp) {
            subsystems.forEach(Subsystem::readIO);
            loops.forEach(loop -> loop.tick(timestamp));
            subsystems.forEach(Subsystem::writeIO);
        }

        @Override
        public void onStop(double timestamp) {
            loops.forEach(loop -> loop.onStop(timestamp));
        }
    }

    private final class DisabledLoop implements Loop {

        @Override
        public void onStart(double timestamp) {}

        @Override
        public void tick(double timestamp) {
            subsystems.forEach(Subsystem::readIO);
        }

        @Override
        public void onStop(double timestamp) {}
    }
}
