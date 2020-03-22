package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.math.cartesian.CartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.StandardCartesianAxisProfile;

import java.util.ArrayList;
import java.util.List;

public class CartesianGridProfile2D {

    public interface AxisProfileChangeListener {
        void onAxisProfileChanged (
            CartesianAxisProfile old,
            CartesianAxisProfile now);
    }

    private final List<AxisProfileChangeListener> xAxisProfileChangeListeners
        = new ArrayList<> ();
    private final List<AxisProfileChangeListener> yAxisProfileChangeListeners
        = new ArrayList<> ();

    private CartesianAxisProfile xAxis = new StandardCartesianAxisProfile (128d);
    private CartesianAxisProfile yAxis = new StandardCartesianAxisProfile (128d);

    private double scientificNotationUpperBound = 1e3d;
    private double scientificNotationLowerBound = 1e-3d;

    private boolean displayMajorAxisValues = true;
    private boolean displayMinorAxisValues = false;

    public CartesianGridProfile2D (double xMfpu, double yMfpu) {
        xAxis.setMinimumFragmentsPerUnit (xMfpu);
        yAxis.setMinimumFragmentsPerUnit (yMfpu);
    }

    public CartesianGridProfile2D () {
        /* Initial state already achieved. */
    }

    /* +----------------------------------------+ */
    /* | Event registration + handling (if any) | */
    /* +----------------------------------------+ */

    public void registerXAxisChangeListener (AxisProfileChangeListener apcl) {
        if (apcl != null)
            xAxisProfileChangeListeners.add (apcl);
    }

    public void unregisterXAxisChangeListener (AxisProfileChangeListener apcl) {
        if (apcl != null)
            xAxisProfileChangeListeners.remove (apcl);
    }

    public void registerYAxisChangeListener (AxisProfileChangeListener apcl) {
        if (apcl != null)
            yAxisProfileChangeListeners.add (apcl);
    }

    public void unregisterYAxisChangeListener (AxisProfileChangeListener apcl) {
        if (apcl != null)
            yAxisProfileChangeListeners.remove (apcl);
    }

    /* +---------------------------+ */
    /* | General getters & setters | */
    /* +---------------------------+ */

    public CartesianAxisProfile getXAxis () {
        return xAxis;
    }

    public void setXAxis (CartesianAxisProfile nXAxis) {
        if (nXAxis == null)
            return;
        xAxisProfileChangeListeners.forEach (
            listener -> listener.onAxisProfileChanged (xAxis, nXAxis));
        xAxis = nXAxis;
    }

    public CartesianAxisProfile getYAxis () {
        return yAxis;
    }

    public void setYAxis (CartesianAxisProfile nYAxis) {
        if (nYAxis == null)
            return;
        yAxisProfileChangeListeners.forEach (
            listener -> listener.onAxisProfileChanged (yAxis, nYAxis));
        yAxis = nYAxis;
    }
}
