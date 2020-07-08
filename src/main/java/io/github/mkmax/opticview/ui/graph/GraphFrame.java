package io.github.mkmax.opticview.ui.graph;

import io.github.mkmax.opticview.util.Disposable;

import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Objects;

public class GraphFrame extends Region implements Disposable {

    /* +--------------------------+ */
    /* | INITIALIZATION & MEMBERS | */
    /* +--------------------------+ */

    /* default appearance settings */
    private static final Insets DEFAULT_LABEL_PADDING = new Insets (10d);
    private static final Border DEFAULT_STACK_BORDER = new Border (
        new BorderStroke (Color.BLACK, BorderStrokeStyle.SOLID, null, null));

    /* underlying container object */
    private final GridPane uframe = new GridPane ();
    private final ChangeListener<Number> widthlistener = (__obs, __old, now) ->
        uframe.setPrefWidth (now == null ? getWidth () : now.doubleValue ());
    private final ChangeListener<Number> heightlistener = (__obs, __old, now) ->
        uframe.setPrefHeight (now == null ? getHeight () : now.doubleValue ());

    /* contained structures */
    private final StackPane graphstack_container = new StackPane ();
    private final GraphStack graphstack;

    private final Group hlabel_container = new Group ();
    private final Group vlabel_container = new Group ();
    private final Label hlabel = new Label (); /* horizontal */
    private final Label vlabel = new Label (); /* vertical */

    public GraphFrame (GraphStack stack) {
        graphstack = Objects.requireNonNull (stack, "A non-null GraphStack must be specified");
        graphstack_container.setBorder (DEFAULT_STACK_BORDER);
        graphstack_container.getChildren ().add (graphstack);
        /* configure labels */
        hlabel.setPadding (DEFAULT_LABEL_PADDING);
        vlabel.setPadding (DEFAULT_LABEL_PADDING);
        vlabel.setRotate (-90d);

        hlabel_container.getChildren ().add (hlabel);
        vlabel_container.getChildren ().add (vlabel);
        /* add the components to the frame and configure it  */
        GridPane.setConstraints (graphstack_container, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        GridPane.setConstraints (hlabel_container, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.SOMETIMES, Priority.SOMETIMES);
        GridPane.setConstraints (vlabel_container, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.SOMETIMES, Priority.SOMETIMES);
        uframe.getChildren ().addAll (
            graphstack_container,
            vlabel_container,
            hlabel_container);
        /* add the underlying container as a child of this region */
        getChildren ().add (uframe);
        /* we also need to manually resize the underlying frame */
        widthProperty ().addListener (widthlistener);
        heightProperty ().addListener (heightlistener);
    }

    public GraphFrame () {
        this (new GraphStack ());
    }

    /* +------------------------------------+ */
    /* | GETTERS, SETTERS & QUICK MODIFIERS | */
    /* +------------------------------------+ */

    /* GRAPH STACK */
    public GraphStack getGraphStack () {
        return graphstack;
    }

    /* H LABEL */
    public void setHorizontalLabel (String label) {
        hlabel.setText (label);
    }

    /* V LABEL */
    public void setVerticalLabel (String label) {
        vlabel.setText (label);
    }

    /* +-----------+ */
    /* | INTERNALS | */
    /* +-----------+ */
    @Override
    public void dispose () {
        widthProperty ().removeListener (widthlistener);
        heightProperty ().removeListener (heightlistener);
        graphstack.dispose ();
    }
}
