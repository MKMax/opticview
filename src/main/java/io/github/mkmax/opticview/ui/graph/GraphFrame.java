package io.github.mkmax.opticview.ui.graph;

import io.github.mkmax.opticview.util.IDisposable;

import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GraphFrame extends Region implements IDisposable {

    /* +--------------------------+ */
    /* | INITIALIZATION & MEMBERS | */
    /* +--------------------------+ */

    /* default appearance settings */
    private static final String DEFAULT_TOOL_LABEL_TEXT = "Tools:";
    private static final double DEFAULT_TOOL_TRAY_SPACING = 5d;
    private static final Insets DEFAULT_TOOL_TRAY_BUTTON_PADDING = new Insets (2d);
    private static final Insets DEFAULT_TOOL_TRAY_PADDING = new Insets (5d);
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
    private final HBox toolTray = new HBox ();
    private final Label toolLabel = new Label (DEFAULT_TOOL_LABEL_TEXT);
    private final ToggleGroup toolToggleGroup = new ToggleGroup ();
    private final Map<GraphStack.Tool, ToggleButton> toolButtonMap = new HashMap<> ();
    private final Map<ToggleButton, GraphStack.Tool> buttonToolMap = new HashMap<> ();

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
        /* configure tool tray */
        toolTray.setAlignment (Pos.CENTER_RIGHT);
        toolTray.setSpacing (DEFAULT_TOOL_TRAY_SPACING);
        toolTray.setPadding (DEFAULT_TOOL_TRAY_PADDING);
        toolTray.getChildren ().add (toolLabel);
        toolToggleGroup.selectedToggleProperty ().addListener (onSelectedToolChangeRequested);
        graphstack.getImmutableToolList ().forEach (this::recognizeTool);
        graphstack.registerToolAdditionListener (recognizeToolDelegate);
        graphstack.registerToolRemovalListener (dismissToolDelegate);
        graphstack
            .getToolRegion ()
            .activeToolProperty ()
            .addListener (onActiveToolChanged);
        onActiveToolChanged.changed (null, null, graphstack.getToolRegion ().getActiveTool ());

        /* configure labels */
        hlabel.setPadding (DEFAULT_LABEL_PADDING);
        vlabel.setPadding (DEFAULT_LABEL_PADDING);
        vlabel.setRotate (-90d);

        hlabel_container.getChildren ().add (hlabel);
        vlabel_container.getChildren ().add (vlabel);
        /* add the components to the frame and configure it  */
        GridPane.setConstraints (toolTray, 0, 0, 2, 1, HPos.RIGHT, VPos.CENTER, Priority.SOMETIMES, Priority.SOMETIMES);
        GridPane.setConstraints (graphstack_container, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        GridPane.setConstraints (hlabel_container, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER, Priority.SOMETIMES, Priority.SOMETIMES);
        GridPane.setConstraints (vlabel_container, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.SOMETIMES, Priority.SOMETIMES);
        uframe.getChildren ().addAll (
            toolTray,
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
    /* | LISTENERS | */
    /* +-----------+ */
    private final ChangeListener<GraphStack.Tool> onActiveToolChanged = (__obs, __old, now) -> {
        final ToggleButton button = toolButtonMap.get (now);
        if (button != null && !button.isSelected ())
            button.setSelected (true);
        if (now == null)
            toolLabel.setText (DEFAULT_TOOL_LABEL_TEXT);
        else
            toolLabel.setText (now.getName ());
    };

    private final ChangeListener<Toggle> onSelectedToolChangeRequested = (__obs, __old, now) -> {
        final ToggleButton button = (ToggleButton) now;
        getGraphStack ()
            .getToolRegion ()
            .setActiveTool (buttonToolMap.get (button));
    };

    /* +-----------+ */
    /* | INTERNALS | */
    /* +-----------+ */
    private final GraphStack.ToolAdditionListener recognizeToolDelegate = (__gs, tool) -> recognizeTool (tool);
    private final GraphStack.ToolRemovalListener dismissToolDelegate = (__gs, tool) -> dismissTool (tool);

    private void recognizeTool (GraphStack.Tool tool) {
        final ImageView graphic = new ImageView (tool.getIcon ());
        graphic.setFitWidth (16d);
        graphic.setFitHeight (16d);
        final ToggleButton nbutton = new ToggleButton (null, graphic);
        Tooltip tooltip = new Tooltip (tool.getName () + " Tool");
        tooltip.setHideDelay (Duration.seconds (0));
        tooltip.setShowDelay (Duration.seconds (0));
        nbutton.setTooltip (tooltip);
        nbutton.setPadding (DEFAULT_TOOL_TRAY_BUTTON_PADDING);
        toolToggleGroup.getToggles ().add (nbutton);
        buttonToolMap.put (nbutton, tool);
        toolButtonMap.put (tool, nbutton);
        toolTray.getChildren ().add (nbutton);
    }

    private void dismissTool (GraphStack.Tool tool) {
        final ToggleButton button = toolButtonMap.get (tool);
        toolToggleGroup.getToggles ().remove (button);
        buttonToolMap.remove (button);
        toolButtonMap.remove (tool);
        toolTray.getChildren ().remove (button);
    }

    @Override
    public void dispose () {
        widthProperty ().removeListener (widthlistener);
        heightProperty ().removeListener (heightlistener);
        toolToggleGroup.selectedToggleProperty ().removeListener (onSelectedToolChangeRequested);
        graphstack.getToolRegion ().activeToolProperty ().removeListener (onActiveToolChanged);
        graphstack.removeToolAdditionListener (recognizeToolDelegate);
        graphstack.removeToolRemovalListener (dismissToolDelegate);
        graphstack.dispose ();
    }
}
