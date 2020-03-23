package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.math.cartesian.CartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.StandardCartesianAxisProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartesianAxes2D {

    public interface XAxisChangeListener {
        void onXAxisChanged (
            CartesianAxisProfile old,
            CartesianAxisProfile now);
    }

    public interface YAxisChangeListener {
        void onYAxisChanged (
            CartesianAxisProfile old,
            CartesianAxisProfile now);
    }

    private final List<XAxisChangeListener> xAxisChangeListeners = new ArrayList<> ();
    private final List<YAxisChangeListener> yAxisChangeListeners = new ArrayList<> ();

    private CartesianAxisProfile xAxis = new StandardCartesianAxisProfile ();
    private CartesianAxisProfile yAxis = new StandardCartesianAxisProfile ();

    public CartesianAxes2D () {
        /* initial state already reached */
    }

    /* +--------------------------------------+ */
    /* | Registration/Unregistering utilities | */
    /* +--------------------------------------+ */

    public void register (XAxisChangeListener xacl) {
        if (xacl != null)
            xAxisChangeListeners.add (xacl);
    }

    public void unregister (XAxisChangeListener xacl) {
        if (xacl != null)
            xAxisChangeListeners.remove (xacl);
    }

    public void register (YAxisChangeListener yacl) {
        if (yacl != null)
            yAxisChangeListeners.add (yacl);
    }

    public void unregister (YAxisChangeListener yacl) {
        if (yacl != null)
            yAxisChangeListeners.remove (yacl);
    }

    /* +---------------------------+ */
    /* | General getters & setters | */
    /* +---------------------------+ */

    public CartesianAxisProfile getXAxis () {
        return xAxis;
    }

    public void setXAxis (CartesianAxisProfile nXAxis) {
        if (nXAxis == null || nXAxis == xAxis)
            return;
        CartesianAxisProfile old = xAxis;
        xAxis = nXAxis;
        xAxisChangeListeners.forEach (l -> l.onXAxisChanged (old, xAxis));
    }

    public CartesianAxisProfile getYAxis () {
        return yAxis;
    }

    public void setYAxis (CartesianAxisProfile nYAxis) {
        if (nYAxis == null || nYAxis == yAxis)
            return;
        CartesianAxisProfile old = yAxis;
        yAxis = nYAxis;
        yAxisChangeListeners.forEach (l -> l.onYAxisChanged (old, yAxis));
    }
}
