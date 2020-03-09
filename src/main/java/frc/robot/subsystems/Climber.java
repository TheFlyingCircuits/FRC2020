package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.can.BetterSpark;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.Constants;

public class Climber extends CommandSubsystem {

    private static Climber instance;

    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber();
        }

        return instance;
    }

    private final BetterSpark A = new BetterSpark(Constants.CLIMB_CH_1), B = new BetterSpark(Constants.CLIMB_CH_2);
    private final Solenoid brakeEngage = new Solenoid(Constants.CLIMB_ENGAGE_CH), brakeDisengage = new Solenoid(Constants.CLIMB_DISENGAGE_CH);
    private final IO io = new IO();

    private Climber() {
        super("Climb");
        B.setInverted(true);
    }

    @Override
    public void check() {
        A.setIdleMode(CANSparkMax.IdleMode.kBrake);
        B.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }

    @Override
    public void readIO() {

    }

    @Override
    public void writeIO() {
        A.set(ControlType.kDutyCycle, io.output);
        B.set(ControlType.kDutyCycle, io.output);
        brakeEngage.set(!io.deployed);
        brakeDisengage.set(io.deployed);
    }

    public boolean isDeployed() {
        return io.deployed;
    }

    public void setClimb(double speed) {
        io.output = speed;
    }

    public void setDeployed(boolean deployed) {
        io.deployed = deployed;
    }

    @Override
    public void updateDashboard() {
        SmartDashboard.putBoolean("Climber.Deployed", io.deployed);
        SmartDashboard.putNumber("Climber.ArmSpeed", io.output);
    }

    @Override
    public void registerLoops(Scheduler scheduler) {

    }

    private static class IO {
        private double output = 0.0;
        private boolean deployed = false;
    }
}
