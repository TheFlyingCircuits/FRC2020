package frc.robot.commands.hood;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.Utils;
import frc.lib.command.CommandBase;
import frc.lib.math.Vision;
import frc.lib.util.PID;
import frc.robot.Constants;
import frc.robot.subsystems.AimingHood;
import frc.robot.subsystems.Control;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.RobotTracker;

public final class TargetHood extends CommandBase {

    private final AimingHood aimingHood = AimingHood.getInstance();
    private final Limelight limelight = Limelight.getInstance();
    private final Joystick rightJoystick = Control.getInstance().getRight();

    private final PID hoodPID = new PID();

    private double targetPosition;

    public TargetHood() {
        super(AimingHood.class, Limelight.class);
    }

    @Override
    public void init() {
        // stop hood
        aimingHood.setOutput(0.0);
    }

    @Override
    public void tick() {
        // get vertical offset, in radians from the target using vision processing
        final double vertAngleError = limelight.getTargetVerticalOffset();

        // get distance from target
        final double distance = Vision.calculateDistance(Constants.LIMELIGHT_HEIGHT, Constants.OUTER_PORT_CENTER_HEIGHT,
                Math.toRadians(Constants.LIMELIGHT_MOUNT_ANGLE), vertAngleError);

        // get angle offset
        final double angle = Vision.calculateAngleFromDistance(Constants.OUTER_PORT_CENTER_HEIGHT, Constants.LIMELIGHT_HEIGHT,
                distance, Constants.FLYWHEEL_RPS);

        // get target position
        this.targetPosition = aimingHood.getPositionByAngle(angle);

        // update pid
        hoodPID.tick(RobotTracker.getInstance().getDT(), this.targetPosition - aimingHood.getHoodPosition());

        // get setpoint for hood
        final double setpoint = hoodPID.getSetpoint();

        // DEBUG display hood output on SmartDashboard
        SmartDashboard.putNumber("ManualHood.setpoint", setpoint);

        // scale the setpoint within an acceptable range
        final double scaledSetpoint = setpoint / Math.abs(Constants.HOOD_TICK_RANGE);

        // set hood motor output
        aimingHood.setOutput(Utils.clamp(scaledSetpoint, -0.3,0.3));

    }

    @Override
    public void end(boolean interrupted) {
        // stop hood
        aimingHood.setOutput(0.0);
    }

    @Override
    public boolean isFinished() {
        // make sure the hood doesn't get stuck
        if (aimingHood.getOutputCurrent() > Constants.HOOD_TICK_RANGE) {
            return true;
        }

        return false;
    }
}
