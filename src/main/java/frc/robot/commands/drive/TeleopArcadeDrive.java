package frc.robot.commands.drive;

import frc.lib.command.CommandBase;
import frc.lib.util.DriveSignal;
import frc.robot.subsystems.Control;
import frc.robot.subsystems.DriveTrain;

public final class TeleopArcadeDrive extends CommandBase {

    public TeleopArcadeDrive() {
        super(DriveTrain.class);
    }

    @Override
    public void init() {
        DriveTrain.getInstance().sendSignal(DriveSignal.NEUTRAL);
    }

    @Override
    public void tick() {
        final Control control = Control.getInstance();

        // get raw inputs
        final double x = control.getDriveX();
        final double y = control.getDriveY();

        // get brake mode control
        final boolean brakeMode = false;

        // calculate motor outputs
        final double left = y + x;
        final double right = y - x;

        // create drive signal
        final DriveSignal output = new DriveSignal(left, right, brakeMode);

        // set outputs
        DriveTrain.getInstance().sendSignal(output);
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
