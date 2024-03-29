package frc.robot.subsystems;

import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.can.BetterSpark;
import frc.lib.scheduling.Loop;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.Constants;
import lombok.Getter;

public final class Shooter extends CommandSubsystem {

    private static Shooter instance;

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }

        return instance;
    }

    private final BetterSpark A = new BetterSpark(Constants.SHOOTER_CH_1), B = new BetterSpark(Constants.SHOOTER_CH_2), accelerator = new BetterSpark(Constants.ACCEL_CH);
    @Getter private final IO io;

    private Shooter() {
        super("Power Cell Shooter");
        A.setInverted(true);
        B.setInverted(false);

        this.io = new IO();
    }

    @Override
    public void check() {

    }

    @Override
    public void readIO() {
        io.aRate = A.getEncoder().getVelocity();
        io.bRate = B.getEncoder().getVelocity();
        io.aFeedback = A.getAppliedOutput();
        io.bFeedback = B.getAppliedOutput();
    }

    @Override
    public void writeIO() {
        A.set(ControlType.kDutyCycle, io.aOutput);
        B.set(ControlType.kDutyCycle, io.bOutput);
        accelerator.set(ControlType.kDutyCycle, io.accelOutput);
    }

    public void setFlywheelSpeed(double value) {
        io.aOutput = value;
        io.bOutput = value;
    }

    public double getRate() {
        // they should have the same rotational velocity
        // this may be unnecessary, but just in case there is some difference
        return (io.aRate + io.bRate) / 2.0;
    }

    public void setAcceleratorSpeed(double speed) {
        io.accelOutput = speed;
    }

    @Override
    public void updateDashboard() {
        SmartDashboard.putNumber("aFeedback", io.aFeedback);
        SmartDashboard.putNumber("aRate", io.aRate);
        SmartDashboard.putNumber("aOutput", io.aOutput);
        SmartDashboard.putNumber("bFeedback", io.bFeedback);
        SmartDashboard.putNumber("bRate", io.bRate);
        SmartDashboard.putNumber("bOutput", io.bOutput);
    }

    @Override
    public void registerLoops(Scheduler scheduler) {
        scheduler.register(new EnabledLoop());
    }

    private final class EnabledLoop implements Loop {

        @Override
        public void onStart(double timestamp) {
            io.aOutput = 0.0;
            io.bOutput = 0.0;
        }

        @Override
        public void tick(double timestamp) {

        }

        @Override
        public void onStop(double timestamp) {
            io.aOutput = 0.0;
            io.bOutput = 0.0;
        }
    }

    @Getter private static class IO {
        private double aOutput = 0.0, bOutput = 0.0;
        private double aRate = 0.0, bRate = 0.0;
        private double aFeedback = 0.0, bFeedback = 0.0;
        private double maxVelocity = 1000;
        private double accelOutput = 0.0;
    }

}
