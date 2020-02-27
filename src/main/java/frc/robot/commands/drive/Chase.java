package frc.robot.commands.drive;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;

public class Chase extends CommandBase {

    private final Limelight limelight = Limelight.getInstance();
    private final DriveTrain driveTrain = DriveTrain.getInstance();

    public Chase() {
        super(Limelight.class, DriveTrain.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
