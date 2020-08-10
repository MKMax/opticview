package io.github.mkmax.opticview.scene.forms;

import io.github.mkmax.opticview.material.Glass;
import io.github.mkmax.opticview.scene.controls.DoubleField;

import io.github.mkmax.opticview.scene.controls.GlassListCell;
import io.github.mkmax.opticview.scene.controls.UnitListCell;
import io.github.mkmax.opticview.units.MetricDistance;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

/* input form for data used to graph the focal length function for a specific lens */
public class FocalLengthForm extends Region {

    /* +---------------+ */
    /* | STYLE CLASSES | */
    /* +---------------+ */
    public static final class ComponentClass {
        public static final String
            FOCAL_LENGTH_FORM = "focal-length-form",
            FILL_CELL         = "fill-cell",
            SECTION_TITLE     = "section-title";
    }

    /* +------------+ */
    /* | COMPONENTS | */
    /* +------------+ */
    private final GridPane container = new GridPane ();

    /* material section */
    private final Label glassHeader = new Label ("Glass");
    private final ComboBox<Glass> glassBox = new ComboBox<> (
        FXCollections.observableArrayList (Glass.values ()));

    /* wavelength (lambda) section */
    private final Label lambdaHeader = new Label ("λ");
    private final Label
        lambdaUnitsLabel = new Label ("units:"),
        lambdaMinLabel   = new Label ("min:"),
        lambdaStepLabel  = new Label ("step:"),
        lambdaMaxLabel   = new Label ("max:");
    private final ComboBox<MetricDistance> lambdaUnitsBox = new ComboBox<> (
        FXCollections.observableArrayList (MetricDistance.values ()));
    private final DoubleField
        lambdaMinField  = new DoubleField (),
        lambdaStepField = new DoubleField (),
        lambdaMaxField  = new DoubleField ();

    /* lens section */
    private final Label lensHeader = new Label ("Plano-Convex Lens");
    private final Label
        lensRadiusUnitLabel = new Label ("units:"),
        lensRadiusLabel     = new Label ("radius:");
    private final ComboBox<MetricDistance> lensRadiusUnitBox = new ComboBox<> (
        FXCollections.observableArrayList (MetricDistance.values ()));
    private final DoubleField lensRadiusField = new DoubleField ();

    /* export section */
    private final Button
        plotButton       = new Button ("Plot"),
        clearPlotsButton = new Button ("Clear Plots"),
        clearInputButton = new Button ("Clear Input");

    /* component initialization */
    {
        /* first of all, add the main container to this region */
        getChildren ().add (container);

        /* set the default style class to access from CSS (style convenience) */
        getStyleClass ().add (ComponentClass.FOCAL_LENGTH_FORM);

        /* configure material section [rows 0-1] */
        glassHeader.getStyleClass ().add (ComponentClass.SECTION_TITLE);
        glassBox.getStyleClass ().add (ComponentClass.FILL_CELL);
        glassBox.setButtonCell (new GlassListCell ());
        glassBox.setCellFactory (view -> new GlassListCell ());

        GridPane.setConstraints (glassHeader, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        GridPane.setConstraints (glassBox,    0, 1, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        container.getChildren ().addAll (glassHeader, glassBox);

        /* configure wavelength/lambda section [rows 2-6] */
        lambdaHeader.getStyleClass ().add (ComponentClass.SECTION_TITLE);
        lambdaUnitsBox.getStyleClass ().add (ComponentClass.FILL_CELL);
        lambdaUnitsBox.setButtonCell (new UnitListCell<> ());
        lambdaUnitsBox.setCellFactory (view -> new UnitListCell<> ());
        lambdaUnitsBox.setValue (MetricDistance.MICROMETERS);

        GridPane.setConstraints (lambdaHeader,     0, 2, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        GridPane.setConstraints (lambdaUnitsLabel, 0, 3, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        GridPane.setConstraints (lambdaMinLabel,   0, 4, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        GridPane.setConstraints (lambdaStepLabel,  0, 5, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        GridPane.setConstraints (lambdaMaxLabel,   0, 6, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);

        GridPane.setConstraints (lambdaUnitsBox,  1, 3, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        GridPane.setConstraints (lambdaMinField,  1, 4, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        GridPane.setConstraints (lambdaStepField, 1, 5, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        GridPane.setConstraints (lambdaMaxField,  1, 6, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        container.getChildren ().addAll (
            lambdaHeader,
            lambdaUnitsLabel, lambdaUnitsBox,
            lambdaMinLabel,   lambdaMinField,
            lambdaStepLabel,  lambdaStepField,
            lambdaMaxLabel,   lambdaMaxField);

        /* configure lens section [rows 7-9] */
        lensHeader.getStyleClass ().add (ComponentClass.SECTION_TITLE);
        lensRadiusUnitBox.getStyleClass ().add (ComponentClass.FILL_CELL);
        lensRadiusUnitBox.setButtonCell (new UnitListCell<> ());
        lensRadiusUnitBox.setCellFactory (view -> new UnitListCell<> ());
        lensRadiusUnitBox.setValue (MetricDistance.MILLIMETERS);

        GridPane.setConstraints (lensHeader,          0, 7, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        GridPane.setConstraints (lensRadiusUnitLabel, 0, 8, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        GridPane.setConstraints (lensRadiusLabel,     0, 9, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);

        GridPane.setConstraints (lensRadiusUnitBox,   1, 8, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        GridPane.setConstraints (lensRadiusField,     1, 9, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        container.getChildren ().addAll (
            lensHeader,
            lensRadiusUnitLabel, lensRadiusUnitBox,
            lensRadiusLabel,     lensRadiusField);

        /* configure export controls [10-12] */
        plotButton.getStyleClass ().add (ComponentClass.FILL_CELL);
        clearPlotsButton.getStyleClass ().add (ComponentClass.FILL_CELL);
        clearInputButton.getStyleClass ().add (ComponentClass.FILL_CELL);

        GridPane.setConstraints (plotButton,       0, 10, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        GridPane.setConstraints (clearPlotsButton, 0, 11, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        GridPane.setConstraints (clearInputButton, 0, 12, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        container.getChildren ().addAll (
            plotButton,
            clearPlotsButton,
            clearInputButton);
    }

    /* +-----------+ */
    /* | LISTENERS | */
    /* +-----------+ */
    private final List<Runnable>
        plotSubmitListeners = new ArrayList<> (),
        plotClearListeners = new ArrayList<> ();

    /* GRAPH CREATION */
    public void addPlotSubmitionListener (Runnable onGraphSubmitted) {
        if (onGraphSubmitted != null) plotSubmitListeners.add (onGraphSubmitted);
    }

    public void removeGraphSubmitionListener (Runnable onGraphSubmitted) {
        plotSubmitListeners.remove (onGraphSubmitted);
    }

    /* GRAPH CLEAR */
    public void addPlotClearanceListener (Runnable onGraphCleared) {
        if (onGraphCleared != null) plotClearListeners.add (onGraphCleared);
    }

    public void removeGraphClearanceListener (Runnable onGraphCleared) {
        plotClearListeners.remove (onGraphCleared);
    }

    /* +--------------------------+ */
    /* | MEMBERS & INITIALIZATION | */
    /* +--------------------------+ */

    /* register listeners and perform final forms of initialization */
    {
        plotButton.setOnAction (e -> plotSubmitListeners.forEach (Runnable::run));
        clearPlotsButton.setOnAction (e -> plotClearListeners.forEach (Runnable::run));
        clearInputButton.setOnAction (e -> {
            glassBox.setValue (null);
            lambdaMinField.setText (null);
            lambdaStepField.setText (null);
            lambdaMaxField.setText (null);
            lensRadiusField.setText (null);
        });
    }

    /* +-------------------+ */
    /* | COMPONENT QUERIES | */
    /* +-------------------+ */

    /* +--- GLASS BOX ---+ */
    public ComboBox<Glass> getGlassBox () {
        return glassBox;
    }

    /* +--- LAMBDA MIN FIELD ---+ */
    public DoubleField getLambdaMinField () {
        return lambdaMinField;
    }

    /* +--- LAMBDA STEP FIELD ---+ */
    public DoubleField getLambdaStepField () {
        return lambdaStepField;
    }

    /* +--- LAMBDA MAX FIELD ---+ */
    public DoubleField getLambdaMaxField () {
        return lambdaMaxField;
    }

    /* +--- LENS RADIUS ---+ */
    public DoubleField getLensRadiusField () {
        return lensRadiusField;
    }

    /* +--------------------------+ */
    /* | INFO QUERIES & MODIFIERS | */
    /* +--------------------------+ */

    /* +--- GLASS MATERIAL ---+ */
    public boolean isGlassMaterialEmpty () {
        return glassBox.getValue () == null;
    }

    public Glass getGlassMaterial () {
        return glassBox.getValue ();
    }

    /* +--- LAMBDA UNITS ---+ */
    public boolean areLambdaUnitsEmpty () {
        return lambdaUnitsBox.getValue () == null;
    }

    public MetricDistance getLambdaUnits () {
        return lambdaUnitsBox.getValue ();
    }

    /* +--- LAMBDA MIN ---+ */
    public boolean isLambdaMinEmpty () {
        return isFieldEmpty (lambdaMinField);
    }

    public double getLambdaMin () throws NumberFormatException {
        return lambdaMinField.getValue ();
    }

    /* +--- LAMBDA STEP ---+ */
    public boolean isLambdaStepEmpty () {
        return isFieldEmpty (lambdaStepField);
    }

    public double getLambdaStep () throws NumberFormatException {
        return lambdaStepField.getValue ();
    }

    /* +--- LAMBDA MAX ---+ */
    public boolean isLambdaMaxEmpty () {
        return isFieldEmpty (lambdaMaxField);
    }

    public double getLambdaMax () throws NumberFormatException {
        return lambdaMaxField.getValue ();
    }

    /* +--- LENS UNITS ---+ */
    public boolean areLensRadiusUnitsEmpty () {
        return lensRadiusUnitBox.getValue () == null;
    }

    public MetricDistance getLensRadiusUnits () {
        return lensRadiusUnitBox.getValue ();
    }

    /* +--- LENS RADIUS ---+ */
    public boolean isLensRadiusEmpty () {
        return isFieldEmpty (lensRadiusField);
    }

    public double getLensRadius () throws NumberFormatException {
        return lensRadiusField.getValue ();
    }

    /* +-----------------+ */
    /* | QUICK UTILITIES | */
    /* +-----------------+ */
    private static boolean isFieldEmpty (TextInputControl tic) {
        return tic.getText () == null || tic.getText ().length () == 0;
    }
}
