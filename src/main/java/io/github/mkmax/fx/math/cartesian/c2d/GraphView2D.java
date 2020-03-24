package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.math.cartesian.AxisMark;
import io.github.mkmax.fx.math.cartesian.AxisMarker;
import io.github.mkmax.fx.math.cartesian.DecimalAxisMarker;
import io.github.mkmax.fx.math.cartesian.c2d.Transform2D.*;
import io.github.mkmax.fx.util.ResizableCanvas;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GraphView2D extends ResizableCanvas {

    /* Predefined event handlers */
        /* grid */
    private final ChangeListener<AxisMarker> XACL     = this::onXYAxisChanged;
    private final ChangeListener<AxisMarker> YACL     = this::onXYAxisChanged;
    private final ChangeListener<Boolean>              MAAPSTL  = this::onXYAxisMAAPToggled;
    private final ChangeListener<Boolean>              MIAPSTL  = this::onXYAxisMIAPToggled;
    private final ChangeListener<Boolean>              MALTL    = this::onXYAxisMajorLabelsToggled;
    private final ChangeListener<Boolean>              MILTL    = this::onXYAxisMinorLabelsToggled;
    private final ChangeListener<Number>               ASNLBCL  = this::onXYAxisSciNotLowerBoundChanged;
    private final ChangeListener<Number>               ASNHBCL  = this::onXYAxisSciNotUpperBoundChanged;
    private final ChangeListener<Number>               MFPUCL   = this::onXYAxisMFPUChanged;
        /* styling */
    private final ChangeListener<Color>                MACCL    = this::onXYAxisMajorColorChanged;
    private final ChangeListener<Color>                MICCL    = this::onXYAxisMinorColorChanged;
        /* javafx */
    private final ChangeListener<Number>               WIDTHCL  = this::onWidthChanged;
    private final ChangeListener<Number>               HEIGHTCL = this::onHeightChanged;
        /* transforms */
    private final RecomputedListener                   TRCL     = this::onTransformRecomputed;

    /* Actual member data */
    private final GraphicsContext graphics = getGraphicsContext2D ();

    private final Transform2D transform;
    private final Registry2D registry;
    private final Style2D style;
    private final Axes2D axes;

    public GraphView2D () {
        /* Initialize members */
        transform = new Transform2D ();
        registry  = new Registry2D ();
        style     = new Style2D ();
        axes      = new Axes2D (
            new DecimalAxisMarker (),
            new DecimalAxisMarker ()
        );

        /* Register event handlers */
        transform.addListener (TRCL);
        axes.xAxisProperty ().addListener (XACL);
        axes.yAxisProperty ().addListener (YACL);
        addAxisListeners (axes.getXAxis ());
        addAxisListeners (axes.getYAxis ());

        widthProperty ().addListener (WIDTHCL);
        heightProperty ().addListener (HEIGHTCL);

        /* Run initial render */
        render ();
    }

    /* +---------------------------+ */
    /* | General getters & setters | */
    /* +---------------------------+ */

    public Transform2D getTransform () {
        return transform;
    }

    public Registry2D getRegistry () {
        return registry;
    }

    public Axes2D getAxes () {
        return axes;
    }

    /* +----------------+ */
    /* | Event handlers | */
    /* +----------------+ */

    /* internal */

    private void removeAxisListeners (AxisMarker cap) {
        if (!(cap instanceof CommonCartesianAxisProfile))
            return;
        CommonCartesianAxisProfile ccap = (CommonCartesianAxisProfile) cap;
        ccap.mfpuProperty ().removeListener (MFPUCL);
        ccap.computeMaapsProperty ().removeListener (MAAPSTL);
        ccap.computeMiapsProperty ().removeListener (MIAPSTL);
        ccap.majorLabelsEnabledProperty ().removeListener (MALTL);
        ccap.minorLabelsEnabledProperty ().removeListener (MILTL);
        ccap.sciNotLowerBoundProperty ().removeListener (ASNLBCL);
        ccap.sciNotUpperBoundProperty ().removeListener (ASNHBCL);
    }

    private void addAxisListeners (AxisMarker cap) {
        if (!(cap instanceof CommonCartesianAxisProfile))
            return;
        CommonCartesianAxisProfile ccap = (CommonCartesianAxisProfile) cap;
        ccap.mfpuProperty ().addListener (MFPUCL);
        ccap.computeMaapsProperty ().addListener (MAAPSTL);
        ccap.computeMiapsProperty ().addListener (MIAPSTL);
        ccap.majorLabelsEnabledProperty ().addListener (MALTL);
        ccap.minorLabelsEnabledProperty ().addListener (MILTL);
        ccap.sciNotLowerBoundProperty ().addListener (ASNLBCL);
        ccap.sciNotUpperBoundProperty ().addListener (ASNHBCL);
    }

    private void onXYAxisChanged (
        ObservableValue<? extends AxisMarker> obs,
        AxisMarker old,
        AxisMarker now)
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

    private void onXYAxisMajorLabelsToggled (
        ObservableValue<? extends Boolean> obs,
        Boolean                            old,
        Boolean                            now)
    {
        render ();
    }

    private void onXYAxisMinorLabelsToggled (
        ObservableValue<? extends Boolean> obs,
        Boolean                            old,
        Boolean                            now)
    {
        render ();
    }

    private void onXYAxisSciNotUpperBoundChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        render ();
    }

    private void onXYAxisSciNotLowerBoundChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
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

    private void onXYAxisMajorColorChanged (
        ObservableValue<? extends Color> obs,
        Color                            old,
        Color                            now)
    {
        render ();
    }

    private void onXYAxisMinorColorChanged (
        ObservableValue<? extends Color> obs,
        Color                            old,
        Color                            now)
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

    private void drawBackground () {
        graphics.setFill (style.getBackground ());
        graphics.fillRect (0, 0, getWidth (), getHeight ());
    }

    private void drawAxis (
        double bx, double by,
        double ex, double ey,
        Color color)
    {
        final double abx = transform.projectX (bx);
        final double aby = transform.projectY (by);
        final double aex = transform.projectX (ex);
        final double aey = transform.projectY (ey);

        graphics.setStroke (color);
        graphics.strokeLine (abx, aby, aex, aey);
    }

    private void drawAxisX (AxisMark point, Color color) {
        drawAxis
    }

    private void drawGrid () {
        /* vertical/x-axis */
        axes
            .getXAxis ()
            .getMajorMarks (
                transform.getWindowRangeX (),
                transform.getViewportRangeX ())
            .forEach (pt -> drawAxisX (pt, style.getMajorAxisColor ()));

        axes
            .getXAxis ()
            .getMinorMarks (
                transform.getWindowRangeX (),
                transform.getViewportRangeX ())
            .forEach (this::drawMinorAxisX);

        /* horizontal/y-axis */
    }

    private void render () {
        drawBackground ();
        drawGrid ();
    }
}
