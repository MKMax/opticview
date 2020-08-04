package io.github.mkmax.opticview.scene.forms;

import io.github.mkmax.opticview.material.Glass;
import io.github.mkmax.opticview.scene.controls.DoubleField;

import io.github.mkmax.opticview.scene.controls.GlassListCell;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.List;

/* input form for data used to graph the focal length function for a specific lens */
public class FocalLengthForm extends Region {

    /* +---------------+ */
    /* | STYLE CLASSES | */
    /* +---------------+ */
    public static final class ComponentClass {
        public static final String FOCAL_LENGTH_FORM = "focal-length-form";
    }

    /* +------------+ */
    /* | COMPONENTS | */
    /* +------------+ */
    private final VBox container = new VBox ();

    /* material section */
    private final Label glassHeader = new Label ("Glass:");
    private final ComboBox<Glass> glassBox = new ComboBox<> (
        FXCollections.observableArrayList (Glass.values ()));

    /* wavelength (lambda) section */
    private final Label lambdaHeader = new Label ("λ (μm):");
    private final GridPane lambdaContainer = new GridPane ();
    private final Label
        lambdaMinLabel  = new Label ("min:"),
        lambdaStepLabel = new Label ("step:"),
        lambdaMaxLabel  = new Label ("max:");
    private final DoubleField
        lambdaMinField  = new DoubleField (),
        lambdaStepField = new DoubleField (),
        lambdaMaxField  = new DoubleField ();

    /* lens radius section */
    private final Label lensHeader = new Label ("Plano-Convex Lens:");
    private final HBox lensRadiusContainer = new HBox ();
    private final Label lensRadiusLabel = new Label ("radius (mm):");
    private final DoubleField lensRadiusField = new DoubleField ();

    /* create/clear graph section */
    private final HBox graphButtonsContainer = new HBox ();
    private final Button
        submitGraph = new Button ("Graph"),
        clearGraph = new Button ("Clear All");

    /* component initialization */
    {
        /* first of all, add the main container to this region */
        getChildren ().add (container);

        /* set the default style class to access from CSS (style convenience) */
        getStyleClass ().add (ComponentClass.FOCAL_LENGTH_FORM);

        /* configure the materials section & add to container */
        glassBox.setButtonCell (new GlassListCell ());
        glassBox.setCellFactory (view -> new GlassListCell ());
        container.getChildren ().addAll (glassHeader, glassBox);

        /* configure the lambda input section & add to container */
        GridPane.setConstraints (lambdaMinLabel,  0, 0, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS);
        GridPane.setConstraints (lambdaStepLabel, 0, 1, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS);
        GridPane.setConstraints (lambdaMaxLabel,  0, 2, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS);

        GridPane.setConstraints (lambdaMinField,  1, 0, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        GridPane.setConstraints (lambdaStepField, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        GridPane.setConstraints (lambdaMaxField,  1, 2, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);

        lambdaContainer.getChildren ().addAll (
            lambdaMinLabel,  lambdaMinField,
            lambdaStepLabel, lambdaStepField,
            lambdaMaxLabel,  lambdaMaxField
        );
        container.getChildren ().addAll (lambdaHeader, lambdaContainer);

        /* configure the lens radius section */
        lensRadiusContainer.setAlignment (Pos.CENTER_LEFT);
        lensRadiusContainer.getChildren ().addAll (lensRadiusLabel, lensRadiusField);
        container.getChildren ().addAll (lensHeader, lensRadiusContainer);

        /* configure the create/clear plot buttons & add to container */
        graphButtonsContainer.setAlignment (Pos.CENTER_RIGHT);
        graphButtonsContainer.getChildren ().addAll (submitGraph, clearGraph);
        container.getChildren ().add (graphButtonsContainer);
    }

    /* +-----------+ */
    /* | LISTENERS | */
    /* +-----------+ */
    private final List<Runnable>
        graphSubmitionListeners = new ArrayList<> (),
        graphClearanceListeners = new ArrayList<> ();

    /* GRAPH CREATION */
    public void addGraphSubmitionListener (Runnable onGraphSubmitted) {
        if (onGraphSubmitted != null) graphSubmitionListeners.add (onGraphSubmitted);
    }

    public void removeGraphSubmitionListener (Runnable onGraphSubmitted) {
        graphSubmitionListeners.remove (onGraphSubmitted);
    }

    /* GRAPH CLEAR */
    public void addGraphClearanceListener (Runnable onGraphCleared) {
        if (onGraphCleared != null) graphClearanceListeners.add (onGraphCleared);
    }

    public void removeGraphClearanceListener (Runnable onGraphCleared) {
        graphClearanceListeners.remove (onGraphCleared);
    }

    /* +--------------------------+ */
    /* | MEMBERS & INITIALIZATION | */
    /* +--------------------------+ */

    /* register listeners and perform final forms of initialization */
    {
        submitGraph.setOnAction (e -> graphSubmitionListeners.forEach (Runnable::run));
        clearGraph.setOnAction (e -> graphClearanceListeners.forEach (Runnable::run));
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
    public Glass getSelectedGlass () {
        return glassBox.getValue ();
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
