package io.github.mkmax.util.math.geo;

import java.util.Objects;

public class DelegateBox2d implements ReadBox2d {

    private final MutableBox2d source;

    public DelegateBox2d (MutableBox2d pSource) {
        source = Objects.requireNonNull (pSource);
    }

    @Override
    public double getLeft () {
        return source.getLeft ();
    }

    @Override
    public double getRight () {
        return source.getRight ();
    }

    @Override
    public double getBottom () {
        return source.getBottom ();
    }

    @Override
    public double getTop () {
        return source.getTop ();
    }
}
