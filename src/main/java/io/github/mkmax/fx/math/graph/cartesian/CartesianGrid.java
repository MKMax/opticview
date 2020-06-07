package io.github.mkmax.fx.math.graph.cartesian;

import static io.github.mkmax.jim.JimStatics.equal;
import io.github.mkmax.fx.scene.MappedRegion;

import javafx.css.StyleablePropertyFactory;
import javafx.scene.layout.Pane;

import java.util.*;

public class CartesianGrid extends MappedRegion {

    /* +-----------------+ */
    /* | COUPLED CLASSES | */
    /* +-----------------+ */

    public enum CoreGuideStyleEIDs {
        ORIGIN (0),
        MAJOR (1),
        MINOR (2);

        private static final CoreGuideStyleEIDs[] TABLE = {
            ORIGIN,
            MAJOR,
            MINOR
        };

        public final int ID;

        CoreGuideStyleEIDs (final int pID) {
            ID = pID;
        }
    }

    public interface AxisPartitioner {
        Collection<Map.Entry<Number, Integer>> partition (
            double axisIntervalBegin,
            double axisIntervalEnd,
            double pixelToIntervalRatio,
            double minPixelsPerTick);
    }

    public static final AxisPartitioner DECIMAL_PARTITIONER = (begin, end, ratio, ppt) -> {
        /* TODO: comment this code later */
        final double min = Math.min (begin, end);
        final double max = Math.max (begin, end);

        final double unit  = ppt / Math.abs (ratio);
        final double log10 = Math.ceil (Math.log10 (unit));
        final double pow10 = Math.pow (10, log10);
        final double norm  = unit / pow10;

        double majstep, minstep;
        if (norm <= 0.2d) {
            majstep = 0.2d * pow10;
            minstep = 0.25d * majstep;
        }
        else if (norm <= 0.5d) {
            majstep = 0.5d * pow10;
            minstep = 0.2d * majstep;
        }
        else {
            majstep = 1.0d * pow10;
            minstep = 0.2d * majstep;
        }

        double majstart, minstart;
        if (min < 0) {
            majstart = min - (min % majstep);
            minstart = min - (min % minstep);
        }
        else {
            majstart = min + (majstep - min % majstep);
            minstart = min + (minstep - min % minstep);
        }

        if (equal (min, majstart))
            majstart += majstep;
        if (equal (min, minstart))
            minstart += minstep;
        if (minstart >= max)
            return Collections.emptyList ();

        /* TODO: refactor later for clarity and readability */
        final int total = (int) Math.floor ((max - minstart) / minstep) + 1;
        final Collection<Map.Entry<Number, Integer>> indices = new ArrayList<> (total);
        int majpoint = 0;
        for (int minpoint = 0; minpoint < total; ++minpoint) {
            int styleId = CoreGuideStyleEIDs.MINOR.ID;
            double minpos = minstart + minpoint * minstep;
            double majpos = majstart + majpoint * majstep;

            if (equal (minpos, majpos)) {
                styleId = CoreGuideStyleEIDs.MAJOR.ID;
                ++majpoint;
            }

            if (equal (minpos, 0d))
                styleId = CoreGuideStyleEIDs.ORIGIN.ID;

            indices.add (Map.entry (minpos, styleId));
        }
        return indices;
    };

    /* +---------------------+ */
    /* | STYLESHEET HANDLING | */
    /* +---------------------+ */

    private static final StyleablePropertyFactory<CartesianGrid>
        FACTORY = new StyleablePropertyFactory<> (MappedRegion.getClassCssMetaData ());

    /* +----------------------+ */
    /* | PROPERTIES & MEMBERS | */
    /* +----------------------+ */

    private final Pane lines = new Pane ();
    private final Pane labels = new Pane ();

    /* TODO: Decide how style objects will work */
    private final List<Object> guideStyleTable = new ArrayList<> (CoreGuideStyleEIDs.TABLE.length);

    /* +-------------------------------+ */
    /* | CONSTRUCTORS & INITIALIZATION | */
    /* +-------------------------------+ */

    public CartesianGrid (
        double pLeft,
        double pRight,
        double pBottom,
        double pTop)
    {
        super (pLeft, pRight, pBottom, pTop);
    }

    public CartesianGrid (double pUniform) {
        super (pUniform);
    }

    public CartesianGrid () {
        super ();
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */


}
