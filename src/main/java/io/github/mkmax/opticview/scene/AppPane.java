package io.github.mkmax.opticview.scene;

import io.github.mkmax.opticview.material.Glass;
import io.github.mkmax.opticview.scene.forms.FocalLengthForm;
import io.github.mkmax.opticview.scene.graph.SampledRangeGraph;

import io.github.mkmax.opticview.units.MetricDistance;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Modality;

import java.util.List;

/* The main panel housing the input forms and the graph */
public class AppPane extends Region {

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
    private final SampledRangeGraph graph = new SampledRangeGraph (
        List.of (MetricDistance.values ()),
        List.of (MetricDistance.values ()),
        MetricDistance.NANOMETERS,
        MetricDistance.MILLIMETERS
    );

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

        graph.setXLabel ("Lambda (λ)");
        graph.setYLabel ("Focal Length");

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
        if (form.isGlassMaterialEmpty ()) {
            quickErrorAlert ("Incomplete Input", "Glass material is not specified.");
            return;
        }

        if (form.areLambdaUnitsEmpty ()) {
            quickErrorAlert ("Incomplete Input", "Lambda units are not specified");
            return;
        }

        if (form.areLensRadiusUnitsEmpty ()) {
            quickErrorAlert ("Incomplete Input", "Lens radius units are not specified");
            return;
        }

        final Glass material = form.getGlassMaterial ();
        final MetricDistance
            lambda_units = form.getLambdaUnits (),
            lens_radius_units = form.getLensRadiusUnits ();

        final double l_min, l_step, l_max;
        final double lens_radius;

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
        final double
            real_l_min       = MetricDistance.MICROMETERS.convertFrom (lambda_units, l_min),
            real_l_step      = MetricDistance.MICROMETERS.convertFrom (lambda_units, l_step),
            real_l_max       = MetricDistance.MICROMETERS.convertFrom (lambda_units, l_max),
            real_lens_radius = MetricDistance.MILLIMETERS.convertFrom (lens_radius_units, lens_radius);


        /* per client request, if inputs are in nanos and real_l_min > 2 microns, set it to microns */
        if (real_l_min > 2d && graph.getInputUnits () == MetricDistance.NANOMETERS)
            graph.setInputUnits (MetricDistance.MICROMETERS);

        graph.getFunctionData ().createEntry (
            l -> real_lens_radius / (material.calcRefractiveIndex (l) - 1d),
            String.format (
                "%s [rad: %s%s]",
                material.NAME,
                lens_radius, lens_radius_units.getMnemonic ()
            ),
            MetricDistance.MICROMETERS,
            MetricDistance.MILLIMETERS,
            real_l_min,
            real_l_step,
            real_l_max
        );
    };

    private final Runnable onFormClearRequested = () ->
        graph.getFunctionData ().clearEntries ();

    /* install form handlers */
    {
        form.addPlotSubmitionListener (onFormGraphSubmitted);
        form.addPlotClearanceListener (onFormClearRequested);
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
