package io.github.mathfx.cartesian.part;

import java.util.Objects;

public interface PartitionScheme {

    Parcel[] EMPTY_PARCEL = new Parcel[0];

    enum Type {
        MINOR,
        MAJOR,
        ORIGIN
    }

    final class Parcel {
        private Type   type;
        private double pos;

        public Parcel (
            final Type   pType,
            final double pPos)
        {
            type = Objects.requireNonNull (pType);
            pos  = pPos;
        }

        public Type getType () {
            return type;
        }

        public double getPos () {
            return pos;
        }
    }

    Parcel[] partition (
        double iBegin,
        double iEnd,
        double fBegin,
        double fEnd,
        double ppu);



}
