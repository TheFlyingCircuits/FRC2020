package frc.lib.subsystem;

import frc.lib.scheduling.Scheduler;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class Subsystem {

    @Getter private final String name;

    public Subsystem(String name) {
        this.name = name;
    }

    public abstract void check();

    // Organized IO
    public abstract void readIO();
    public abstract void writeIO();

    // SmartDashboard updates
    public abstract void updateDashboard();

    // Loop stuff
    public abstract void registerLoops(Scheduler scheduler);

    public static <S extends Subsystem> S getInstance(Class<S> subsystemClass) {
        try {
            Method method = getInstanceMethod(subsystemClass);
            return (S) method.invoke(subsystemClass);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new SubsystemError("error loading subsystem " + subsystemClass.getName(), e.getCause());
        }
    }

    private static Method getInstanceMethod(Class<? extends Subsystem> subsystemClass) throws NoSuchMethodException {
        final Method getInstance = subsystemClass.getDeclaredMethod("getInstance");

        // Verify method type
        if (!subsystemClass.isAssignableFrom(getInstance.getReturnType())) {
            throw new SubsystemError("subsystem does not provide a get instance method");
        }

        // make sure method is static
        if (!Modifier.isStatic(getInstance.getModifiers())) {
            throw new SubsystemError("subsystem instance method must be static");
        }

        return getInstance;
    }
}
