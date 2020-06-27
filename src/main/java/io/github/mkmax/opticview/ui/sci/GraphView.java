package io.github.mkmax.opticview.ui.sci;

import io.github.mkmax.opticview.ui.OrthoRegion;
import io.github.mkmax.opticview.util.Disposable;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;

public final class GraphView extends OrthoRegion implements Disposable {

    /* settings */
    private final ObservableList<Color> idxcolors = FXCollections.observableArrayList ();

    public ReadOnlyListProperty<Color> getIndexColors () {
        return idxcolors;
    }

    /* UI devices */
    private final Canvas canvas = new Canvas ();
    private final GraphicsContext gc = canvas.getGraphicsContext2D ();

    /* core members */
    private final GraphData dataref;

    public GraphView (GraphData ref) {
        dataref = Objects.requireNonNull (ref);
    }





    private void render () {
        final double
            width  = getWidth (),
            height = getHeight (),
            left   = getLeft (),
            right  = getRight ();
        /* preform initial setup */
        gc.clearRect (0, 0, canvas.getWidth (), canvas.getHeight ());
        canvas.setWidth (width);
        canvas.setHeight (height);

        /* render */
        final double step = (right - left) / width;
        for (int i = 0; i < functions.size (); ++i) {
            final FuncMeta funcm = functions.get (i);
            gc.setStroke (COLORS[i % COLORS.length]);
            gc.setLineWidth (funcm.lineWidth);
            double oldy = Double.NaN;
            for (int j = 0; j <= width; ++j) {
                double y = funcm.func.apply (left + step * j);
                double nowx = j;
                double nowy = mapy (y);
                if (!Double.isNaN (oldy) &&
                    ((0d <= oldy && oldy <= height) ||
                     (0d <= nowy && nowy <= height)) )
                {
                    gc.strokeLine (j - 1, oldy, j, nowy);
                }
                oldy = nowy;
            }
        }
    }

    private void updatelegend () {
        /* check for additions */
        for (int i = 0; i < functions.size (); ++i) {
            final FuncMeta funcm = functions.get (i);
            if (!funclegends.containsKey (funcm)) {
                final LegendEntry nlegend = new LegendEntry (funcm);
                nlegend.color.set (funcm.color);
                nlegend.font.set (LEGEND_FONT);
                nlegend.name.set (funcm.name);
                legendbox.getChildren ().add (nlegend);
                funclegends.put (funcm, nlegend);
            }
        }

        /* check for removal */
        Iterator<FuncMeta> it = funclegends.keySet ().iterator ();
        for (int i = 0; it.hasNext (); ++i) {
            final FuncMeta funcm = it.next ();
            if (!functions.contains (funcm)) {
                final LegendEntry le = funclegends.get (funcm);
                legendbox.getChildren ().remove (le);
                funclegends.remove (funcm);
                le.dispose ();
            }
        }
    }
}
