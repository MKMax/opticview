package io.github.mkmax.exfx.layout;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.WeakChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.Objects;

public abstract class OrthoRegion extends Region {

    private double
        Mx, Cx,
        My, Cy;

    private ReadOnlyDoubleProperty widthBindingPoint;
    private ReadOnlyDoubleProperty heightBindingPoint;

    private final DoubleProperty
        width  = new SimpleDoubleProperty (),
        height = new SimpleDoubleProperty (),
        left   = new SimpleDoubleProperty (-1d),
        right  = new SimpleDoubleProperty (+1d),
        bottom = new SimpleDoubleProperty (-1d),
        top    = new SimpleDoubleProperty (+1d);

    /* compact getters */
    public double getLeft   () { return left  .get (); }
    public double getRight  () { return right .get (); }
    public double getBottom () { return bottom.get (); }
    public double getTop    () { return top   .get (); }

    /* compact setters */
    public void setLeft   (double nleft  ) { left  .set (nleft  ); }
    public void setRight  (double nright ) { right .set (nright ); }
    public void setBottom (double nbottom) { bottom.set (nbottom); }
    public void setTop    (double ntop   ) { top   .set (ntop   ); }

    /* fused setters */
    public void setHorizontal (double nleft, double nright) {
        setLeft (nleft);
        setRight (nright);
    }
    public void setVertical (double nbottom, double ntop) {
        setBottom (nbottom);
        setTop (ntop);
    }





    private final WeakChangeListener<Number> horParameterListener = new WeakChangeListener<> ((obs, __old, __now) -> {
        final double
            nwidth = width.get (),
            nleft  = left.get (),
            nright = right.get ();
        if (obs == width && widthBindingPoint != widthProperty ())
            setWidth (nwidth);
        Mx = nwidth / (nright - nleft);
        Cx = -Mx * nleft;
        onHorizontalMapChanged ();
    });

    private final WeakChangeListener<Number> verParameterListener = new WeakChangeListener<> ((obs, __old, __now) -> {
        final double
            nheight = height.get (),
            nbottom = bottom.get (),
            ntop    = top.get ();
        if (obs == height && heightBindingPoint != heightProperty ())
            setHeight (nheight);
        My = nheight / (nbottom - ntop);
        Cy = -My * ntop;
        onVerticalMapChanged ();
    });

    {
        width.bind (widthBindingPoint = widthProperty ());
        height.bind (heightBindingPoint = heightProperty ());

        width.addListener (horParameterListener);
        left .addListener (horParameterListener);
        right.addListener (horParameterListener);

        height.addListener (verParameterListener);
        bottom.addListener (verParameterListener);
        top   .addListener (verParameterListener);
    }





    public void bindFrustum (OrthoRegion other) {
        Objects.requireNonNull (other);
        width .bind (widthBindingPoint = other.width);
        height.bind (heightBindingPoint = other.height);
        left  .bind (other.left);
        right .bind (other.right);
        bottom.bind (other.bottom);
        top   .bind (other.top);
    }

    public void unbindFrustum () {
        width .bind (widthBindingPoint = widthProperty ());
        height.bind (heightBindingPoint = heightProperty ());
        left  .unbind ();
        right .unbind ();
        bottom.unbind ();
        top   .unbind ();
    }

    public double mapx (double p) {
        return Mx * p + Cx;
    }

    public double mapy (double p) {
        return My * p + Cy;
    }

    public double unmapx (double q) {
        return (q - Cx) / Mx;
    }

    public double unmapy (double q) {
        return (q - Cy) / My;
    }

    protected abstract void onHorizontalMapChanged ();
    protected abstract void onVerticalMapChanged ();





    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData () {
        return Region.getClassCssMetaData ();
    }
}