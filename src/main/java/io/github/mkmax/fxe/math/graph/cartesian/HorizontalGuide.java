package io.github.mkmax.fxe.math.graph.cartesian;

import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class HorizontalGuide extends AbstractGuide {

    /* +---------------------+ */
    /* | DATA + CONSTRUCTORS | */
    /* +---------------------+ */

    private final Rectangle guide = new Rectangle ();
    private final Label     label = new Label ();

    HorizontalGuide (Grid pView) {
        super (pView);
        getChildren ().addAll (guide, label);
        update ();
    }

    /* +-------------------------------------+ */
    /* | LISTENERS + DISPOSE IMPLEMENTATIONS | */
    /* +-------------------------------------+ */

    @Override
    public void dispose () {
        super.dispose ();
        getChildren ().removeAll (guide, label);
    }

    @Override
    protected void update () {
        setWidth (grid.getWidth ());
        setMaxHeight (0d);
        setHeight (0d);

        setLayoutX (0d);
        setLayoutY (getPosition ());

        updateGuide ();
        updateLabel ();
    }

    private void updateGuide () {
        guide.setFill (grid.getGuideFill (getDegree ()));
        guide.setHeight (grid.getGuideWidth (getDegree ()));

        guide.setWidth (getWidth ());
        guide.setLayoutX (0d);
        guide.setLayoutY (-guide.getHeight () / 2);
    }

    private void updateLabel () {
        /* (x,y) is the real position of the label */
        double x;
        double y;

        switch (grid.getHorLabelJustify ()) {
        case LEFT:
            x = getLabelPosition () - label.getWidth ();
            y = -label.getHeight () / 2;
            break;
        case RIGHT:
            x = getLabelPosition ();
            y = -label.getHeight () / 2;
        case BOTTOM:
            x = getLabelPosition () - label.getWidth () / 2;
            y = 0d;
        case TOP:
            x = getLabelPosition () - label.getWidth () / 2;
            y = -label.getHeight ();
        default:
            /* should never reach here but ok */
            x = getLabelPosition ();
            y = 0d;
        }

        label.setTextFill (grid.getLabelForeground (getDegree ()));
        label.setBackground (grid.getLabelBackground (getDegree ()));

        label.setText (grid.formatGuideValue (getValue ()));
        label.setLayoutX (x);
        label.setLayoutY (y);
    }
}
