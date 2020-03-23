package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.math.cartesian.CartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.CommonCartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.StandardCartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.c2d.CartesianTransform2D.*;
import io.github.mkmax.fx.util.ResizableCanvas;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.scene.canvas.GraphicsContext;

public class CartesianGraphView2D extends ResizableCanvas {

    /* Predefined event handlers */
    private final RecomputedListener                   TRCL     = this::onTransformRecomputed;
    private final ChangeListener<CartesianAxisProfile> XACL     = this::onXYAxisChanged;
    private final ChangeListener<CartesianAxisProfile> YACL     = this::onXYAxisChanged;
    private final ChangeListener<Boolean>              MAAPSTL  = this::onXYAxisMAAPToggled;
    private final ChangeListener<Boolean>              MIAPSTL  = this::onXYAxisMIAPToggled;
    private final ChangeListener<Number>               MFPUCL   = this::onXYAxisMFPUChanged;

    private final ChangeListener<Number>               WIDTHCL  = this::onWidthChanged;
    private final ChangeListener<Number>               HEIGHTCL = this::onHeightChanged;

    /* Actual member data */
    private final GraphicsContext graphics = getGraphicsContext2D ();

    private final CartesianTransform2D transform;
    private final CartesianRegistry2D registry;
    private final CartesianAxes2D axes;

    public CartesianGraphView2D () {
        /* Initialize members */
        transform = new CartesianTransform2D ();
        registry  = new CartesianRegistry2D ();
        axes      = new CartesianAxes2D (
            new StandardCartesianAxisProfile (),
            new StandardCartesianAxisProfile ()
        );

        /* Register event handlers */
        transform.addListener (TRCL);
        addAxisListeners (axes.getXAxis ());
        addAxisListeners (axes.getYAxis ());
        axes.xAxisProperty ().addListener (XACL);
        axes.yAxisProperty ().addListener (YACL);

        widthProperty ().addListener (WIDTHCL);
        heightProperty ().addListener (HEIGHTCL);

        /* Run initial render */
        render ();
    }

    /* +---------------------------+ */
    /* | General getters & setters | */
    /* +---------------------------+ */

    public CartesianTransform2D getTransform () {
        return transform;
    }

    public CartesianRegistry2D getRegistry () {
        return registry;
    }

    public CartesianAxes2D getAxes () {
        return axes;
    }

    /* +----------------+ */
    /* | Event handlers | */
    /* +----------------+ */

    /* internal */

    private void removeAxisListeners (CartesianAxisProfile cap) {
        if (!(cap instanceof CommonCartesianAxisProfile))
            return;
        CommonCartesianAxisProfile ccap = (CommonCartesianAxisProfile) cap;
        ccap.mfpuProperty ().removeListener (MFPUCL);
        ccap.computeMaapsProperty ().removeListener (MAAPSTL);
        ccap.computeMiapsProperty ().removeListener (MIAPSTL);
    }

    private void addAxisListeners (CartesianAxisProfile cap) {
        if (!(cap instanceof CommonCartesianAxisProfile))
            return;
        CommonCartesianAxisProfile ccap = (CommonCartesianAxisProfile) cap;
        ccap.mfpuProperty ().addListener (MFPUCL);
        ccap.computeMaapsProperty ().addListener (MAAPSTL);
        ccap.computeMiapsProperty ().addListener (MIAPSTL);
    }

    private void onXYAxisChanged (
        ObservableValue<? extends CartesianAxisProfile> obs,
        CartesianAxisProfile                            old,
        CartesianAxisProfile                            now)
    {
        removeAxisListeners (old);
        addAxisListeners (now);
        render ();
    }

    private void onXYAxisMAAPToggled (
        ObservableValue<? extends Boolean> obs,
        Boolean                            old,
        Boolean                            now)
    {
        render ();
    }

    private void onXYAxisMIAPToggled (
        ObservableValue<? extends Boolean> obs,
        Boolean                            old,
        Boolean                            now)
    {
        render ();
    }

    private void onXYAxisMFPUChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        render ();
    }

    private void onTransformRecomputed () {
        render ();
    }

    /* javafx/ui */

    private void onWidthChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        transform.setViewport (getWidth (), getHeight ());
    }

    private void onHeightChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        transform.setViewport (getWidth (), getHeight ());
    }

    /* +--------------------+ */
    /* | Internal functions | */
    /* +--------------------+ */

    private void render () {
        System.out.println ("Rendering");
    }
}
