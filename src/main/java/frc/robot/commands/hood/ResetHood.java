package frc.robot.commands.hood;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.subsystems.AimingHood;

public class ResetHood extends CommandBase {

    private final AimingHood aimingHood = AimingHood.getInstance();

    public ResetHood() {
        super(AimingHood.class);
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
