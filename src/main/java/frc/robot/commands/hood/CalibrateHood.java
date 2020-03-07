package frc.robot.commands.hood;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.robot.Constants;
import frc.robot.subsystems.AimingHood;

public class CalibrateHood extends CommandBase {

    private final AimingHood hood = AimingHood.getInstance();

    public CalibrateHood() {
        super(AimingHood.class);
    }

    @Override
    public void init() {
        hood.setOutput(0.0);
        System.out.println("Hood calibration start...");
    }

    @Override
    public void tick() {
        hood.setOutput(-0.2);
    }

    @Override
    public void end(boolean interrupted) {
        hood.setOutput(0.0);
        System.out.println("Hood calibrated.");
    }

    @Override
    public boolean isFinished() {
        return hood.getOutputCurrent() > Constants.HOOD_CURRENT_STOP;
    }
}
