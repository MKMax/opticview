package io.github.mkmax.fxe.math.graph.cartesian.grid;

import javafx.scene.layout.Pane;

import java.util.Objects;

public class Grid extends Pane {

    private AxisMarker x = new DecimalAxisMarker (DecimalAxisMarker.Scheme.FRAGMENTS);
    private AxisMarker y = new DecimalAxisMarker (DecimalAxisMarker.Scheme.FRAGMENTS);

    public AxisMarker getX () {
        return x;
    }

    public void setX (AxisMarker nAxisMarker) {
        x = Objects.requireNonNull (nAxisMarker);
    }

    public AxisMarker getY () {
        return y;
    }

    public void setY (AxisMarker nAxisMarker) {
        y = Objects.requireNonNull (nAxisMarker);
    }
}
