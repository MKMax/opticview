package io.github.mkmax.opticview.ui.layout;

import io.github.mkmax.opticview.util.Numbers;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleConsumer;

/* utility implementation of an OrthoComponent */
public final class OrthoMixin implements IOrthoDevice {

    /* +----------------------+ */
    /* | PROPERTIES & MEMBERS | */
    /* +----------------------+ */
    private final Object EVENT_DISPATCH_LOCK = new Object ();

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
    /* @TODO(max): replace these properties with raw doubles (for performance & clarity)? */
    private final SimpleDoubleProperty
        width  = new SimpleDoubleProperty (),
        height = new SimpleDoubleProperty (),
        left   = new SimpleDoubleProperty (),
        right  = new SimpleDoubleProperty (),
        bottom = new SimpleDoubleProperty (),
        top    = new SimpleDoubleProperty ();

    /* GETTER & SETTERS */
    @Override public ReadOnlyDoubleProperty deviceWidthProperty ()               { return width;                    }
    @Override public double getDeviceWidth ()               { return width.get ();             }
    @Override public void setDeviceWidth (double nWidth)  {
        /* this method is essentially an alias for width.set(nWidth); */
        if (widthBindingPoint != nodeWidthProperty)
            throw new RuntimeException ("Prohibited to set the width of a bound component/property");
        nodeWidthSetter.accept (nWidth);
    }

    @Override public ReadOnlyDoubleProperty deviceHeightProperty ()               { return height;                   }
    @Override public double getDeviceHeight ()               { return height.get ();            }
    @Override public void setDeviceHeight (double nHeight) {
        /* this method is essentially an alias for height.set(nHeight); */
        if (heightBindingPoint != nodeHeightProperty)
            throw new RuntimeException ("Prohibited to set the height of a bound component/property");
        nodeHeightSetter.accept (nHeight);
    }

    /* Fix for double loss of accuracy for too large or too small of values */
    /* @TODO(max): Use BigDecimal */
    private static final double MIN_RANGE = 1e-15d;
    private static final double MAX_RANGE = 1e15d;

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
        boolean set = false;
        if (MIN_RANGE <= spandiff && spandiff <= MAX_RANGE) {
            left.set (nLeft);
            right.set (nRight);
            set = true;
        }
        inFusedHorizontalModify = false;

        /* manually trigger the horizontal change event after it completes */
        onHorizontalParamsChanged (
            false,
            set,
            set,
            Double.NaN,
            set ? nLeft : Double.NaN,
            set ? nRight : Double.NaN);
    }

    @Override
    public void setVertical (double nBottom, double nTop) {
        /* use atomics to control event triggers which may be expensive */
        inFusedVerticalModify = true;
        /* prevent double values losing enough accuracy to go into zeroes */
        final double spandiff = Math.abs (nTop - nBottom);
        boolean set = false;
        if (MIN_RANGE <= spandiff && spandiff <= MAX_RANGE) {
            bottom.set (nBottom);
            top.set (nTop);
            set = true;
        }
        inFusedVerticalModify = false;

        /* manually trigger the vertical change event after it completes */
        onVerticalParamsChanged (
            false,
            set,
            set,
            Double.NaN,
            set ? nBottom : Double.NaN,
            set ? nTop : Double.NaN);
    }

    /* FUSED WINDOW SETTER */
    @Override
    public void setWindow (double nLeft, double nRight, double nBottom, double nTop) {
        /* as with setHorizontal() and setVertical(), we use atomics to control event triggers */
        inFusedWindowModify = true;
        /* prevent double values losing enough accuracy to go into zeroes */
        /* --- HORIZONTAL --- */
        final double hspandiff = Math.abs (nRight - nLeft);
        boolean hset = false;
        if (MIN_RANGE <= hspandiff && hspandiff <= MAX_RANGE) {
            left.set (nLeft);
            right.set (nRight);
            hset = true;
        }
        /* --- VERTICAL --- */
        final double vspandiff = Math.abs (nTop - nBottom);
        boolean vset = false;
        if (MIN_RANGE <= vspandiff && vspandiff <= MAX_RANGE) {
            bottom.set (nBottom);
            top.set (nTop);
            vset = true;
        }
        inFusedWindowModify = false;

        final boolean
            bothSet = hset && vset,
            hOnly = hset && !vset,
            vOnly = !hset && vset;

        if (bothSet)
            onWindowParamsChanged (
                false,
                false,
                true,
                true,
                true,
                true,
                Double.NaN,
                Double.NaN,
                nLeft,
                nRight,
                nBottom,
                nTop
            );
        else if (hOnly)
            onHorizontalParamsChanged (
                false,
                true,
                true,
                Double.NaN,
                nLeft,
                nRight
            );
        else if (vOnly)
            onVerticalParamsChanged (
                false,
                true,
                true,
                Double.NaN,
                nBottom,
                nTop
            );
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

    private final ChangeListener<Number> onHorizontalParamChangedListener = (obs, __old, now) -> {
        final double nowVal = now == null ? Double.NaN : now.doubleValue ();
        final boolean
            isWidth = obs == width,
            isLeft  = obs == left,
            isRight = obs == right;
        onHorizontalParamsChanged (
            isWidth,
            isLeft,
            isRight,
            isWidth ? nowVal : Double.NaN,
            isLeft ? nowVal : Double.NaN,
            isRight ? nowVal : Double.NaN);
    };

    private final ChangeListener<Number> onVerticalParamChangedListener = (obs, __old, now) -> {
        final double nowVal = now == null ? Double.NaN : now.doubleValue ();
        final boolean
            isHeight = obs == height,
            isBottom = obs == bottom,
            isTop    = obs == top;
        onVerticalParamsChanged (
            isHeight,
            isBottom,
            isTop,
            isHeight ? nowVal : Double.NaN,
            isBottom ? nowVal : Double.NaN,
            isTop ? nowVal : Double.NaN);
    };

    private void onHorizontalParamsChanged (
        boolean isWidthProperty,
        boolean isLeftProperty,
        boolean isRightProperty,
        double nWidthValue,
        double nLeftValue,
        double nRightValue)
    {
        if (inFusedWindowModify || inFusedHorizontalModify)
            return;
        synchronized (EVENT_DISPATCH_LOCK) {
            recomputeHorizontalMap ();
            if (isWidthProperty && widthBindingPoint != nodeWidthProperty)
                nodeWidthSetter.accept (nWidthValue);
            horremaplisteners.forEach (l -> l.onRemap (this,
                isWidthProperty, nWidthValue,
                false, Double.NaN,
                isLeftProperty, nLeftValue,
                isRightProperty, nRightValue,
                false, Double.NaN,
                false, Double.NaN));
        }
    }

    private void onVerticalParamsChanged (
        boolean isHeightProperty,
        boolean isBottomProperty,
        boolean isTopProperty,
        double nHeightValue,
        double nBottomValue,
        double nTopValue)
    {
        if (inFusedWindowModify || inFusedVerticalModify)
            return;
        synchronized (EVENT_DISPATCH_LOCK) {
            recomputeVerticalMap ();
            if (isHeightProperty && heightBindingPoint != nodeHeightProperty)
                nodeHeightSetter.accept (nHeightValue);
            verremaplisteners.forEach (l -> l.onRemap (this,
                false, Double.NaN,
                isHeightProperty, nHeightValue,
                false, Double.NaN,
                false, Double.NaN,
                isBottomProperty, nBottomValue,
                isTopProperty, nTopValue));
        }
    }

    /* used to trigger an update event once for changes in both the horizontal and vertical */
    private void onWindowParamsChanged (
        boolean isWidthProperty,
        boolean isHeightProperty,
        boolean isLeftProperty,
        boolean isRightProperty,
        boolean isBottomProperty,
        boolean isTopProperty,
        double nWidthValue,
        double nHeightValue,
        double nLeftValue,
        double nRightValue,
        double nBottomValue,
        double nTopValue)
    {
        if (inFusedWindowModify) /* shouldn't happen, but we have it here just in case */
            return;
        synchronized (EVENT_DISPATCH_LOCK) {
            recomputeHorizontalMap ();
            recomputeVerticalMap ();
            if (isWidthProperty && widthBindingPoint != nodeWidthProperty)
                nodeWidthSetter.accept (nWidthValue);
            if (isHeightProperty && heightBindingPoint != nodeHeightProperty)
                nodeHeightSetter.accept (nHeightValue);
            winremaplisteners.forEach (l -> l.onRemap (this,
                isWidthProperty, nWidthValue,
                isHeightProperty, nHeightValue,
                isLeftProperty, nLeftValue,
                isRightProperty, nRightValue,
                isBottomProperty, nBottomValue,
                isTopProperty, nTopValue));
        }
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
    public OrthoMixin (
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

        onWindowParamsChanged (
            true,
            true,
            false,
            false,
            false,
            false,
            getDeviceWidth (),
            getDeviceHeight (),
            Double.NaN,
            Double.NaN,
            Double.NaN,
            Double.NaN);
    }

    /* +------------+ */
    /* | TRANSFORMS | */
    /* +------------+ */
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
    public void zoomHorizontal (double vx, double mult) {
        final double
            nmLeft = getLeft () - vx,
            nmRight = getRight () - vx;
        final double
            max = Math.max (nmRight, nmLeft),
            min = Math.min (nmRight, nmLeft);
        final double
            minmult = MIN_RANGE / (max - min),
            maxmult = MAX_RANGE / (max - min);
        final double realmult = Numbers.clamp (mult < 0d ? -1d / mult : mult, minmult, maxmult);
        final double
            newLeft = nmLeft * realmult + vx,
            newRight = nmRight * realmult + vx;
        inFusedHorizontalModify = true;
        left.set (newLeft);
        right.set (newRight);
        inFusedHorizontalModify = false;

        onHorizontalParamsChanged (
            false,
            true,
            true,
            Double.NaN,
            newLeft,
            newRight);
    }

    @Override
    public void zoomVertical (double vy, double mult) {
        final double
            nmBottom = getBottom () - vy,
            nmTop = getTop () - vy;
        final double
            max = Math.max (nmTop, nmBottom),
            min = Math.min (nmTop, nmBottom);
        final double
            minmult = MIN_RANGE / (max - min),
            maxmult = MAX_RANGE / (max - min);
        final double realmult = Numbers.clamp (mult < 0d ? -1d / mult : mult, minmult, maxmult);
        final double
            newBottom = nmBottom * realmult + vy,
            newTop = nmTop * realmult + vy;
        inFusedVerticalModify = true;
        bottom.set (newBottom);
        top.set (newTop);
        inFusedVerticalModify = false;

        onVerticalParamsChanged (
            false,
            true,
            true,
            Double.NaN,
            newBottom,
            newTop);
    }

    @Override
    public void zoomWindow (double vx, double vy, double mult) {
        final double
            nmLeft   = getLeft ()   - vx,
            nmRight  = getRight ()  - vx,
            nmBottom = getBottom () - vy,
            nmTop    = getTop ()    - vy;
        final double
            hmax = Math.max (nmRight, nmLeft),
            hmin = Math.min (nmRight, nmLeft),
            vmax = Math.max (nmTop, nmBottom),
            vmin = Math.min (nmTop, nmBottom),
            hdiff = hmax - hmin,
            vdiff = vmax - vmin;
        final double
            hminmult = MIN_RANGE / (hmax - hmin),
            hmaxmult = MAX_RANGE / (hmax - hmin),
            vminmult = MIN_RANGE / (vmax - vmin),
            vmaxmult = MAX_RANGE / (vmax - vmin);
        final double formedmult = mult < 0d ? -1d / mult : mult;
        final double
            hrealmult = Numbers.clamp (formedmult, hminmult, hmaxmult),
            vrealmult = Numbers.clamp (formedmult, vminmult, vmaxmult);
        final double
            newLeft   = nmLeft   * hrealmult + vx,
            newRight  = nmRight  * hrealmult + vx,
            newBottom = nmBottom * vrealmult + vy,
            newTop    = nmTop    * vrealmult + vy;
        inFusedWindowModify = true;
        boolean
            hset = false,
            vset = false;
        if (hdiff != 0d) { /* if it is not LITERALLY zero */
            left.set (newLeft);
            right.set (newRight);
            hset = true;
        }
        if (vdiff != 0d) { /* if it is not LITERALLY zero */
            bottom.set (newBottom);
            top.set (newTop);
            vset = true;
        }

        final boolean
            bothSet = hset && vset,
            hOnly = hset && !vset,
            vOnly = !hset && vset;

        if (bothSet)
            onWindowParamsChanged (
                false,
                false,
                true,
                true,
                true,
                true,
                Double.NaN,
                Double.NaN,
                newLeft,
                newRight,
                newBottom,
                newTop
            );
        else if (hOnly)
            onHorizontalParamsChanged (
                false,
                true,
                true,
                Double.NaN,
                newLeft,
                newRight
            );
        else if (vOnly)
            onVerticalParamsChanged (
                false,
                true,
                true,
                Double.NaN,
                newBottom,
                newTop
            );
    }

    /* +---------+ */
    /* | BINDING | */
    /* +---------+ */
    @Override
    public void bindOrtho (IOrthoDevice to) {
        Objects.requireNonNull (to);
        /* just as with the fused setters, we will only fire a remap event once */
        inFusedWindowModify = true;
        width .bind (widthBindingPoint = to.deviceWidthProperty ());
        height.bind (heightBindingPoint = to.deviceHeightProperty ());
        left  .bind (to.leftProperty ());
        right .bind (to.rightProperty ());
        bottom.bind (to.bottomProperty ());
        top   .bind (to.topProperty ());
        inFusedWindowModify = false;

        /* manually trigger the total remap event after binding is complete */
        onWindowParamsChanged (
            true,
            true,
            true,
            true,
            true,
            true,
            getDeviceWidth (),
            getDeviceHeight (),
            getLeft (),
            getRight (),
            getBottom (),
            getTop ());
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

    /* +-------------+ */
    /* | IDisposable | */
    /* +-------------+ */
    @Override
    public void dispose () {
        unbindOrtho ();
        width .removeListener (onHorizontalParamChangedListener);
        left  .removeListener (onHorizontalParamChangedListener);
        right .removeListener (onHorizontalParamChangedListener);
        height.removeListener (onVerticalParamChangedListener);
        bottom.removeListener (onVerticalParamChangedListener);
        top   .removeListener (onVerticalParamChangedListener);
        width .unbind ();
        height.unbind ();
        horremaplisteners.clear ();
        verremaplisteners.clear ();
        winremaplisteners.clear ();
    }
}