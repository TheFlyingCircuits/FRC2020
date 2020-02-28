package frc.robot.commands.hood;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.Utils;
import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;
import frc.lib.util.PID;
import frc.robot.subsystems.AimingHood;
import frc.robot.subsystems.Control;
import frc.robot.subsystems.RobotTracker;

public class ManualHood extends CommandBase {

    private final AimingHood aimingHood = AimingHood.getInstance();
    private final Joystick rightJoystick = Control.getInstance().getRight();

    private final PID hoodPID = new PID();

    private double targetPosition;

    public ManualHood() {
        super(AimingHood.class);
    }

    @Override
    public void init() {
        // stop hood
        aimingHood.setOutput(0.0);
    }

    @Override
    public void tick() {
        // get the joystick value
        final double sliderValue = rightJoystick.getRawAxis(3);

        // convert to angle within range
        final double targetAngle = Utils.normalize(sliderValue, -1. 1,
                aimingHood.getMinHoodPosition(), aimingHood.getMaxHoodPosition());

        // get target position
        this.targetPosition = aimingHood.getPositionByAngle(targetAngle);

        // update pid
        hoodPID.tick(RobotTracker.getInstance().getDT(), this.targetPosition - aimingHood.getHoodPosition());

        // get setpoint for hood
        final double setpoint = hoodPID.getSetpoint();

        SmartDashboard.putNumber("ManualHood.setpoint", setpoint);

        // set output
        aimingHood.setOutput(setpoint / (aimingHood.getMaxHoodPosition() - aimingHood.getMinHoodPosition()));

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
