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

public class TargetHood extends CommandBase {

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

        SmartDashboard.putNumber("ManualHood.setpoint", setpoint);

        // set output
        aimingHood.setOutput(setpoint / (aimingHood.getMaxHoodPosition() - aimingHood.getMinHoodPosition()));

    }

    @Override
    public void end(boolean interrupted) {
        // stop hood
        aimingHood.setOutput(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
