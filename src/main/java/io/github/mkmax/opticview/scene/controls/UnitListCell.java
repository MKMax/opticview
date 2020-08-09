package io.github.mkmax.opticview.scene.controls;

import io.github.mkmax.opticview.units.IUnit;

import javafx.scene.control.ListCell;

import java.util.Objects;

public class UnitListCell<T extends IUnit> extends ListCell<T> {

    public enum DisplayMode {
        MNEMONIC_ONLY,
        NAME_ONLY,
        NAME_AND_MNEMONIC
    }

    /* +---------------+ */
    /* | STYLE CLASSES | */
    /* +---------------+ */
    public static final class ComponentClass {
        public static final String UNIT_LIST_CELL = "unit-list-cell";
    }

    /* +--------------------------+ */
    /* | INITIALIZATION & MEMBERS | */
    /* +--------------------------+ */
    private final DisplayMode displayMode;

    public UnitListCell (DisplayMode pDisplayMode) {
        getStyleClass ().add (ComponentClass.UNIT_LIST_CELL);
        displayMode = Objects.requireNonNull (pDisplayMode, "a display mode must be specified");
    }

    public UnitListCell () {
        this (DisplayMode.NAME_AND_MNEMONIC);
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */
    @Override
    protected void updateItem (T item, boolean empty) {
        /* super invocation is required, do not touch */
        super.updateItem (item, empty);

        /* handle the rest of the functionality */
        if (item != null && !empty) {
            switch (displayMode) {
                case NAME_AND_MNEMONIC:
                    setText (String.format ("[%s] %s", item.getMnemonic (), item.getName ()));
                    break;
                case MNEMONIC_ONLY:
                    setText (item.getMnemonic ());
                    break;
                case NAME_ONLY:
                    setText (item.getName ());
                    break;
            }
        }
    }
}
