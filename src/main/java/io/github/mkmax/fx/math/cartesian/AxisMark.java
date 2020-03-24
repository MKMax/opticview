package io.github.mkmax.fx.math.cartesian;

import java.util.Objects;

public class AxisMark {

    public enum Type {
        MINOR,
        MAJOR
    }

    public final Type   type;
    public final double unitSpaceLocation;

    public AxisMark (
        Type   pType,
        double pUnitSpaceLocation)
    {
        type              = Objects.requireNonNull (pType);
        unitSpaceLocation = pUnitSpaceLocation;
    }

}
