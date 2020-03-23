package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.math.cartesian.CartesianAxisProfile;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

public class CartesianAxes2D {

    private ObjectProperty<CartesianAxisProfile> xAxis = new SimpleObjectProperty<> ();
    private ObjectProperty<CartesianAxisProfile> yAxis = new SimpleObjectProperty<> ();

    public CartesianAxes2D (
        CartesianAxisProfile pXAxis,
        CartesianAxisProfile pYAxis)
    {
        Objects.requireNonNull (pXAxis);
        Objects.requireNonNull (pYAxis);
        xAxis.set (pXAxis);
        yAxis.set (pYAxis);
    }

    /* +---------------------------+ */
    /* | General getters & setters | */
    /* +---------------------------+ */

    public CartesianAxisProfile getXAxis () {
        return xAxis.get ();
    }

    public void setXAxis (CartesianAxisProfile nXAxis) {
        if (nXAxis != null)
            xAxis.set (nXAxis);
    }

    public ReadOnlyObjectProperty<CartesianAxisProfile> xAxisProperty () {
        return xAxis;
    }

    public CartesianAxisProfile getYAxis () {
        return yAxis.get ();
    }

    public void setYAxis (CartesianAxisProfile nYAxis) {
        if (nYAxis != null)
            yAxis.set (nYAxis);
    }

    public ReadOnlyObjectProperty<CartesianAxisProfile> yAxisProperty () {
        return yAxis;
    }
}
