package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.math.cartesian.AxisMarker;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

public class Axes2D {

    private ObjectProperty<AxisMarker> xAxis = new SimpleObjectProperty<> ();
    private ObjectProperty<AxisMarker> yAxis = new SimpleObjectProperty<> ();

    public Axes2D (
        AxisMarker pXAxis,
        AxisMarker pYAxis)
    {
        Objects.requireNonNull (pXAxis);
        Objects.requireNonNull (pYAxis);
        xAxis.set (pXAxis);
        yAxis.set (pYAxis);
    }

    /* +---------------------------+ */
    /* | General getters & setters | */
    /* +---------------------------+ */

    public AxisMarker getXAxis () {
        return xAxis.get ();
    }

    public void setXAxis (AxisMarker nXAxis) {
        if (nXAxis != null)
            xAxis.set (nXAxis);
    }

    public ReadOnlyObjectProperty<AxisMarker> xAxisProperty () {
        return xAxis;
    }

    public AxisMarker getYAxis () {
        return yAxis.get ();
    }

    public void setYAxis (AxisMarker nYAxis) {
        if (nYAxis != null)
            yAxis.set (nYAxis);
    }

    public ReadOnlyObjectProperty<AxisMarker> yAxisProperty () {
        return yAxis;
    }
}
