package frc.robot.commands.drive;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.lib.util.DriveSignal;
import frc.robot.subsystems.DriveTrain;

public final class Stopped extends CommandBase {

    private final DriveTrain driveTrain = DriveTrain.getInstance();

    public Stopped() {
        super(DriveTrain.class);
    }

    @Override
    public void init() {
        driveTrain.sendSignal(new DriveSignal(0.0 ,0.0));
    }

    @Override
    public void tick() {
        driveTrain.sendSignal(new DriveSignal(0.0 ,0.0));
    }

    @Override
    public void end(boolean interrupted) {
        driveTrain.sendSignal(new DriveSignal(0.0 ,0.0));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
