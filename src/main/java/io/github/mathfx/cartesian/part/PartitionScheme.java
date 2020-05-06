package io.github.mathfx.cartesian.part;

import java.util.List;
import java.util.Objects;

public interface PartitionScheme {

    enum Type {
        MINOR,
        MAJOR,
        ORIGIN
    }

    final class Index {
        public final Type   type;
        public final double pos;

        public Index (Type pType, double pPos) {
            type = Objects.requireNonNull (pType);
            pos  = pPos;
        }
    }

    List<Index> partition (
        double iBegin,
        double iEnd,
        double fBegin,
        double fEnd,
        double ppu);

}
