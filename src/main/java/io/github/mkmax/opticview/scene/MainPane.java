package io.github.mkmax.opticview.scene;

import io.github.mkmax.opticview.material.Glass;
import io.github.mkmax.opticview.scene.forms.FocalLengthForm;
import io.github.mkmax.opticview.scene.graph.SampledRangeGraph;

import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Modality;

/* The main panel housing the input forms and the graph */
public class MainPane extends Region {

    /* +---------------+ */
    /* | STYLE CLASSES | */
    /* +---------------+ */
    public static final class ComponentClass {
        public static final String
            CONTAINER = "container",
            FORM      = "form",
            GRAPH     = "graph";
    }

    /* +------------+ */
    /* | COMPONENTS | */
    /* +------------+ */
    private final GridPane container = new GridPane ();
    private final FocalLengthForm form = new FocalLengthForm ();
    private final SampledRangeGraph graph = new SampledRangeGraph ();

    /* +--------------------+ */
    /* | DIMENSION HANDLERS | */
    /* +--------------------+ */
    private final ChangeListener<Number> onWidthChanged = (__obs, __old, now) ->
        container.setPrefWidth (now == null ? Double.NaN : now.doubleValue ());

    private final ChangeListener<Number> onHeightChanged = (__obs, __old, now) ->
        container.setPrefHeight (now == null ? Double.NaN : now.doubleValue ());

    /* install dimension listeners */
    {
        widthProperty ().addListener (onWidthChanged);
        heightProperty ().addListener (onHeightChanged);
    }

    /* +----------------+ */
    /* | INITIALIZATION | */
    /* +----------------+ */

    /* setup components */
    {
        /* set style classes */
        container.getStyleClass ().add (ComponentClass.CONTAINER);
        form.getStyleClass ().add (ComponentClass.FORM);
        graph.getStyleClass ().add (ComponentClass.GRAPH);

        /* constrain components & configure graph  */
        GridPane.setConstraints (form, 0, 0, 1, 1, HPos.LEFT, VPos.TOP, Priority.NEVER, Priority.ALWAYS);
        GridPane.setConstraints (graph, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);

        graph.setXLabel ("Lambda (λ) in μm");
        graph.setYLabel ("Focal Length (mm)");

        /* configure container */
        container.getChildren ().addAll (form, graph);
        minWidthProperty ().bind (container.minWidthProperty ());
        minHeightProperty ().bind (container.minHeightProperty ());

        getChildren ().add (container);
    }

    /* +---------------+ */
    /* | FORM HANDLERS | */
    /* +---------------+ */

    /* +--- VALIDATION CONSTANTS ---+ */
    private static final double RANGE_EPSILON = 1e-12d;
    private static final double STEP_EPSILON = 1e-6d;

    private final Runnable onFormGraphSubmitted = () -> {
        final Glass material = form.getSelectedGlass ();
        if (material == null) {
            quickErrorAlert ("Incomplete Input", "Glass material is not specified.");
            return;
        }

        double l_min, l_step, l_max;
        double lens_radius;

        /* validate lambda MIN */
        try {
            l_min = form.getLambdaMin ();
        }
        catch (NumberFormatException e) {
            quickErrorAlert ("Invalid Input", "Lambda minimum is not a valid decimal number.");
            return;
        }

        /* validate lambda STEP */
        try {
            l_step = form.getLambdaStep ();
        }
        catch (NumberFormatException e) {
            quickErrorAlert ("Invalid Input", "Lambda step is not a valid decimal number.");
            return;
        }

        /* validate lambda MAX */
        try {
            l_max = form.getLambdaMax ();
        }
        catch (NumberFormatException e) {
            quickErrorAlert ("Invalid Input", "Lambda max is not a valid decimal number.");
            return;
        }

        /* validate lens radius */
        try {
            lens_radius = form.getLensRadius ();
        }
        catch (NumberFormatException e) {
            quickErrorAlert ("Invalid Input", "Lens radius is not a valid decimal number.");
            return;
        }

        /* validate the interval */
        if (Math.abs (l_max - l_min) <= RANGE_EPSILON) {
            quickErrorAlert ("Invalid Range", "Lambda min/max are too close (within " + RANGE_EPSILON + ")");
            return;
        }

        /* validate step size */
        if (Math.abs (l_step) <= STEP_EPSILON) {
            quickErrorAlert ("Invalid Step", "Lambda step is too small (within " + STEP_EPSILON + ")");
            return;
        }

        /* validate step direction */
        if (Math.signum (l_max - l_min) != Math.signum (l_step)) {
            quickErrorAlert ("Invalid Step", "Lambda step is heading outside of the interval.");
            return;
        }

        /* actually graph the focal length now */
        graph.getData ().createEntry (
            l -> lens_radius / (material.calcRefractiveIndex (l) - 1d),
            String.format ("%s [%.2f, %.2f, %.2f]", material.NAME, l_min, l_step, l_max),
            l_min,
            l_step,
            l_max
        );
        /* @TODO(max): graph */
    };

    private final Runnable onFormClearRequested = () -> {
        graph.getData ().clearEntries ();
        form.getGlassBox ().setValue (null);
        form.getLambdaMinField ().clear ();
        form.getLambdaStepField ().clear ();
        form.getLambdaMaxField ().clear ();
        form.getLensRadiusField ().clear ();
    };

    /* install form handlers */
    {
        form.addGraphSubmitionListener (onFormGraphSubmitted);
        form.addGraphClearanceListener (onFormClearRequested);
    }

    /* +-------------+ */
    /* | QUICK UTILS | */
    /* +-------------+ */
    private static void quickErrorAlert (String title, String message) {
        Alert alert = new Alert (
            Alert.AlertType.ERROR,
            message,
            ButtonType.OK);
        alert.setTitle (title);
        alert.setHeaderText (title);
        alert.initModality (Modality.APPLICATION_MODAL);
        alert.show ();
    }
}
