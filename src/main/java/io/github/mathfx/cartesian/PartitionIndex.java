package io.github.mathfx.cartesian;

import java.util.Objects;

public class PartitionIndex {

    public final PartitionType type;
    public final double        pos;

    public PartitionIndex (PartitionType pType, double pPos) {
        type = Objects.requireNonNull (pType);
        pos  = pPos;
    }

}
