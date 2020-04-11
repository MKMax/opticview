package io.github.mkmax.fxe.math.graph.cartesian;

import java.util.List;

public interface SpacePartitioner {

    List<Double> major (
        double intervalBegin,
        double intervalEnd,
        double fragmentBegin,
        double fragmentEnd,
        double fpu);

    List<Double> minor (
        double intervalBegin,
        double intervalEnd,
        double fragmentBegin,
        double fragmentEnd,
        double fpu);

}