package frc.lib.geometry;

import frc.lib.util.Interpolable;

public interface ITranslation2<S> extends State<S>, Interpolable<S> {
    Translation2 getTranslation();
}
