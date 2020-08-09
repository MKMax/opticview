package io.github.mkmax.opticview.scene.controls;

import io.github.mkmax.opticview.material.Glass;

import javafx.scene.control.ListCell;

public class GlassListCell extends ListCell<Glass> {



    /* +---------------+ */
    /* | STYLE CLASSES | */
    /* +---------------+ */
    public static final class ComponentClass {
        public static final String GLASS_LIST_CELL = "glass-list-cell";
    }

    /* +----------------+ */
    /* | INITIALIZATION | */
    /* +----------------+ */
    {
        getStyleClass ().add (ComponentClass.GLASS_LIST_CELL);
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */
    @Override
    protected void updateItem (Glass item, boolean empty) {
        /* super invocation is required, do not touch */
        super.updateItem (item, empty);

        /* handle the rest of the functionality */
        if (item != null && !empty)
            setText (item.NAME);
    }

}
