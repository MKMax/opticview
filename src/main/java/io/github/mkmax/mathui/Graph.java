package io.github.mkmax.mathui;

import io.github.mkmax.fxutil.ResizableCanvas;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import org.joml.Matrix3d;
import org.joml.Rectangled;
import org.joml.Vector2dc;

public class Graph extends ResizableCanvas {

    private final GraphicsContext graphics    = getGraphicsContext2D ();
    private final Transform       transform   = new Transform ();
    private final Registry        registry    = new Registry ();

    public Graph () {
        /* Property attachments */
        widthProperty ().addListener (this::handleOnWidthChanged);
        heightProperty ().addListener (this::handleOnHeightChanged);

        /* Event listeners */

        /* Setup initial window/viewport */
        transform.setWindow (-1, -1, +1, +1);
        transform.setViewport (this);
    }

    /* +-----------+ */
    /* | Rendering | */
    /* +-----------+ */

    private void render () {

    }

    /* +----------------+ */
    /* | Event Handlers | */
    /* +----------------+ */

    private void handleOnWidthChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        dispatchTotalUpdate ();
    }

    private void handleOnHeightChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        dispatchTotalUpdate ();
    }

    /* Updates the transform, registry, and renders this graph */
    private void dispatchTotalUpdate () {
        transform.setViewport (this);
        render ();
    }

    /* +------------+ */
    /* | Transforms | */
    /* +------------+ */

    private static final class Transform {

        private static void orthographicMap (
            Rectangled from,
            Rectangled to,
            Matrix3d   out)
        {
            /* Compute the X-axis linear mapping */
            double dx = (to.maxX - to.minX) / (from.maxX - from.minX);
            double x0 = to.minX - dx * from.minX;

            /* Compute the Y-axis linear mapping */
            double dy = (to.maxY - to.minY) / (from.maxY - from.minY);
            double y0 = to.minY - dy * from.minY;

            out.set (
                dx, 0d, 0d,
                0d, dy, 0d,
                x0, y0, 1d
            );
        }

        /* The following parameters define the transforms applied going from function -> graph space */
        private final Rectangled window   = new Rectangled (-1, -1, +1, +1);
        private final Rectangled viewport = new Rectangled (-1, -1, +1, +1);

        /* Matrices responsible for translating graph/screen coordinates */
        private final Matrix3d projection = new Matrix3d ();
        private final Matrix3d inverse    = new Matrix3d ();

        public Transform () {
            /* Initial state has already been achieved */
        }

        public Matrix3d getProjection () {
            return projection;
        }

        public Matrix3d getInverse () {
            return inverse;
        }

        public void setWindow (
            double left,
            double right,
            double bottom,
            double top)
        {
            window.minX = left;
            window.maxX = right;
            window.minY = bottom;
            window.maxY = top;
            recompute ();
        }

        public void scaleWindow (
            double sx,
            double sy,
            double ax,
            double ay)
        {
            window.minX = (window.minX - ax) * sx + ax;
            window.minY = (window.minY - ay) * sy + ay;
            window.maxX = (window.maxX - ax) * sx + ax;
            window.maxY = (window.maxY - ay) * sy + ay;
            recompute ();
        }

        public void scaleWindow (
            double    sx,
            double    sy,
            Vector2dc anchor)
        {
            scaleWindow (sx, sy, anchor.x (), anchor.y ());
        }

        public void setViewport (
            double left,
            double right,
            double bottom,
            double top)
        {
            viewport.minX = left;
            viewport.maxX = right;
            viewport.minY = bottom;
            viewport.maxY = top;
            recompute ();
        }

        public void setViewport (Canvas adaptation) {
            setViewport (0, adaptation.getWidth (), adaptation.getHeight (), 0);
        }

        public void recompute () {
            orthographicMap (window, viewport, projection);
            orthographicMap (viewport, window, inverse);
        }
    }
}
