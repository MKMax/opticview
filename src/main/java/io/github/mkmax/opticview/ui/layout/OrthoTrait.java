package io.github.mkmax.opticview.ui.layout;

import io.github.mkmax.opticview.util.Disposable;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.Objects;

/* utility implementation of an OrthoComponent */
public final class OrthoTrait implements OrthoComponent, Disposable {

    /* +----------------------+ */
    /* | PROPERTIES & MEMBERS | */
    /* +----------------------+ */

    private volatile boolean
        inFusedHorizontalModify = false,
        inFusedVerticalModify   = false,
        inFusedWindowModify     = false;

    private final ReadOnlyDoubleProperty
        nodeWidthProperty,
        nodeHeightProperty;
    private final DoubleConsumer
        nodeWidthSetter,
        nodeHeightSetter;
    private ReadOnlyDoubleProperty
        widthBindingPoint,
        heightBindingPoint;
    private final SimpleDoubleProperty
        width  = new SimpleDoubleProperty (),
        height = new SimpleDoubleProperty (),
        left   = new SimpleDoubleProperty (),
        right  = new SimpleDoubleProperty (),
        bottom = new SimpleDoubleProperty (),
        top    = new SimpleDoubleProperty ();

    /* GETTER & SETTERS */
    @Override public ReadOnlyDoubleProperty widthPropertyOC  ()               { return width;                    }
    @Override public double                 getWidthOC       ()               { return width.get ();             }
    @Override public void                   setWidthOC       (double nWidth)  {
        if (widthBindingPoint == nodeWidthProperty) nodeWidthSetter.accept (nWidth);
    }

    @Override public ReadOnlyDoubleProperty heightPropertyOC ()               { return height;                   }
    @Override public double                 getHeightOC      ()               { return height.get ();            }
    @Override public void                   setHeightOC      (double nHeight) {
        if (heightBindingPoint == nodeHeightProperty) nodeHeightSetter.accept (nHeight);
    }

    @Override public ReadOnlyDoubleProperty leftProperty     ()               { return left;                     }
    @Override public double                 getLeft          ()               { return left.get ();              }
    @Override public void                   setLeft          (double nLeft)   { left.set (nLeft);                }

    @Override public ReadOnlyDoubleProperty rightProperty    ()               { return right;                    }
    @Override public double                 getRight         ()               { return right.get ();             }
    @Override public void                   setRight         (double nRight)  { right.set (nRight);              }

    @Override public ReadOnlyDoubleProperty bottomProperty   ()               { return bottom;                   }
    @Override public double                 getBottom        ()               { return bottom.get ();            }
    @Override public void                   setBottom        (double nBottom) { bottom.set (nBottom);            }

    @Override public ReadOnlyDoubleProperty topProperty      ()               { return top;                      }
    @Override public double                 getTop           ()               { return top.get ();               }
    @Override public void                   setTop           (double nTop)    { top.set (nTop);                  }

    /* FUSED VERTICAL & HORIZONTAL SETTERS */
    @Override
    public void setHorizontal (double nLeft, double nRight) {
        /* use atomics to control event triggers which may be expensive */
        inFusedHorizontalModify = true;
        setLeft (nLeft);
        setRight (nRight);
        inFusedHorizontalModify = false;

        /* manually trigger the horizontal change event after it completes */
        onHorizontalParamChanged.changed (null, null, null);
    }

    @Override
    public void setVertical (double nBottom, double nTop) {
        /* use atomics to control event triggers which may be expensive */
        inFusedVerticalModify = true;
        setBottom (nBottom);
        setTop (nTop);
        inFusedVerticalModify = false;

        /* manually trigger the vertical change event after it completes */
        onVerticalParamChanged.changed (null, null, null);
    }

    /* FUSED WINDOW SETTER */
    @Override
    public void setWindow (double nLeft, double nRight, double nBottom, double nTop) {
        /* as with setHorizontal() and setVertical(), we use atomics to control event triggers */
        inFusedWindowModify = true;
        setLeft (nLeft);
        setRight (nRight);
        setBottom (nBottom);
        setTop (nTop);
        inFusedWindowModify = false;

        /* manually trigger a single change listener to update to changes in one routine */
        onTotalParamChange ();
    }

    /* +----------------------+ */
    /* | LISTENER MANAGEMENT  | */
    /* +----------------------+ */

    private final List<HorizontalRemapListener> horremaplisteners = new ArrayList<> ();
    private final List<VerticalRemapListener>   verremaplisteners = new ArrayList<> ();
    private final List<TotalRemapListener>      totremaplisteners = new ArrayList<> ();

    /* HORIZONTAL REMAP LISTENERS */
    @Override
    public void addRemapListener (HorizontalRemapListener lis) {
        if (lis != null) horremaplisteners.add (lis);
    }

    @Override
    public void removeRemapListener (HorizontalRemapListener lis) {
        horremaplisteners.remove (lis);
    }

    /* VERTICAL REMAP LISTENERS */
    @Override
    public void addRemapListener (VerticalRemapListener lis) {
        if (lis != null) verremaplisteners.add (lis);
    }

    @Override
    public void removeRemapListener (VerticalRemapListener lis) {
        verremaplisteners.remove (lis);
    }

    /* TOTAL REMAP LISTENERS */
    @Override
    public void addRemapListener (TotalRemapListener lis) {
        if (lis != null) totremaplisteners.add (lis);
    }

    @Override
    public void removeRemapListener (TotalRemapListener lis) {
        totremaplisteners.remove (lis);
    }

    /* +----------+ */
    /* | INTERNAL | */
    /* +----------+ */

    private double
        Mx, iMx, Cx,
        My, iMy, Cy;

    private final ChangeListener<Number> onHorizontalParamChanged = (__obs, __old, __now) -> {
        if (inFusedWindowModify || inFusedHorizontalModify)
            return;
        recomputeHorizontalMap ();
        horremaplisteners.forEach (l -> l.onRemap (this));
    };

    private final ChangeListener<Number> onVerticalParamChanged = (__obs, __old, __now) -> {
        if (inFusedWindowModify || inFusedVerticalModify)
            return;
        recomputeVerticalMap ();
        verremaplisteners.forEach (l -> l.onRemap (this));
    };

    /* used by setWindow() to trigger an update event once */
    private void onTotalParamChange () {
        if (inFusedWindowModify) /* shouldn't happen, but we have it here just in case */
            return;
        recomputeHorizontalMap ();
        recomputeVerticalMap ();
        totremaplisteners.forEach (l -> l.onRemap (this));
    }

    private void recomputeHorizontalMap () {
        final double
            w = width.get (),
            l = getLeft (),
            r = getRight ();
        Mx = w / (r - l);
        iMx = 1d / Mx;
        Cx = -Mx * l;
    }

    private void recomputeVerticalMap () {
        final double
            h = height.get (),
            b = getBottom (),
            t = getTop ();
        My = h / (b - t);
        iMy = 1d / My;
        Cy = -My * t;
    }

    /* +----------------+ */
    /* | INITIALIZATION | */
    /* +----------------+ */

    /* install the listeners before constructor to automatically update based on width/height */
    {
        width.addListener (onHorizontalParamChanged);
        left.addListener (onHorizontalParamChanged);
        right.addListener (onHorizontalParamChanged);
        height.addListener (onVerticalParamChanged);
        bottom.addListener (onVerticalParamChanged);
        top.addListener (onVerticalParamChanged);
    }

    public OrthoTrait (
        ReadOnlyDoubleProperty pNodeWidthProperty,
        ReadOnlyDoubleProperty pNodeHeightProperty,
        DoubleConsumer pNodeWidthSetter,
        DoubleConsumer pNodeHeightSetter)
    {
        nodeWidthProperty  = Objects.requireNonNull (pNodeWidthProperty);
        nodeHeightProperty = Objects.requireNonNull (pNodeHeightProperty);
        nodeWidthSetter    = Objects.requireNonNull (pNodeWidthSetter);
        nodeHeightSetter   = Objects.requireNonNull (pNodeHeightSetter);

        width.bind (widthBindingPoint = nodeWidthProperty);
        height.bind (heightBindingPoint = nodeHeightProperty);
    }

    /* +-----------------+ */
    /* | MAPPING & OTHER | */
    /* +-----------------+ */

    @Override
    public double mapToComponentX (double x) {
        return Mx * x + Cx;
    }

    @Override
    public double mapToComponentY (double y) {
        return My * y + Cy;
    }

    @Override
    public double mapToVirtualX (double x) {
        return (x - Cx) * iMx;
    }

    @Override
    public double mapToVirtualY (double y) {
        return (y - Cy) * iMy;
    }

    @Override
    public void dispose () {
        width.removeListener (onHorizontalParamChanged);
        left.removeListener (onHorizontalParamChanged);
        right.removeListener (onHorizontalParamChanged);
        height.removeListener (onVerticalParamChanged);
        bottom.removeListener (onVerticalParamChanged);
        top.removeListener (onVerticalParamChanged);
    }
}
