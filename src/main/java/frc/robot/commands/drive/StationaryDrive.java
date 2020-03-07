package frc.robot.commands.drive;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.lib.util.DriveSignal;
import frc.robot.subsystems.DriveTrain;

public final class StationaryDrive extends CommandBase {

    private final DriveTrain driveTrain = DriveTrain.getInstance();

    public StationaryDrive() {
        super(DriveTrain.class);
    }

    @Override
    public void init() {
        driveTrain.sendSignal(new DriveSignal(0.0 ,0.0, true));
    }

    @Override
    public void tick() {
        driveTrain.sendSignal(new DriveSignal(0.0 ,0.0, true));
    }

    @Override
    public void end(boolean interrupted) {
        driveTrain.sendSignal(new DriveSignal(0.0 ,0.0, true));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
