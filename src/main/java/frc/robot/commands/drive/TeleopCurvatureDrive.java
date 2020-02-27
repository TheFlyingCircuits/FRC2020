package frc.robot.commands.drive;

import frc.lib.Utils;
import frc.lib.command.CommandBase;
import frc.lib.util.DriveSignal;
import frc.robot.Constants;
import frc.robot.subsystems.Control;
import frc.robot.subsystems.DriveTrain;

public final class TeleopCurvatureDrive extends CommandBase {
    private double quickStopThreshold, quickStopAlpha;
    private double quickStopAccumulator;

    private final Control control = Control.getInstance();

    public TeleopCurvatureDrive() {
        this(0.1, 0.9);
    }

    public TeleopCurvatureDrive(final double quickStopThreshold, final double quickStopAlpha) {
        super(DriveTrain.class);
        this.quickStopThreshold = quickStopThreshold;
        this.quickStopAlpha = quickStopAlpha;
    }

    public DriveSignal calculateSignal(final double throttle, final double wheel, final boolean quickTurn) {
        double xSpeed = Utils.clamp(throttle, -1.0, 1.0);
        xSpeed = Utils.deadzone(xSpeed);

        double zRotation = Utils.clamp(wheel, -1.0, 1.0);
        zRotation = Utils.deadzone(zRotation);

        double angularPower;
        boolean overPower;

        if (quickTurn) {
            if (Math.abs(xSpeed) < quickStopThreshold) {
                this.quickStopAccumulator = (1 - quickStopAlpha) * this.quickStopAccumulator + quickStopAlpha
                        * Utils.clamp(zRotation, -1.0, 1.0) * 2;
            }
            overPower = true;
            angularPower = zRotation;
        } else {
            overPower = false;
            angularPower = Math.abs(xSpeed) * zRotation - this.quickStopAccumulator;

            if (this.quickStopAccumulator > 1) {
                this.quickStopAccumulator -= 1;
            } else if (this.quickStopAccumulator < -1) {
                this.quickStopAccumulator += 1;
            } else {
                this.quickStopAccumulator = 0;
            }
        }

        double left = xSpeed + angularPower;
        double right = xSpeed - angularPower;

        if (overPower) {
            if (left > 1.0) {
                right -= left - 1.0;
                left = 1.0;
            } else if (right > 1.0) {
                left -= right - 1.0;
                right = 1.0;
            } else if (left < -1.0) {
                right -= left + 1.0;
            } else if (right < -1.0) {
                left -= right + 1.0;
                right = -1.0;
            }
        }

        double maxMagnitude = Math.max(Math.abs(left), Math.abs(right));

        if (maxMagnitude > 1.0) {
            left /= maxMagnitude;
            right /= maxMagnitude;
        }

        return new DriveSignal(left, right);
    }

    @Override
    public void init() {
        DriveTrain.getInstance().sendSignal(DriveSignal.NEUTRAL);
    }

    @Override
    public void tick() {
        // get raw inputs
        final double turn = -control.getDriveX();
        final double throttle = control.getDriveY();

        // set quick turn
        boolean quickTurn = control.getRight().getRawButton(Constants.QUICKTURN_CH); // TODO control quickturn

        // get drive signal
        final DriveSignal drive = calculateSignal(throttle, turn, quickTurn);

        // send drive signal
        DriveTrain.getInstance().sendSignal(drive);
    }

    @Override
    public void end(boolean interrupted) {
        DriveTrain.getInstance().sendSignal(DriveSignal.BRAKE);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
