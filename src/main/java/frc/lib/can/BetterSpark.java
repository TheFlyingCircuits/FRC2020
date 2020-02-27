package frc.lib.can;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import frc.robot.Constants;

public class BetterSpark extends CANSparkMax {
    private double lastSet = Double.NaN;
    private ControlType lastType = null;

    public BetterSpark(int device) {
        super(device, MotorType.kBrushless);
        this.getEncoder().setVelocityConversionFactor(1.0);
        this.getEncoder().setPositionConversionFactor(1.0);
        this.setSmartCurrentLimit(Constants.CURRENT_LIMIT);
    }

    public BetterSpark(int device, MotorType type) {
        super(device, type);
        this.getEncoder().setVelocityConversionFactor(1.0);
        this.getEncoder().setPositionConversionFactor(1.0);
        this.setSmartCurrentLimit(Constants.CURRENT_LIMIT);
    }

    public void set(ControlType type, double value) {
        if (value != lastSet || type != lastType) {
            lastSet = value;
            lastType = type;
            super.getPIDController().setReference(value, type);
        }
    }
}
