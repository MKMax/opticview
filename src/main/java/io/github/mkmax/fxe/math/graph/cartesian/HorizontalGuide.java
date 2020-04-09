package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.util.math.Floats;
import io.github.mkmax.util.math.LinearInterpolatord;
import org.joml.Rectangled;

import java.text.DecimalFormat;

public final class HorizontalGuide extends Guide {

    protected HorizontalGuide () {
        setMaxHeight (0d);
    }

    @Override
    protected void update (
        Rectangled          window,
        Rectangled          viewport,
        LinearInterpolatord xmap,
        LinearInterpolatord ymap)
    {
        if (!(getParent () instanceof Grid))
            return;
        final Grid grid = (Grid) getParent ();

        /* adjust pane so it would work with most JavaFX layouts */
        setLayoutX (0d);
        setLayoutY (pos);
        setWidth (grid.getWidth ());

        /* Adjust the position and dimension of the guiding line */
        guide.setWidth (grid.getWidth ());
        guide.setHeight (span);
        guide.setLayoutX (0d);
        guide.setLayoutY (-span * 0.5d);

        /* Set the label text before adjusting to receive proper layout bounds */
        label.setText (grid.format (val));
        double lw = label.getWidth ();
        double lh = label.getHeight ();
        double hlw = lw / 2;
        double hlh = lh / 2;

        /* We define the center of the label to act as a basis point */
        double lx = 0d, ly = 0d;

        /* We define the label position offsets to account for the fact that
         * positioning nodes on the scene graph is relative to the top left corner
         * of the content.
         */
        double dx = 0d, dy = 0d;

        /* Now we properly position the label */
        switch (labelJustification) {
        case LEFT:
            dx = -lw;
            dy = -hlh;
            break;
        case RIGHT:
            dx = 0d;
            dy = -hlh;
            break;
        case BOTTOM:
            dx = -hlw;
            dy = 0d;
            break;
        case TOP:
            dx = -hlw;
            dy = -lh;
            break;
        case CENTER:
            dx = -hlw;
            dy = -hlh;
            break;
        }

        /* We find the X value of the Y axis so we can adjust the position of the label */
        final double yaxis = xmap.map (0d);
        lx = Floats.clamp (
            yaxis,
            grid.getLabelPadding () - dx,
            grid.getWidth () - grid.getLabelPadding () - lw - dx);
        ly = 0d;

        /* we now make sure the components are visible, if not, we hide them */
        final double hMaxHeight = Math.max (span / 2, hlh);
        if (pos + hMaxHeight <= 0 || grid.getHeight () <= pos - hMaxHeight) {
            guide.setVisible (false);
            label.setVisible (false);
        }
        else {
            guide.setVisible (true);
            label.setVisible (true);
        }
    }
}
