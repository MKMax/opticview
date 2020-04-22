package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.fxe.util.ReactivePane;
import javafx.scene.paint.Color;

public abstract class Guide extends ReactivePane {

    public class Appearance {

        private final double MIN_SIZE  = 1d;

        private boolean visible = true;
        private double  size    = MIN_SIZE;
        private Color   color   = Color.BLACK;

        public boolean isVisible () {
            return visible;
        }

        public void setVisible (boolean nVisible) {
            Guide.this.onVisibilityChanged (
                visible,
                visible = nVisible);
        }

        public double getSize () {
            return size;
        }

        public void setSize (double nSize) {
            Guide.this.onSizeChanged (
                size,
                size = Math.max (MIN_SIZE, nSize));
        }

        public Color getColor () {
            return color;
        }

        public void setColor (Color nColor) {
            Guide.this.onColorChanged (
                color,
                color = nColor == null ? Color.BLACK : nColor);
        }
    }

    private double ortho    = 0d;
    private double parallel = 0d;

    /* +-------------------+ */
    /* | COMMON PROPERTIES | */
    /* +-------------------+ */

    /* APPEARANCE */

    public <T extends Appearance> T getAppearance () {
        return null; /* default implementation */
    }

    /* ORTHO */

    public double getOrtho () {
        return ortho;
    }

    public void setOrtho (double nOrtho) {
        onOrthoChanged (ortho, ortho = nOrtho);
    }

    /* PARALLEL */

    public double getParallel () {
        return parallel;
    }

    public void setParallel (double nParallel) {
        onParallelChanged (parallel, parallel = nParallel);
    }

    /* +--------------------+ */
    /* | OPTIONAL CALLBACKS | */
    /* +--------------------+ */

    protected abstract void onOrthoChanged (double old, double now);

    protected abstract void onParallelChanged (double old, double now);

    /* APPEARANCE */

    protected abstract void onVisibilityChanged (boolean old, boolean now);

    protected abstract void onSizeChanged (double old, double now);

    protected abstract void onColorChanged (Color old, Color now);

}
