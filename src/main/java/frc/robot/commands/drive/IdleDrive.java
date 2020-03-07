package frc.robot.commands.drive;

import frc.lib.command.CommandBase;
import frc.lib.util.DriveSignal;
import frc.robot.subsystems.DriveTrain;

public final class IdleDrive extends CommandBase {

    private final DriveTrain driveTrain = DriveTrain.getInstance();

    public IdleDrive() {
        super(DriveTrain.class);
    }

    @Override
    public void init() {
        driveTrain.sendSignal(new DriveSignal(0.0 ,0.0, false));
    }

    @Override
    public void tick() {
        driveTrain.sendSignal(new DriveSignal(0.0 ,0.0, false));
    }

    @Override
    public void end(boolean interrupted) {
        driveTrain.sendSignal(new DriveSignal(0.0 ,0.0, false));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
