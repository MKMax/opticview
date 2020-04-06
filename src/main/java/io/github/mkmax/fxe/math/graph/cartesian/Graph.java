package io.github.mkmax.fxe.math.graph.cartesian;

import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import java.util.Objects;

public class Graph extends Canvas {

    private final GraphView container;

    Graph (GraphView pContainer) {
        container = Objects.requireNonNull (pContainer);

        setWidth (container.getWidth ());
        setHeight (container.getHeight ());

        container.widthProperty () .addListener (this::onContainerWidthChanged);
        container.heightProperty ().addListener (this::onContainerHeightChanged);
    }

    private void onContainerWidthChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        setWidth (now.doubleValue ());
    }

    private void onContainerHeightChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        setHeight (now.doubleValue ());
    }

}
