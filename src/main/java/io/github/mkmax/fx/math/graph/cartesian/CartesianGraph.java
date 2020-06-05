package io.github.mkmax.fx.math.graph.cartesian;

import io.github.mkmax.fx.util.ObservableGroup;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class CartesianGraph extends Region {

    final Pane guideLinePane = new Pane ();
    final Pane guideLabelPane = new Pane ();

    private final DoubleProperty
        left   = new SimpleDoubleProperty (),
        right  = new SimpleDoubleProperty (),
        bottom = new SimpleDoubleProperty (),
        top    = new SimpleDoubleProperty ();

    private final ObservableGroup<Number> windowMetricsGroup = ObservableGroup.builder ()
        .withValues (left, right, bottom, top)
        .withListeners (this::)

    private final CartesianXAxis xAxis = new CartesianXAxis (this);
    private final CartesianYAxis yAxis = new CartesianYAxis (this);

    public CartesianGraph () {
        getChildren ().addAll (
            guideLinePane,
            guideLabelPane
        );
    }

}
