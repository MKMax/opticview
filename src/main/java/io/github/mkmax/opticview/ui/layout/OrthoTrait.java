package io.github.mkmax.opticview.ui.layout;

import io.github.mkmax.opticview.util.IDisposable;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.Objects;

/* utility implementation of an OrthoComponent */
public final class OrthoTrait implements IOrthoComponent, IDisposable {

    /* +----------------------+ */
    /* | PROPERTIES & MEMBERS | */
    /* +----------------------+ */

    /* @NOTE(max): This may be a terrible way to handle multiple setters per event trigger */
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
        /* this method is essentially an alias for width.set(nWidth); */
        if (widthBindingPoint != nodeWidthProperty)
            throw new RuntimeException ("Prohibited to set the width of a bound component/property");
        nodeWidthSetter.accept (nWidth);
    }

    @Override public ReadOnlyDoubleProperty heightPropertyOC ()               { return height;                   }
    @Override public double                 getHeightOC      ()               { return height.get ();            }
    @Override public void                   setHeightOC      (double nHeight) {
        /* this method is essentially an alias for height.set(nHeight); */
        if (heightBindingPoint != nodeHeightProperty)
            throw new RuntimeException ("Prohibited to set the height of a bound component/property");
        nodeHeightSetter.accept (nHeight);
    }

    /* Fix for double loss of accuracy for too large or too small of values */
    /* @TODO(max): Use BigDecimal */
    private static final double MIN_RANGE = 1e-14d;
    private static final double MAX_RANGE = 1e14d;

    @Override public ReadOnlyDoubleProperty leftProperty     ()               { return left;                     }
    @Override public double                 getLeft          ()               { return left.get ();              }
    @Override public void                   setLeft          (double nLeft)   {
        /* prevent double values losing enough accuracy to go into zeroes */
        final double spandiff = Math.abs (getRight () - nLeft);
        if (MIN_RANGE <= spandiff && spandiff <= MAX_RANGE)
            left.set (nLeft);
    }

    @Override public ReadOnlyDoubleProperty rightProperty    ()               { return right;                    }
    @Override public double                 getRight         ()               { return right.get ();             }
    @Override public void                   setRight         (double nRight)  {
        /* prevent double values losing enough accuracy to go into zeroes */
        final double spandiff = Math.abs (nRight - getLeft ());
        if (MIN_RANGE <= spandiff && spandiff <= MAX_RANGE)
            right.set (nRight);
    }

    @Override public ReadOnlyDoubleProperty bottomProperty   ()               { return bottom;                   }
    @Override public double                 getBottom        ()               { return bottom.get ();            }
    @Override public void                   setBottom        (double nBottom) {
        /* prevent double values losing enough accuracy to go into zeroes */
        final double spandiff = Math.abs (getTop () - nBottom);
        if (MIN_RANGE <= spandiff && spandiff <= MAX_RANGE)
            bottom.set (nBottom);
    }

    @Override public ReadOnlyDoubleProperty topProperty      ()               { return top;                      }
    @Override public double                 getTop           ()               { return top.get ();               }
    @Override public void                   setTop           (double nTop)    {
        /* prevent double values losing enough accuracy to go into zeroes */
        final double spandiff = Math.abs (nTop - getBottom ());
        if (MIN_RANGE <= spandiff && spandiff <= MAX_RANGE)
            top.set (nTop);
    }

    /* FUSED VERTICAL & HORIZONTAL SETTERS */
    @Override
    public void setHorizontal (double nLeft, double nRight) {
        /* use atomics to control event triggers which may be expensive */
        inFusedHorizontalModify = true;
        /* prevent double values losing enough accuracy to go into zeroes */
        final double spandiff = Math.abs (nRight - nLeft);
        if (MIN_RANGE <= spandiff && spandiff <= MAX_RANGE) {
            left.set (nLeft);
            right.set (nRight);
        }
        inFusedHorizontalModify = false;

        /* manually trigger the horizontal change event after it completes */
        onHorizontalParamsChanged (false, Double.NaN);
    }

    @Override
    public void setVertical (double nBottom, double nTop) {
        /* use atomics to control event triggers which may be expensive */
        inFusedVerticalModify = true;
        /* prevent double values losing enough accuracy to go into zeroes */
        final double spandiff = Math.abs (nTop - nBottom);
        if (MIN_RANGE <= spandiff && spandiff <= MAX_RANGE) {
            bottom.set (nBottom);
            top.set (nTop);
        }
        inFusedVerticalModify = false;

        /* manually trigger the vertical change event after it completes */
        onVerticalParamsChanged (false, Double.NaN);
    }

    /* FUSED WINDOW SETTER */
    @Override
    public void setWindow (double nLeft, double nRight, double nBottom, double nTop) {
        /* as with setHorizontal() and setVertical(), we use atomics to control event triggers */
        inFusedWindowModify = true;
        /* prevent double values losing enough accuracy to go into zeroes */
        /* --- HORIZONTAL --- */
        final double hspandiff = Math.abs (nRight - nLeft);
        if (MIN_RANGE <= hspandiff && hspandiff <= MAX_RANGE) {
            left.set (nLeft);
            right.set (nRight);
        }
        /* --- VERTICAL --- */
        final double vspandiff = Math.abs (nTop - nBottom);
        if (MIN_RANGE <= vspandiff && vspandiff <= MAX_RANGE) {
            bottom.set (nBottom);
            top.set (nTop);
        }
        inFusedWindowModify = false;

        /* manually trigger a single change listener to update to changes in one routine */
        onWindowParamsChanged (false, false, Double.NaN, Double.NaN);
    }

    /* +----------------------+ */
    /* | LISTENER MANAGEMENT  | */
    /* +----------------------+ */

    private final List<RemapListener> horremaplisteners = new ArrayList<> ();
    private final List<RemapListener> verremaplisteners = new ArrayList<> ();
    private final List<RemapListener> winremaplisteners = new ArrayList<> ();

    /* HORIZONTAL REMAP LISTENERS */
    @Override
    public void registerHorizontalRemapListener (RemapListener lis) {
        if (lis != null) horremaplisteners.add (lis);
    }

    @Override
    public void removeHorizontalRemapListener (RemapListener lis) {
        horremaplisteners.remove (lis);
    }

    /* VERTICAL REMAP LISTENERS */
    @Override
    public void registerVerticalRemapListener (RemapListener lis) {
        if (lis != null) verremaplisteners.add (lis);
    }

    @Override
    public void removeVerticalRemapListener (RemapListener lis) {
        verremaplisteners.remove (lis);
    }

    /* WINDOW REMAP LISTENERS */
    @Override
    public void registerWindowRemapListener (RemapListener lis) {
        if (lis != null) winremaplisteners.add (lis);
    }

    @Override
    public void removeWindowRemapListener (RemapListener lis) {
        winremaplisteners.remove (lis);
    }

    /* +----------+ */
    /* | INTERNAL | */
    /* +----------+ */

    private double
        Mx, iMx, Cx,
        My, iMy, Cy;

    private final ChangeListener<Number> onHorizontalParamChangedListener = (obs, __old, now) ->
        onHorizontalParamsChanged (obs == width, now == null ? Double.NaN : now.doubleValue ());
    private final ChangeListener<Number> onVerticalParamChangedListener = (obs, __old, now) ->
        onVerticalParamsChanged (obs == height, now == null ? Double.NaN : now.doubleValue ());

    private void onHorizontalParamsChanged (
        boolean isWidthProperty,
        double nWidthValue)
    {
        if (inFusedWindowModify || inFusedHorizontalModify)
            return;
        recomputeHorizontalMap ();
        if (isWidthProperty && widthBindingPoint != nodeWidthProperty)
            nodeWidthSetter.accept (nWidthValue);
        horremaplisteners.forEach (l -> l.onRemap (this));
    }

    private void onVerticalParamsChanged (
        boolean isHeightProperty,
        double nHeightValue)
    {
        if (inFusedWindowModify || inFusedVerticalModify)
            return;
        recomputeVerticalMap ();
        if (isHeightProperty && heightBindingPoint != nodeHeightProperty)
            nodeHeightSetter.accept (nHeightValue);
        verremaplisteners.forEach (l -> l.onRemap (this));
    }

    /* used to trigger an update event once for changes in both the horizontal and vertical */
    private void onWindowParamsChanged (
        boolean widthPropertyChanged,
        boolean heightPropertyChanged,
        double nWidthValue,
        double nHeightValue)
    {
        if (inFusedWindowModify) /* shouldn't happen, but we have it here just in case */
            return;
        recomputeHorizontalMap ();
        recomputeVerticalMap ();
        if (widthPropertyChanged && widthBindingPoint != nodeWidthProperty)
            nodeWidthSetter.accept (nWidthValue);
        if (heightPropertyChanged && heightBindingPoint != nodeHeightProperty)
            nodeHeightSetter.accept (nHeightValue);
        winremaplisteners.forEach (l -> l.onRemap (this));
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
    public OrthoTrait (
        ReadOnlyDoubleProperty pNodeWidthProperty,
        ReadOnlyDoubleProperty pNodeHeightProperty,
        DoubleConsumer pNodeWidthSetter,
        DoubleConsumer pNodeHeightSetter)
    {
        /* configure node-related settings first */
        nodeWidthProperty  = Objects.requireNonNull (pNodeWidthProperty);
        nodeHeightProperty = Objects.requireNonNull (pNodeHeightProperty);
        nodeWidthSetter    = Objects.requireNonNull (pNodeWidthSetter);
        nodeHeightSetter   = Objects.requireNonNull (pNodeHeightSetter);

        /* install listeners */
        width .addListener (onHorizontalParamChangedListener);
        left  .addListener (onHorizontalParamChangedListener);
        right .addListener (onHorizontalParamChangedListener);
        height.addListener (onVerticalParamChangedListener);
        bottom.addListener (onVerticalParamChangedListener);
        top   .addListener (onVerticalParamChangedListener);

        /* bind width/height immediately to the node's width/height */
        inFusedWindowModify = true; /* disable automatic event triggering */
        width.bind (widthBindingPoint = nodeWidthProperty);
        height.bind (heightBindingPoint = nodeHeightProperty);
        inFusedWindowModify = false; /* allow automatic event triggering */
        onWindowParamsChanged (true, true, getWidthOC (), getHeightOC ());
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
    public void bindOrtho (IOrthoComponent to) {
        /* just as with the fused setters, we will only fire a remap event once */
        inFusedWindowModify = true;
        width .bind (widthBindingPoint = to.widthPropertyOC ());
        height.bind (heightBindingPoint = to.heightPropertyOC ());
        left  .bind (to.leftProperty ());
        right .bind (to.rightProperty ());
        bottom.bind (to.bottomProperty ());
        top   .bind (to.topProperty ());
        inFusedWindowModify = false;

        /* manually trigger the total remap event after binding is complete */
        onWindowParamsChanged (true, true, getWidthOC (), getHeightOC ());
    }

    @Override
    public void unbindOrtho () {
        inFusedWindowModify = true;
        width .bind (widthBindingPoint = nodeWidthProperty);
        height.bind (heightBindingPoint = nodeHeightProperty);
        left  .unbind ();
        right .unbind ();
        bottom.unbind ();
        top   .unbind ();
        inFusedWindowModify = false;
        /* we don't need to update the mapping since it's already up to date */
    }

    @Override
    public void dispose () {
        width .removeListener (onHorizontalParamChangedListener);
        left  .removeListener (onHorizontalParamChangedListener);
        right .removeListener (onHorizontalParamChangedListener);
        height.removeListener (onVerticalParamChangedListener);
        bottom.removeListener (onVerticalParamChangedListener);
        top   .removeListener (onVerticalParamChangedListener);
        width .unbind ();
        height.unbind ();
    }
}
