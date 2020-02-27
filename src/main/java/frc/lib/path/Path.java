package frc.lib.path;

import java.util.List;

public class Path {

    private List<PathSegment> segments;
    private PathSegment previousSegment;

    public Path() {}

    public void addSegment(PathSegment segment) {
        segments.add(segment);
    }
}
