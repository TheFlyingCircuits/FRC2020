package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import com.revrobotics.EncoderType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.Utils;
import frc.lib.can.BetterSpark;
import frc.lib.command.CommandScheduler;
import frc.lib.scheduling.Loop;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.Constants;
import frc.robot.commands.hood.IdleHood;
import lombok.Getter;

public final class AimingHood extends CommandSubsystem {

    private static AimingHood instance;

    public static AimingHood getInstance() {
        if (instance == null) {
            instance = new AimingHood();
        }

        return instance;
    }

    private final IO io = new IO();
    private final BetterSpark hoodControl = new BetterSpark(Constants.HOOD_CH, MotorType.kBrushed);
    private final CANEncoder encoder = new CANEncoder(hoodControl, EncoderType.kQuadrature, 8192);

    public AimingHood() {
        super("Shooter Hood");

        // invert hood motor
        hoodControl.setInverted(true);

        // initial calibration
        calibrate();
    }

    public double getHoodPosition() {
        return io.position;
    }

    public double getHoodAngle() {
        return Utils.normalize(getHoodPosition(), Constants.HOOD_TICK_RANGE, 0.0, Constants.HOOD_MIN_ANGLE, Constants.HOOD_MAX_ANGLE);
    }

    /**
     * Gets a target encoder position from a desired hood angle in degrees
     *
     * @param angle the angle in degrees
     * @return the target encoder position
     */
    public double getPositionByAngle(double angle) {
        return Utils.normalize(angle, Constants.HOOD_MIN_ANGLE, Constants.HOOD_MAX_ANGLE, Constants.HOOD_TICK_RANGE, 0.0);
    }

    public void setOutput(double output) {
        io.output = output;
    }

    public void calibrate() {
        // reset the encoder at its current position
        this.encoder.setPosition(0.0);
    }

    @Override
    public void check() {
        // set factors to 1
        encoder.setPositionConversionFactor(1.0);
        encoder.setVelocityConversionFactor(1.0);
    }

    @Override
    public void readIO() {
        io.position = encoder.getPosition();
        io.current = hoodControl.getOutputCurrent();
    }

    @Override
    public void writeIO() {
        hoodControl.set(io.output);
    }

    public double getOutputCurrent() {
        return io.current;
    }

    @Override
    public void updateDashboard() {
        SmartDashboard.putNumber("hood.position", io.position);
        SmartDashboard.putNumber("hood.output", io.output);
        SmartDashboard.putNumber("hood.current", io.current);
        SmartDashboard.putNumber("hood.angle", getHoodAngle());
    }

    @Override
    public void registerLoops(Scheduler scheduler) {
    }

    private static final class IO {
        private double output, position, current;

        private IO() {
        }
    }
}
