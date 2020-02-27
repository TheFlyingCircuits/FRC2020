package frc.lib.geometry;

import frc.lib.util.Interpolable;

public interface IRotation2<S> extends Interpolable<S> {
    Rotation2 getRotation();
}
