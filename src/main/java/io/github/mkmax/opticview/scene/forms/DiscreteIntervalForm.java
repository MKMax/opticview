package io.github.mkmax.opticview.scene.forms;

import io.github.mkmax.opticview.scene.controls.DoubleField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/* A utility form to specify the start, the step, and the end of an interval */
public class DiscreteIntervalForm extends Region {

    /* +------------+ */
    /* | COMPONENTS | */
    /* +------------+ */
    private final GridPane container = new GridPane ();
    private final Label title = new Label ();
    private final Label
        startLabel = new Label (),
        stepLabel  = new Label (),
        endLabel   = new Label ();
    private final DoubleField
        startField = new DoubleField (),
        stepField  = new DoubleField (),
        endField   = new DoubleField ();

    /* +------------+ */
    /* | PROPERTIES | */
    /* +------------+ */
    private final StringProperty
        titleProperty       = new SimpleStringProperty (),
        startPromptProperty = new SimpleStringProperty (),
        stepPromptProperty  = new SimpleStringProperty (),
        endPromptProperty   = new SimpleStringProperty ();

    /* TITLE */
    public StringProperty titleProperty ()
        { return titleProperty; }
    public String getTitle ()
        { return titleProperty.get (); }
    public void setTitle (String nTitle)
        { titleProperty.set (nTitle); }

    /* START PROMPT */
    public StringProperty startPromptProperty ()
        { return startPromptProperty; }
    public String getStartPrompt ()
        { return startPromptProperty.get (); }
    public void setStartPrompt (String nStartPrompt)
        { startPromptProperty.set (nStartPrompt); }

    /* STEP PROMPT */
    public StringProperty stepPromptProperty ()
        { return stepPromptProperty; }
    public String getStepPrompt ()
        { return stepPromptProperty.get (); }
    public void setStepPrompt (String nStepPrompt)
        { stepPromptProperty.set (nStepPrompt); }

    /* END PROMPT */
    public StringProperty endPromptProperty ()
        { return endPromptProperty; }
    public String getEndPrompt ()
        { return endPromptProperty.get (); }
    public void setEndPrompt (String nEndPrompt)
        { endPromptProperty.set (nEndPrompt); }

    /* +----------------+ */
    /* | INITIALIZATION | */
    /* +----------------+ */
    public DiscreteIntervalForm (
        String pTitle,
        String pStartPrompt,
        String pStepPrompt,
        String pEndPrompt)
    {
        /* setup the components for the container grid */
        GridPane.setConstraints (title, 0, 0, 2, 1, HPos.LEFT, VPos.CENTER, Priority.SOMETIMES, Priority.NEVER);

        GridPane.setConstraints (startLabel, 0, 1, 1, 1, HPos.RIGHT, VPos.TOP, Priority.NEVER, Priority.SOMETIMES);
        GridPane.setConstraints (stepLabel, 0, 2, 1, 1, HPos.RIGHT, VPos.TOP, Priority.NEVER, Priority.SOMETIMES);
        GridPane.setConstraints (endLabel, 0, 3, 1, 1, HPos.RIGHT, VPos.TOP, Priority.NEVER, Priority.SOMETIMES);

        GridPane.setConstraints (startField, 1, 1, 1, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.SOMETIMES);
        GridPane.setConstraints (stepField, 1, 2, 1, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.SOMETIMES);
        GridPane.setConstraints (endField, 1, 3, 1, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.SOMETIMES);

        /* register components with the container */
        container.getChildren ().addAll (
            title,
            startLabel, startField,
            stepLabel, stepField,
            endLabel, endField
        );

        /* register container with the actual component */
        getChildren ().add (container);

        /* set the given prompts */
        setTitle (pTitle);
        setStartPrompt (pStartPrompt);
        setStepPrompt (pStepPrompt);
        setEndPrompt (pEndPrompt);

        /* bind the string properties to their respective labels */
        title     .textProperty ().bind (titleProperty);
        startLabel.textProperty ().bind (startPromptProperty);
        stepLabel .textProperty ().bind (stepPromptProperty);
        endLabel  .textProperty ().bind (endPromptProperty);
    }

    public DiscreteIntervalForm (String pTitle) {
        this (pTitle, null, null, null);
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */

    /* START FIELD */
    public DoubleField getStartField () {
        return startField;
    }

    /* STEP FIELD */
    public DoubleField getStepField () {
        return stepField;
    }

    /* END FIELD */
    public DoubleField getEndField () {
        return endField;
    }
}
