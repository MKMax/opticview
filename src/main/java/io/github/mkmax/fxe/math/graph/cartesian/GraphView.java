package io.github.mkmax.fxe.math.graph.cartesian;

import javafx.scene.layout.StackPane;
import org.joml.Rectangled;

import java.util.Objects;

public class GraphView extends StackPane {

    public static final class Mapping {

        private Rectangled source;
        private Rectangled destination;

        private double Mx;
        private double Cx;

        private double My;
        private double Cy;

        private Mapping (
            Rectangled pSource,
            Rectangled pDestination)
        {
            set (pSource, pDestination);
        }

        public double mapX (double sx) {
            return Mx * sx + Cx;
        }

        public double mapY (double sy) {
            return My * sy + Cy;
        }

        public double unmapX (double dx) {
            return (dx - Cx) / Mx;
        }

        public double unmapY (double dy) {
            return (dy - Cy) / My;
        }

        private void set (
            Rectangled nSource,
            Rectangled nDestination)
        {
            source = Objects.requireNonNull (nSource);
            destination = Objects.requireNonNull (nDestination);
            update ();
        }

        private void setSource (Rectangled nSource) {
            source = Objects.requireNonNull (nSource);
            update ();
        }

        private void setDestination (Rectangled nDestination) {
            destination = Objects.requireNonNull (nDestination);
        }

        private void update () {
            Mx = (destination.maxX - destination.minX) / (source.maxX - source.minX);
            Cx = destination.minX - Mx * source.minX;

            My = (destination.maxY - destination.minY) / (source.maxY - source.minY);
            Cy = destination.minY - My * source.minY;
        }
    }

    /* The view of the graph and transformation */
    private final Rectangled window   = new Rectangled ();
    private final Rectangled viewport = new Rectangled ();
    private final Mapping    mapping  = new Mapping (window, viewport);

    /* Layers composing the graph view */
    private final Graph graph = new Graph (this);
    private final Grid  grid  = new Grid (this);

    public GraphView () {

    }

}
