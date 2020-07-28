package io.github.mkmax.opticview.scene.controls;

import javafx.beans.value.ChangeListener;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class DoubleField extends Region {

    /* +--------+ */
    /* | FILTER | */
    /* +--------+ */
    public static final class FilterException extends RuntimeException {
        public FilterException (String message) {
            super (message);
        }
    }

    @FunctionalInterface
    public interface IFilter {
        void test (String stringForm, double parsedForm) throws FilterException;
    }

    /* +---------------+ */
    /* | STYLE CLASSES | */
    /* +---------------+ */

    /* main */
    public static final String
        CONTAINER = "container",
        FIELD     = "field",
        LABEL     = "label";

    /* state classes */
    public enum State {
        NORMAL  ("normal"),
        ERROR   ("error"),
        WARNING ("warning");

        public final String STATE_CLASS;

        State (String stateClass) {
            STATE_CLASS = Objects.requireNonNull (stateClass);
        }
    }

    /* +------------+ */
    /* | COMPONENTS | */
    /* +------------+ */
    private final VBox      container = new VBox ();
    private final TextField field     = new TextField ();
    private final Label     label     = new Label ();

    private String parseErrorMessage = "Invalid Decimal Number";
    private State currentState = State.NORMAL;

    /* setup the components */
    {
        // assign classes
        container.getStyleClass ().addAll (CONTAINER, currentState.STATE_CLASS);
        field.getStyleClass ().addAll (FIELD, currentState.STATE_CLASS);
        label.getStyleClass ().addAll (LABEL, currentState.STATE_CLASS);

        // container
        container.getChildren ().addAll (field, label);

        // register container
        getChildren ().add (container);
    }

    /* +--------------------------+ */
    /* | INITIALIZATION & MEMBERS | */
    /* +--------------------------+ */
    private final ObservableList<IFilter> filters = FXCollections.observableArrayList ();

    public DoubleField () {
        setMaxWidth (192);
        setMaxHeight (192);
        filters.addListener (onFilterListUpdated);
        field.textProperty ().addListener (onTextChanged);
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */
    private final ListChangeListener<IFilter> onFilterListUpdated = (__change) ->
        runParseCheckOnCurrentText ();

    private final ChangeListener<String> onTextChanged = (__obs, old, now) -> {
        if (now == null || now.length () == 0)
            clearMessage ();
        else
            runParseCheckOnCurrentText ();
    };

    /* PROMPT TEXT */
    public String getPromptText () {
        return field.getPromptText ();
    }

    public void setPromptText (String prompt) {
        field.setPromptText (prompt);
    }

    /* MESSAGE */
    public void clearMessage () {
        setMessage (State.NORMAL, "");
    }

    public void setMessage (State state, String message) {
        Objects.requireNonNull (state, "A state must be specified for the message");
        Objects.requireNonNull (message, "A message must be specified");
        if (state != currentState) {
            /* add the new state class first */
            container.getStyleClass ().add (state.STATE_CLASS);
            field.getStyleClass ().add (state.STATE_CLASS);
            label.getStyleClass ().add (state.STATE_CLASS);

            /* remove old state class after adding new one */
            container.getStyleClass ().remove (currentState.STATE_CLASS);
            field.getStyleClass ().remove (currentState.STATE_CLASS);
            label.getStyleClass ().remove (currentState.STATE_CLASS);
        }
        label.setText (message);
        currentState = state;
    }

    public void setParseErrorMessage (String message) {
        parseErrorMessage = Objects.requireNonNull (message, "A new parse error message must be specified");
        if (currentState == State.ERROR)
            setMessage (State.ERROR, parseErrorMessage);
    }

    /* DOUBLE */
    public void setDouble (double value) {
        field.setText (Double.toString (value));
    }

    public double getDouble () {
        return Double.parseDouble (field.getText ());
    }

    /* +----------+ */
    /* | INTERNAL | */
    /* +----------+ */
    public void runParseCheckOnCurrentText () {
        final String now = field.getText ();
        try {
            double parsed = Double.parseDouble (now);
            filters.forEach (i -> i.test (now, parsed));
            clearMessage ();
        }
        catch (NumberFormatException err) {
            setMessage (State.ERROR, parseErrorMessage);
        }
        catch (FilterException err) {
            setMessage (State.ERROR, err.getMessage ());
        }
    }
}
