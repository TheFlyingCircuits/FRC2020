package frc.robot.commands.hood;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.subsystems.AimingHood;

public final class IdleHood extends CommandBase {

    private final AimingHood hood = AimingHood.getInstance();

    public IdleHood() {
        super(AimingHood.class);
    }

    @Override
    public void init() {
        hood.setOutput(0.0);
    }

    @Override
    public void tick() {
        hood.setOutput(0.0);
    }

    @Override
    public void end(boolean interrupted) {
        hood.setOutput(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
