package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import com.revrobotics.EncoderType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.Utils;
import frc.lib.can.BetterSpark;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.CommandSubsystem;
import frc.lib.subsystem.Subsystem;
import frc.lib.util.PID;
import frc.robot.Constants;
import lombok.Getter;

public final class AimingHood extends CommandSubsystem {

    private static AimingHood instance;

    public static AimingHood getInstance() {
        if (instance == null) {
            instance = new AimingHood();
        }

        return instance;
    }

    // should be calibrated
    @Getter private double maxHoodPosition, minHoodPosition;

    private final IO io = new IO();
    private final BetterSpark hoodControl = new BetterSpark(Constants.HOOD_CH, MotorType.kBrushed);
    private final CANEncoder encoder = new CANEncoder(hoodControl, EncoderType.kQuadrature, 8192);

    public AimingHood() {
        super("Shooter Hood");
    }

    public double getHoodPosition() {
        return io.output;
    }

    public double getHoodAngle() {
        return Utils.normalize(getHoodPosition(), minHoodPosition, maxHoodPosition, Constants.HOOD_MIN_ANGLE, Constants.HOOD_MAX_ANGLE);
    }

    public double getPositionByAngle(double angle) {
        return Utils.normalize(angle, Constants.HOOD_MIN_ANGLE, Constants.HOOD_MAX_ANGLE, minHoodPosition, maxHoodPosition);
    }

    public void setOutput(double output) {
        io.output = output;
    }

    @Override
    public void check() {
        encoder.setPositionConversionFactor(1.0);
        encoder.setVelocityConversionFactor(1.0);
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

        private IO() {
        }
    }
}
