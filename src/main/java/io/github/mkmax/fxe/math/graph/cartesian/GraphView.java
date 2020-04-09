package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.util.math.LinearInterpolatord;
import javafx.scene.layout.StackPane;
import org.joml.Rectangled;

public class GraphView extends StackPane {

    /* The view of the graph and transformation */
    private final Rectangled          window   = new Rectangled (-1d, -1d, +  1d, +  1d);
    private final Rectangled          viewport = new Rectangled ( 0d,  0d, +128d, +128d);
    private final LinearInterpolatord xmap     = LinearInterpolatord.mapX (window, viewport);
    private final LinearInterpolatord ymap     = LinearInterpolatord.mapY (window, viewport);

    /* Layers composing the graph view */
    private final Graph graph = new Graph ();
    private final Grid  grid  = new Grid ();

    public GraphView () {

    }

}
