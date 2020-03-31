package io.github.mkmax.fxe.math.graph.cartesian.axes;

import java.util.Objects;

public class AxisMark {

    public enum Type {
        MINOR,
        MAJOR
    }

    public final Type    type;
    public final double  location;
    public final boolean overlapPossible;

    public AxisMark (
        Type    pType,
        double  pLocation,
        boolean pOverlapPossible)
    {
        type            = Objects.requireNonNull (pType);
        location        = pLocation;
        overlapPossible = pOverlapPossible;
    }

}
