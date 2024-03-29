package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.Utils;
import frc.lib.command.CommandBase;
import frc.lib.util.DriveSignal;
import frc.lib.util.PID;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Limelight.LEDMode;
import frc.robot.subsystems.RobotTracker;

public final class FaceTarget extends CommandBase {

    private final Limelight limelight = Limelight.getInstance();
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    private final PID pid = new PID();

    public FaceTarget() {
        super(DriveTrain.class, Limelight.class);
    }

    @Override
    public void init() {
        limelight.setLEDMode(LEDMode.ON);
        driveTrain.sendSignal(new DriveSignal(0, 0, true));
        System.out.println("starting face");
    }

    @Override
    public void tick() {
        // get the current angle error
        final double error = limelight.getTargetHorizontalOffset();

        // tick the pid loop with angle error
        pid.tick(RobotTracker.getInstance().getDT(), error);

        // scale the output to within a reasonable range
        final double output = pid.getSetpoint() / 30.0;

        // clamp the output
        final double setpoint = Utils.clamp(output, -0.3, 0.3);

        SmartDashboard.putNumber("FaceTarget.setpoint", setpoint);

        // get the output
        DriveSignal turnSignal = new DriveSignal(setpoint, -setpoint, true);

        // apply output to drive train
        driveTrain.sendSignal(turnSignal);
    }

    @Override
    public void end(boolean interrupted) {
        limelight.setLEDMode(LEDMode.OFF);
        driveTrain.sendSignal(new DriveSignal(0, 0, true));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
