package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import com.revrobotics.EncoderType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.can.BetterSpark;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.Subsystem;
import frc.robot.Constants;

public final class AimingHood extends Subsystem {

    private final IO io = new IO();
    private final BetterSpark hoodControl = new BetterSpark(Constants.HOOD_CH, MotorType.kBrushed);
    private final CANEncoder encoder = new CANEncoder(hoodControl, EncoderType.kQuadrature, 8192);

    public AimingHood() {
        super("Shooter Hood");
    }

    @Override
    public void check() {
        encoder.setPositionConversionFactor(1.0);
    }

    @Override
    public void readIO() {
        io.position = encoder.getPosition();
    }

    @Override
    public void writeIO() {
        hoodControl.set(ControlType.kDutyCycle, io.output);
    }

    @Override
    public void updateDashboard() {
        SmartDashboard.putNumber("hood.position", io.position);
        SmartDashboard.putNumber("hood.output", io.output);
    }

    @Override
    public void registerLoops(Scheduler scheduler) {

    }

    private static final class IO {
        private double output, position;

        private IO() {}
    }
}
