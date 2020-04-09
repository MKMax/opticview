package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.util.math.LinearInterpolatord;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.joml.Rectangled;


public abstract class Guide extends Pane {

    public enum LabelJustification {
        LEFT,
        RIGHT,
        BOTTOM,
        TOP,
        CENTER
    }

    protected final Rectangle          guide              = new Rectangle ();
    protected final Label              label              = new Label ();
    protected       LabelJustification labelJustification = LabelJustification.CENTER;
    protected       double             span               = 2.5d;
    protected       double             pos                = 0d;
    protected       double             val                = 0d;

    protected Guide () {
        getChildren ().addAll (guide, label);
    }

    protected abstract void update (
        Rectangled          window,
        Rectangled          viewport,
        LinearInterpolatord xmap,
        LinearInterpolatord ymap);

}
