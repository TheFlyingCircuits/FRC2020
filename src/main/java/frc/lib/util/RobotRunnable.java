package frc.lib.util;

import frc.robot.Robot;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

public abstract class RobotRunnable implements Runnable {
    @Override
    public final void run() {
        try {
            onRun();
        } catch (Throwable t) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            t.printStackTrace(writer);
            Robot.log(Level.SEVERE, stringWriter.toString());
            throw t;
        }
    }

    public abstract void onRun();
}
