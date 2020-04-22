package io.github.mkmax.fxe.math.graph.cartesian.partitioning;

public interface Partition {

    enum Type {
        MINOR,
        MAJOR,
        ORIGIN
    }

    interface Receiver {
        void rcv (Type type, double pos);
    }

    void calc (
        Receiver rcv,
        double   intervalBegin,
        double   intervalEnd,
        double   fragmentBegin,
        double   fragmentEnd,
        double   fpu);

}