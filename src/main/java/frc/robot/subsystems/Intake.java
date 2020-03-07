package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import frc.lib.can.BetterSpark;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.Constants;

public final class Intake extends CommandSubsystem {

    private static Intake instance;

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }

        return instance;
    }

    private final BetterSpark intakeInner = new BetterSpark(Constants.INTAKE_INNER_CH);
    private final BetterSpark intakeFront = new BetterSpark(Constants.INTAKE_FRONT_CH);

    private final Solenoid intakeExtend = new Solenoid(Constants.INTAKE_EXTEND_CH);
    private final Solenoid intakeRetract = new Solenoid(Constants.INTAKE_RETRACT_CH);

    private final IO io = new IO();

    public Intake() {
        super("Intake");
    }

    @Override
    public void check() {

    }

    @Override
    public void readIO() {

    }

    @Override
    public void writeIO() {
        intakeExtend.set(io.extend);
        intakeRetract.set(!io.extend);
        intakeFront.set(io.intakeSpeed);
        intakeInner.set(io.intakeSpeed);
    }

    public void setExtended(boolean extended) {
        io.extend = extended;
    }

    public void setIntakeSpeed(double speed) {
        io.intakeSpeed = speed;
    }

    @Override
    public void updateDashboard() {

    }

    @Override
    public void registerLoops(Scheduler scheduler) {

    }

    private final class IO {
        private boolean extend = false;
        private double intakeSpeed = 0.0;
    }


}
