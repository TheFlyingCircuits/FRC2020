package frc.robot.subsystems;

import com.google.common.collect.ImmutableList;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.can.BetterSpark;
import frc.lib.scheduling.Loop;
import frc.lib.scheduling.Scheduler;
import frc.lib.subsystem.CommandSubsystem;
import frc.lib.util.DriveSignal;
import frc.robot.Constants;
import lombok.Getter;

import java.util.List;

public final class DriveTrain extends CommandSubsystem {

    // Signal drive train instance
    private static DriveTrain instance;

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }

        return instance;
    }

    private final IO io;
    private final BetterSpark primaryRight, primaryLeft, secondaryRight, secondaryLeft, tertiaryRight, tertiaryLeft;
    private final CANEncoder rightEncoder, leftEncoder;
    private final List<BetterSpark> right, left, all;

    public DriveTrain() {
        super("Drive");

        // Initialize IO handler
        this.io = new IO();

        // Initialize motor controllers
        this.primaryRight = new BetterSpark(Constants.R_CH_1);
        this.primaryLeft = new BetterSpark(Constants.L_CH_1);
        this.secondaryRight = new BetterSpark(Constants.R_CH_2);
        this.secondaryLeft = new BetterSpark(Constants.L_CH_2);
        this.tertiaryRight = new BetterSpark(Constants.R_CH_3);
        this.tertiaryLeft = new BetterSpark(Constants.L_CH_3);

        // create easy lists for batch motor configuration
        this.right = ImmutableList.of(primaryRight, secondaryRight, tertiaryRight);
        this.left = ImmutableList.of(primaryLeft, secondaryLeft, tertiaryLeft);
        this.all = ImmutableList.of(primaryRight, secondaryRight, tertiaryRight, primaryLeft, secondaryLeft, tertiaryLeft);

        // Configure Sparks
        configureSparks();

        // configure encoders
        configureEncoders();

        // Initialize encoders
        rightEncoder = primaryRight.getEncoder();
        leftEncoder = primaryLeft.getEncoder();

        // set default IO values
        io.idleMode = CANSparkMax.IdleMode.kBrake;
    }

    private void configureSparks() {
        // Invert right side
        right.forEach(ctl -> ctl.setInverted(true));
        left.forEach(ctl -> ctl.setInverted(false));

        // Apply current limit
        all.forEach(ctl -> ctl.setSmartCurrentLimit(Constants.CURRENT_LIMIT));

        // set default brake mode
        all.forEach(ctl -> ctl.setIdleMode(CANSparkMax.IdleMode.kBrake));
    }

    private void configureEncoders() {
        // TODO determine encoder inversion
//        right.forEach(ctl -> ctl.getEncoder().setInverted(false));
//        left.forEach(ctl -> ctl.getEncoder().setInverted(false));

        all.forEach(ctl -> {
            ctl.getEncoder().setPositionConversionFactor(1.0);
            ctl.getEncoder().setVelocityConversionFactor(1.0);
        });

        // reset encoder positions
        all.forEach(ctl -> {
           ctl.getEncoder().setPosition(0);
        });
    }

    public void sendSignal(DriveSignal signal) {
        io.rightOutput = signal.getRight();
        io.leftOutput = signal.getLeft();
    }

    @Override
    public void check() {
        all.forEach(ctl -> ctl.set(0));
    }

    public void reset() {
        // reset encoder positions
        all.forEach(ctl -> ctl.getEncoder().setPosition(0.0));
    }

    @Override
    public void readIO() {
        io.r1Position = primaryRight.getEncoder().getPosition();
        io.r2Position = secondaryRight.getEncoder().getPosition();
        io.r3Position = tertiaryRight.getEncoder().getPosition();
        io.l1Position = primaryLeft.getEncoder().getPosition();
        io.l2Position = secondaryLeft.getEncoder().getPosition();
        io.l3Position = tertiaryLeft.getEncoder().getPosition();

        io.r1Rate = primaryRight.getEncoder().getVelocity();
        io.r2Rate = secondaryRight.getEncoder().getVelocity();
        io.r3Rate = tertiaryRight.getEncoder().getVelocity();
        io.l1Rate = primaryLeft.getEncoder().getVelocity();
        io.l2Rate = secondaryLeft.getEncoder().getVelocity();
        io.l3Rate = tertiaryLeft.getEncoder().getVelocity();

        io.rightPosition = (io.r1Position + io.r2Position + io.r3Position) / 3.0;
        io.leftPosition = (io.l1Position + io.l2Position + io.l3Position) / 3.0;

        io.rightVelocity = (io.r1Rate + io.r2Rate + io.r3Rate) / 3.0;
        io.leftVelocity = (io.l1Rate + io.l2Rate + io.l3Rate) / 3.0;

        // MAKE SURE TICKS PER REVOLUTION IS CORRECT
    }

    @Override
    public void writeIO() {
        // Write motor values
        right.forEach(ctl -> ctl.set(ControlType.kDutyCycle, io.rightOutput));
        left.forEach(ctl -> ctl.set(ControlType.kDutyCycle, io.leftOutput));

        right.forEach(ctl -> ctl.setIdleMode(io.idleMode));
    }

    @Override
    public void updateDashboard() {
        SmartDashboard.putNumber("rightOutput", io.rightOutput);
        SmartDashboard.putNumber("leftOutput", io.leftOutput);
        SmartDashboard.putNumber("rightPosition", io.rightPosition);
        SmartDashboard.putNumber("leftPosition", io.leftPosition);
        SmartDashboard.putNumber("rightDistance", getRightDistance());
        SmartDashboard.putNumber("leftDistance", getLeftDistance());
        SmartDashboard.putNumber("rightIPS", getRightVelocity());
        SmartDashboard.putNumber("leftIPS", getLeftVelocity());
        SmartDashboard.putNumber("rightVelocity", io.rightVelocity);
        SmartDashboard.putNumber("leftVelocity", io.leftVelocity);

        SmartDashboard.putNumber("rightPosition1", io.r1Position);
        SmartDashboard.putNumber("rightPosition2", io.r2Position);
        SmartDashboard.putNumber("rightPosition3", io.r3Position);
        SmartDashboard.putNumber("leftPosition1", io.l1Position);
        SmartDashboard.putNumber("leftPosition2", io.l2Position);
        SmartDashboard.putNumber("leftPosition3", io.l3Position);

        SmartDashboard.putNumber("rightRate1", io.r1Rate);
        SmartDashboard.putNumber("rightRate2", io.r2Rate);
        SmartDashboard.putNumber("rightRate3", io.r3Rate);
        SmartDashboard.putNumber("leftRate1", io.l1Rate);
        SmartDashboard.putNumber("leftRate2", io.l2Rate);
        SmartDashboard.putNumber("leftRate3", io.l3Rate);
    }

    public double getRightVelocity() {
        return getRightRPS() * Constants.wheelDiameterA * Math.PI;
    }

    public double getLeftVelocity() {
        return getLeftRPS() * Constants.wheelDiameterA * Math.PI;
    }

    public double getLeftRPS() {
        return io.leftVelocity * Constants.VTR;
    }

    public double getRightRPS() {
        return io.rightVelocity * Constants.VTR;
    }

    public double getRightRotations() {
        return io.rightPosition * Constants.RPT;
    }

    public double getLeftRotations() {
        return io.leftPosition * Constants.RPT;
    }

    public double getRightDistance() {
        return getRightRotations() * Constants.DRIVE_WHEEL_CIRCUMFERENCE;
    }

    public double getLeftDistance() {
        return getLeftRotations() * Constants.DRIVE_WHEEL_CIRCUMFERENCE;
    }

    public double getLinearVelocity() {
        return (getRightVelocity() + getLeftVelocity()) / 2.0;
    }

    @Override
    public void registerLoops(Scheduler scheduler) {
        scheduler.register(new DriveLoop());
    }

    public IO getIO() {
        return this.io;
    }

    private final class DriveLoop implements Loop {

        @Override
        public void onStart(double timestamp) {
            leftEncoder.setPosition(0);
            rightEncoder.setPosition(0);
        }

        @Override
        public void tick(double timestamp) {
//            io.rightOutput = 0.1;
//            io.leftOutput = 0.1;
        }

        @Override
        public void onStop(double timestamp) {
            leftEncoder.setPosition(0);
            rightEncoder.setPosition(0);
            RobotTracker.getInstance().reset();
        }
    }

    @Getter public static class IO {
        private double timestamp;
        private double rightOutput, leftOutput;
        private double rightPosition, leftPosition;
        private double rightVelocity, leftVelocity;

        private double r1Position, r2Position, r3Position, l1Position, l2Position, l3Position;
        private double r1Rate, r2Rate, r3Rate, l1Rate, l2Rate, l3Rate;
        private CANSparkMax.IdleMode idleMode;
        private int tpr;
    }
}
