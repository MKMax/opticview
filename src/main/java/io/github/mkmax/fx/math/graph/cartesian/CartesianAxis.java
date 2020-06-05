package io.github.mkmax.fx.math.graph.cartesian;

import java.util.Objects;

public abstract class CartesianAxis {

    protected final CartesianGraph parent;

    public CartesianAxis (CartesianGraph pParent) {
        parent = Objects.requireNonNull (pParent);
    }
}
