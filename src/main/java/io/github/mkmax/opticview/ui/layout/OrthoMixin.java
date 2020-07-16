package io.github.mkmax.opticview.ui.layout;

import io.github.mkmax.opticview.util.BigMath;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/* utility implementation of an OrthoComponent */
public final class OrthoMixin implements IOrthoDevice {

    /* +----------------------+ */
    /* | PROPERTIES & MEMBERS | */
    /* +----------------------+ */
    private volatile boolean
        inFusedHorizontalSetter = false,
        inFusedVerticalSetter   = false,
        inFusedWindowSetter     = false;

    private final ObservableValue<? extends Number>
        nodeWidthProp,
        nodeHeightProp;
    private final Consumer<? super Number>
        nodeWidthSetter,
        nodeHeightSetter;

    private final List<IRemapListener>
        horRemapListeners = new ArrayList<> (),
        verRemapListeners = new ArrayList<> (),
        winRemapListeners = new ArrayList<> ();

    private final SimpleObjectProperty<BigDecimal>
        width  = new SimpleObjectProperty<> (),
        height = new SimpleObjectProperty<> (),
        left   = new SimpleObjectProperty<> (),
        right  = new SimpleObjectProperty<> (),
        bottom = new SimpleObjectProperty<> (),
        top    = new SimpleObjectProperty<> ();

    private static final class BigDecimalTranslationBinding {
        private final ObjectProperty<? super BigDecimal> target;
        private ObservableValue<? extends Number> bindingPoint;

        private final ChangeListener<? super Number> onBindingPointChanged = (__obs, __old, now) ->
            BigDecimalTranslationBinding.this.target.set (BigMath.toBigDecimal (now, BigDecimal.ZERO));

        private BigDecimalTranslationBinding (
            ObjectProperty<? super BigDecimal> pTarget)
        {
            target = Objects.requireNonNull (pTarget);
        }

        public ObservableValue<? extends Number> getBindingPoint () {
            return bindingPoint;
        }

        public void bind (ObservableValue<? extends Number> point) {
            if (bindingPoint != null)
                bindingPoint.removeListener (onBindingPointChanged);
            if (point != null) {
                bindingPoint = point;
                bindingPoint.addListener (onBindingPointChanged);
                target.set (BigMath.toBigDecimal (bindingPoint.getValue (), BigDecimal.ZERO));
            }
        }

        public void unbind () {
            bind (null);
        }
    }

    private final BigDecimalTranslationBinding widthTranslationBinding =
        new BigDecimalTranslationBinding (width);

    private final BigDecimalTranslationBinding heightTranslationBinding =
        new BigDecimalTranslationBinding (height);

    private final BigDecimalTranslationBinding leftTranslationBinding =
        new BigDecimalTranslationBinding (left);

    private final BigDecimalTranslationBinding rightTranslationBinding =
        new BigDecimalTranslationBinding (right);

    private final BigDecimalTranslationBinding bottomTranslationBinding =
        new BigDecimalTranslationBinding (bottom);

    private final BigDecimalTranslationBinding topTranslationBinding =
        new BigDecimalTranslationBinding (top);

    public static BigDecimal getPropertyValueElse (ObjectProperty<BigDecimal> prop, BigDecimal def) {
        return prop.getValue () == null ? def : prop.getValue ();
    }

    private BigDecimal
        Mx = BigDecimal.ONE, iMx = BigDecimal.ONE, Cx = BigDecimal.ZERO,
        My = BigDecimal.ONE, iMy = BigDecimal.ONE, Cy = BigDecimal.ZERO;

    /* +-------------------------------+ */
    /* | PROPERTY LISTENERS & HANDLERS | */
    /* +-------------------------------+ */
    private final ChangeListener<Number> horizontalChangeListener = (obs, __old, now) ->
        onHorizontalParametersChanged (obs == width, now);

    private final ChangeListener<Number> verticalChangeListener = (obs, __old, now) ->
        onVerticalParametersChanged (obs == height, now);

    private void onHorizontalParametersChanged (
        boolean isWidthProperty,
        Number  newValue)
    {
        if (inFusedWindowSetter || inFusedHorizontalSetter)
            return;
        if (isWidthProperty && widthTranslationBinding.getBindingPoint () != nodeWidthProp) {
            Objects.requireNonNull (newValue);
            nodeWidthSetter.accept (newValue);
        }
        computeHorizontalMap ();
        horRemapListeners.forEach (l -> l.onRemap (this));
    }

    private void onVerticalParametersChanged (
        boolean isHeightProperty,
        Number  newValue)
    {
        if (inFusedWindowSetter || inFusedVerticalSetter)
            return;
        if (isHeightProperty && heightTranslationBinding.getBindingPoint () != nodeHeightProp) {
            Objects.requireNonNull (newValue);
            nodeHeightSetter.accept (newValue);
        }
        computeVerticalMap ();
        verRemapListeners.forEach (l -> l.onRemap (this));
    }

    private void onWindowParametersChanged (
        boolean isWidthProperty,
        boolean isHeightProperty,
        Number  newWidthValue,
        Number  newHeightValue)
    {
        if (inFusedWindowSetter)
            return;
        if (isWidthProperty && widthTranslationBinding.getBindingPoint () != nodeWidthProp) {
            Objects.requireNonNull (newWidthValue);
            nodeWidthSetter.accept (newWidthValue);
        }
        if (isHeightProperty && heightTranslationBinding.getBindingPoint () != nodeHeightProp) {
            Objects.requireNonNull (newHeightValue);
            nodeHeightSetter.accept (newHeightValue);
        }
        computeHorizontalMap ();
        computeVerticalMap ();
        winRemapListeners.forEach (l -> l.onRemap (this));
    }

    private void computeHorizontalMap () {
        final BigDecimal
            nmWidth = getPropertyValueElse (width, BigDecimal.ZERO),
            nmLeft  = getPropertyValueElse (left,  BigDecimal.ZERO),
            nmRight = getPropertyValueElse (right, BigDecimal.ZERO);
        if (nmRight.equals (nmLeft)) {
            /* default to the identity map */
            Mx  = BigDecimal.ONE;
            iMx = BigDecimal.ONE;
            Cx  = BigDecimal.ZERO;
        }
        else {
            // Mx = width / (right - left)
            Mx = nmWidth.divide (nmRight.subtract (nmLeft), RoundingMode.UNNECESSARY);
            // iMx = 1 / Mx
            iMx = BigDecimal.ONE.divide (Mx, RoundingMode.UNNECESSARY);
            // Cx = -Mx * left
            Cx = Mx.negate ().multiply (nmLeft);
        }
    }

    private void computeVerticalMap () {
        final BigDecimal
            nmHeight = getPropertyValueElse (height, BigDecimal.ZERO),
            nmBottom = getPropertyValueElse (bottom, BigDecimal.ZERO),
            nmTop    = getPropertyValueElse (top,    BigDecimal.ZERO);
        if (nmBottom.equals (nmTop)) {
            /* default to the identity map */
            My  = BigDecimal.ONE;
            iMy = BigDecimal.ONE;
            Cy  = BigDecimal.ZERO;
        }
        else {
            /* invert the Y axis for graphing applications */
            // My = height / (bottom - top)
            My = nmHeight.divide (nmBottom.subtract (nmTop), RoundingMode.UNNECESSARY);
            // iMy = 1 / My
            iMy = BigDecimal.ONE.divide (My, RoundingMode.UNNECESSARY);
            // Cy = -My * top
            Cy = Mx.negate ().multiply (nmTop);
        }
    }

    /* +----------------+ */
    /* | INITIALIZATION | */
    /* +----------------+ */
    public OrthoMixin (
        ObservableValue<? extends Number> pNodeWidthProperty,
        ObservableValue<? extends Number> pNodeHeightProperty,
        Consumer<? super Number> pNodeWidthSetter,
        Consumer<? super Number> pNodeHeightSetter)
    {
        /* install node-related settings first */
        nodeWidthProp    = Objects.requireNonNull (pNodeWidthProperty);
        nodeHeightProp   = Objects.requireNonNull (pNodeHeightProperty);
        nodeWidthSetter  = Objects.requireNonNull (pNodeWidthSetter);
        nodeHeightSetter = Objects.requireNonNull (pNodeHeightSetter);

        /* install listeners */
        width.addListener (horizontalChangeListener);
        left .addListener (horizontalChangeListener);
        right.addListener (horizontalChangeListener);

        height.addListener (verticalChangeListener);
        bottom.addListener (verticalChangeListener);
        top   .addListener (verticalChangeListener);

        /* bind width/height and compute the current mapping */
        inFusedWindowSetter = true; /* disable automatic re-computation */
        widthTranslationBinding.bind (nodeWidthProp);
        heightTranslationBinding.bind (nodeHeightProp);
        inFusedWindowSetter = false; /* enable automatic re-computation again */
        onWindowParametersChanged (true, true, getDeviceWidth (), getDeviceHeight ());
    }

    /* +------------+ */
    /* | DIMENSIONS | */
    /* +------------+ */
    @Override
    public ReadOnlyObjectProperty<? extends Number> deviceWidthProperty () {
        return width;
    }

    @Override
    public ReadOnlyObjectProperty<? extends Number> deviceHeightProperty () {
        return height;
    }

    @Override
    public Number getDeviceWidth () {
        return width.get ();
    }

    @Override
    public void setDeviceWidth (Number nWidth) {
        if (widthTranslationBinding.getBindingPoint () != nodeWidthProp)
            throw new RuntimeException ("Cannot set width of a bound IOrthoDevice");
        Objects.requireNonNull (nWidth, "A non-null width number reference must be specified");
        nodeWidthSetter.accept (nWidth);
    }

    @Override
    public Number getDeviceHeight () {
        return height.get ();
    }

    @Override
    public void setDeviceHeight (Number nHeight) {
        if (heightTranslationBinding.getBindingPoint () != nodeHeightProp)
            throw new RuntimeException ("Cannot set height of a bound IOrthoDevice");
        Objects.requireNonNull (nHeight, "A non-null height number reference must be specified");
        nodeHeightSetter.accept (nHeight);
    }

    /* +--------+ */
    /* | WINDOW | */
    /* +--------+ */
    @Override
    public ReadOnlyObjectProperty<? extends Number> leftProperty () {
        return left;
    }

    @Override
    public Number getLeft () {
        return getPropertyValueElse (left, BigDecimal.ZERO);
    }

    @Override
    public void setLeft (Number nLeft) {
        left.set (BigMath.toBigDecimal (nLeft, null));
    }

    @Override
    public ReadOnlyObjectProperty<? extends Number> rightProperty () {
        return right;
    }

    @Override
    public Number getRight () {
        return getPropertyValueElse (right, BigDecimal.ZERO);
    }

    @Override
    public void setRight (Number nRight) {
        right.set (BigMath.toBigDecimal (nRight, null));
    }

    @Override
    public ReadOnlyObjectProperty<? extends Number> bottomProperty () {
        return bottom;
    }

    @Override
    public Number getBottom () {
        return getPropertyValueElse (bottom, BigDecimal.ZERO);
    }

    @Override
    public void setBottom (Number nBottom) {
        bottom.set (BigMath.toBigDecimal (nBottom, null));
    }

    @Override
    public ReadOnlyObjectProperty<? extends Number> topProperty () {
        return top;
    }

    @Override
    public Number getTop () {
        return getPropertyValueElse (top, BigDecimal.ZERO);
    }

    @Override
    public void setTop (Number nTop) {
        top.set (BigMath.toBigDecimal (nTop, null));
    }

    /* +----------------------+ */
    /* | WINDOW FUSED SETTERS | */
    /* +----------------------+ */
    @Override
    public void setHorizontal (Number nLeft, Number nRight) {
        inFusedHorizontalSetter = true;
        setLeft (nLeft);
        setRight (nRight);
        inFusedHorizontalSetter = false;
        onHorizontalParametersChanged (false, null);
    }

    @Override
    public void setVertical (Number nBottom, Number nTop) {
        inFusedVerticalSetter = true;
        setBottom (nBottom);
        setTop (nTop);
        inFusedVerticalSetter = false;
        onVerticalParametersChanged (false, null);
    }

    @Override
    public void setWindow (Number nLeft, Number nRight, Number nBottom, Number nTop) {
        inFusedWindowSetter = true;
        setLeft (nLeft);
        setRight (nRight);
        setBottom (nBottom);
        setTop (nTop);
        inFusedWindowSetter = false;
        onWindowParametersChanged (false, false, null, null);
    }

    /* +-----------------------+ */
    /* | OTHER MAPPING QUERIES | */
    /* +-----------------------+ */

    /* Math.abs(right - left) and Math.abs(top - bottom) */
    @Override
    public Number getHorizontalSpan () {
        final BigDecimal
            nmRight = getPropertyValueElse (right, BigDecimal.ZERO),
            nmLeft  = getPropertyValueElse (left,  BigDecimal.ZERO);
        // abs(right - left)
        return nmRight.subtract (nmLeft).abs ();
    }

    @Override
    public Number getVerticalSpan () {
        final BigDecimal
            nmTop    = getPropertyValueElse (top,    BigDecimal.ZERO),
            nmBottom = getPropertyValueElse (bottom, BigDecimal.ZERO);
        // abs(top - bottom)
        return nmTop.subtract (nmBottom).abs ();
    }

    private static final BigDecimal HALF = new BigDecimal ("0.5");

    /* getHorizontalSpan() / 2 and getVerticalSpan() / 2 */
    @Override
    public Number getHorizontalZoom () {
        final BigDecimal
            nmRight = getPropertyValueElse (right, BigDecimal.ZERO),
            nmLeft  = getPropertyValueElse (left,  BigDecimal.ZERO);
        // 0.5 * abs(right - left)
        return HALF.multiply (nmRight.subtract (nmLeft).abs ());
    }

    @Override
    public Number getVerticalZoom () {
        final BigDecimal
            nmBottom = getPropertyValueElse (bottom, BigDecimal.ZERO),
            nmTop    = getPropertyValueElse (top,    BigDecimal.ZERO);
        // 0.5 * abs(top - bottom)
        return HALF.multiply (nmTop.subtract (nmBottom));
    }

    /* +----------+ */
    /* | LISTENER | */
    /* +----------+ */

    /* HORIZONTAL */
    @Override
    public void registerHorizontalRemapListener (IRemapListener lis) {
        if (lis != null) horRemapListeners.add (lis);
    }

    @Override
    public void removeHorizontalRemapListener (IRemapListener lis) {
        horRemapListeners.remove (lis);
    }

    /* VERTICAL */
    @Override
    public void registerVerticalRemapListener (IRemapListener lis) {
        if (lis != null) verRemapListeners.add (lis);
    }

    @Override
    public void removeVerticalRemapListener (IRemapListener lis) {
        verRemapListeners.remove (lis);
    }

    /* WINDOW */
    @Override
    public void registerWindowRemapListener (IRemapListener lis) {
        if (lis != null) winRemapListeners.add (lis);
    }

    @Override
    public void removeWindowRemapListener (IRemapListener lis) {
        winRemapListeners.remove (lis);
    }

    /* +---------+ */
    /* | MAPPING | */
    /* +---------+ */
    @Override
    public Number mapToDeviceX (Number virtualX) {
        // Mx * virtualX + Cx
        return Mx.multiply (BigMath.toBigDecimal (virtualX, BigDecimal.ZERO)).add (Cx);
    }

    @Override
    public Number mapToDeviceY (Number virtualY) {
        // My * virtualY + Cy
        return My.multiply (BigMath.toBigDecimal (virtualY, BigDecimal.ZERO)).add (Cy);
    }

    @Override
    public Number mapToVirtualX (Number devX) {
        // (devX - Cx) * iMx
        return BigMath.toBigDecimal (devX, BigDecimal.ZERO).subtract (Cx).multiply (iMx);
    }

    @Override
    public Number mapToVirtualY (Number devY) {
        // (devY - Cy) * iMy
        return BigMath.toBigDecimal (devY, BigDecimal.ZERO).subtract (Cy).multiply (iMy);
    }

    /* +---------+ */
    /* | BINDING | */
    /* +---------+ */
    @Override
    public void bindOrtho (IOrthoDevice dev) {
        inFusedWindowSetter = true;
        widthTranslationBinding .bind (dev.deviceWidthProperty ());
        heightTranslationBinding.bind (dev.deviceHeightProperty ());
        leftTranslationBinding  .bind (dev.leftProperty ());
        rightTranslationBinding .bind (dev.rightProperty ());
        bottomTranslationBinding.bind (dev.bottomProperty ());
        topTranslationBinding   .bind (dev.topProperty ());
        inFusedWindowSetter = false;
        onWindowParametersChanged (true, true, getDeviceWidth (), getDeviceHeight ());
    }

    @Override
    public void unbindOrtho () {
        inFusedWindowSetter = true;
        widthTranslationBinding .bind (nodeWidthProp);
        heightTranslationBinding.bind (nodeHeightProp);
        leftTranslationBinding  .unbind ();
        rightTranslationBinding .unbind ();
        bottomTranslationBinding.unbind ();
        topTranslationBinding   .unbind ();
        inFusedWindowSetter = false;
        /* no need to dispatch update event since unbinding doesn't change the
         * value and the node properties have been updated with the device
         * properties during the time they were bound elsewhere.
         */
    }

    /* +------------+ */
    /* | DISPOSABLE | */
    /* +------------+ */
    @Override
    public void dispose () {
        /* totally unbind everything */
        widthTranslationBinding .unbind ();
        heightTranslationBinding.unbind ();
        leftTranslationBinding  .unbind ();
        rightTranslationBinding .unbind ();
        bottomTranslationBinding.unbind ();
        topTranslationBinding   .unbind ();

        /* remove all listeners */
        width.removeListener (horizontalChangeListener);
        left .removeListener (horizontalChangeListener);
        right.removeListener (horizontalChangeListener);

        height.removeListener (verticalChangeListener);
        bottom.removeListener (verticalChangeListener);
        top   .removeListener (verticalChangeListener);

        /* clear the listener lists */
        horRemapListeners.clear ();
        verRemapListeners.clear ();
        winRemapListeners.clear ();
    }
}
