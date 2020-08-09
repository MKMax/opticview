package io.github.mkmax.opticview.scene.controls;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

import java.util.Objects;
import java.util.Optional;

public class DoubleField extends TextField {

    /* +---------------+ */
    /* | STYLE CLASSES | */
    /* +---------------+ */
    public static final class ComponentClass {
        public static final String DOUBLE_FIELD = "double-field";
    }

    private enum StateClass {
        NORMAL  ("normal"),
        ERROR   ("error");

        public final String STATE_CLASS;

        StateClass (String stateClass) {
            STATE_CLASS = Objects.requireNonNull (stateClass);
        }
    }

    /* +--------------------------+ */
    /* | MEMBERS & INITIALIZATION | */
    /* +--------------------------+ */
    private StateClass currentState = StateClass.NORMAL;

    public DoubleField () {
        getStyleClass ().addAll (ComponentClass.DOUBLE_FIELD, currentState.STATE_CLASS);
        textProperty ().addListener (onTextChanged);
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */
    private final ChangeListener<String> onTextChanged = (__obs, old, now) -> {
        if (now == null || now.length () == 0)
            setState (StateClass.NORMAL);
        else {
            try {
                Double.parseDouble (now);
                setState (StateClass.NORMAL);
            }
            catch (NumberFormatException err) {
                setState (StateClass.ERROR);
            }
        }
    };

    /* VALUE */
    public void setValue (double value) {
        setText (Double.toString (value));
    }

    public double getValue () {
        return Double.parseDouble (getText ());
    }

    public Optional<Double> getValueSafely () {
        try {
            return Optional.of (getValue ());
        }
        catch (NumberFormatException e) {
            return Optional.empty ();
        }
    }

    /* +----------+ */
    /* | INTERNAL | */
    /* +----------+ */

    /* STATE */
    public void setState (StateClass state) {
        Objects.requireNonNull (state, "A state must be specified for the message");
        if (state != currentState) {
            /* add the new state class first */
            getStyleClass ().add (state.STATE_CLASS);

            /* remove old state class after adding new one */
            getStyleClass ().remove (currentState.STATE_CLASS);
        }
        currentState = state;
    }
}
