package frc.lib.geometry;

public interface State<S> {
    double distance(final S other);
    boolean equals(final Object other);
}
