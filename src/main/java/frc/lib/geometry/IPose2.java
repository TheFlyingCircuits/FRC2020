package frc.lib.geometry;

@SuppressWarnings("ClassReferencesSubclass")
public interface IPose2<S> extends IRotation2<S>, ITranslation2<S> {
    Pose2 getPose();

    S transform(Pose2 transform);

    S mirror();
}
