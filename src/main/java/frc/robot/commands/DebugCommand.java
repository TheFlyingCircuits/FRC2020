package frc.robot.commands;

import frc.lib.command.CommandBase;
import frc.lib.subsystem.CommandSubsystem;

public class DebugCommand extends CommandBase {

    private final String id;

    public DebugCommand(String id) {
        super();
        this.id = id;
    }

    @Override
    public void init() {
        System.out.println(String.format("DBG %s: init", id));
    }

    @Override
    public void tick() {
        System.out.println(String.format("DBG %s: tick", id));
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println(String.format("DBG %s: end", id));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
