package io.github.mathfx.cartesian;

import io.github.mathfx.cartesian.part.PartitionScheme;
import io.github.mathfx.util.css.StyleableFactory;
import io.github.mathfx.util.Disposable;
import javafx.scene.layout.Pane;

import java.util.*;

public class GuideContainer extends Pane implements Disposable {

    public static final String ORIGIN_GUIDE_CLASS = "origin-guide";
    public static final String MAJOR_GUIDE_CLASS  = "major-guide";
    public static final String MINOR_GUIDE_CLASS  = "minor-guide";

    public static final Map<PartitionScheme.Type, String> TYPE_STYLE_CLASS_MAP = Map.of (
        PartitionScheme.Type.MINOR,  MINOR_GUIDE_CLASS,
        PartitionScheme.Type.MAJOR,  MAJOR_GUIDE_CLASS,
        PartitionScheme.Type.ORIGIN, ORIGIN_GUIDE_CLASS
    );

    public static final StyleableFactory<GuideContainer>

    /* +----------------------------+ */
    /* | CSS META DATA DECLARATIONS | */
    /* +----------------------------+ */

    @Override
    public void dispose () {

    }
}
