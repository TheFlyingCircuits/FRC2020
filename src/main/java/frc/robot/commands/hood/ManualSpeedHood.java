package frc.robot.commands.hood;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.Utils;
import frc.lib.command.CommandBase;
import frc.lib.util.PID;
import frc.robot.Constants;
import frc.robot.subsystems.AimingHood;
import frc.robot.subsystems.Control;
import frc.robot.subsystems.RobotTracker;

public final class ManualSpeedHood extends CommandBase {

    private final AimingHood aimingHood = AimingHood.getInstance();
    private final Joystick rightJoystick = Control.getInstance().getRight();

    private final PID hoodPID = new PID();

    private double targetPosition;

    public ManualSpeedHood() {
        super(AimingHood.class);
    }

    @Override
    public void init() {
    }

    @Override
    public void tick() {
        // get the joystick value
        final double sliderValue = rightJoystick.getX();

        SmartDashboard.putNumber("sliderValue", sliderValue);

        // set the control value
        aimingHood.setOutput(sliderValue);
    }

    @Override
    public void end(boolean interrupted) {
        // stop hood
        aimingHood.setOutput(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
