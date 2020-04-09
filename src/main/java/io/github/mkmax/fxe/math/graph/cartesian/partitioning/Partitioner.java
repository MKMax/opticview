package io.github.mkmax.fxe.math.graph.cartesian.partitioning;

public interface Partitioner {

    interface Consumer {
        void accept (
            double  intervalPosition,
            double  fragmentPosition,
            boolean major,
            boolean overlap,
            int     instance
        );
    }

    void partition (
        double   intervalBegin,
        double   intervalEnd,
        double   fragmentBegin,
        double   fragmentEnd,
        double   fragmentsPerUnit,
        Consumer consumer);

}
