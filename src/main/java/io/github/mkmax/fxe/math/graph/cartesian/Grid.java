package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.fxe.math.graph.cartesian.partitioning.DecimalPartitioner;
import io.github.mkmax.fxe.math.graph.cartesian.partitioning.Partitioner;
import io.github.mkmax.util.PrecisionDecimalFormat;
import io.github.mkmax.util.ScientificDecimalFormat;
import io.github.mkmax.util.math.LinearInterpolatord;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.joml.Rectangled;

import java.util.ArrayList;
import java.util.List;

public final class Grid extends Pane {

    private static final class Guide {
        public Rectangle guide = new Rectangle ();
        public Label     label = new Label ();
    }

    private final List<Guide> horizontal   = new ArrayList<> ();
    private final List<Guide> vertical     = new ArrayList<> ();
    private double            labelPadding = 5d;

    private PrecisionDecimalFormat  pdf         = new PrecisionDecimalFormat (3);
    private ScientificDecimalFormat sdf         = new ScientificDecimalFormat (3);
    private double                  formatMin   = 1e-3d;
    private double                  formatMax   = 1e3d;

    private Partitioner             partitioner = DecimalPartitioner.INSTANCE;
    private double                  fpu         = 128d;

    protected Grid () {
    }

    protected double getLabelPadding () {
        return labelPadding;
    }

    protected String format (double value) {
        if (formatMin <= value && value <= formatMax)
            return pdf.format (value);
        return sdf.format (value);
    }

    protected void update (
        Rectangled          window,
        Rectangled          viewport,
        LinearInterpolatord xmap,
        LinearInterpolatord ymap)
    {
        if (!(getParent () instanceof GraphView))
            return;
        final GraphView parent = (GraphView) getParent ();

    }
}
