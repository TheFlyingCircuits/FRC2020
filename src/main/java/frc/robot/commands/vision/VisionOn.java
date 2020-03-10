package frc.robot.commands.vision;

import frc.lib.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Limelight.LEDMode;

public final class VisionOn extends CommandBase {

    private final Limelight limelight = Limelight.getInstance();

    public VisionOn() {
        super(Limelight.class);
    }

    @Override
    public void init() {
        limelight.setLEDMode(LEDMode.ON);
    }

    @Override
    public void tick() {
        limelight.setLEDMode(LEDMode.ON);
    }

    @Override
    public void end(boolean interrupted) {
        limelight.setLEDMode(LEDMode.OFF);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
