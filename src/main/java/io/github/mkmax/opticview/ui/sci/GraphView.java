package io.github.mkmax.opticview.ui.sci;

import io.github.mkmax.opticview.ui.OrthoRegion;
import io.github.mkmax.opticview.util.Disposable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public final class GraphView extends OrthoRegion implements Disposable {

    /* settings */
    private final ObservableList<Color> idxcolors = FXCollections.observableArrayList ();

    /* UI devices */
    private final Canvas canvas = new Canvas ();
    private final GraphicsContext gc = canvas.getGraphicsContext2D ();

    /* core members */
    private final GraphData dataref;

    public GraphView (GraphData ref) {
        dataref = Objects.requireNonNull (ref);
    }
}
