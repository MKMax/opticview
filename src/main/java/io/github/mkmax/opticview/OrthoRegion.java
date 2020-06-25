package io.github.mkmax.opticview;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Region;
import javafx.beans.property.*;
import javafx.css.*;
import java.util.*;

public abstract class OrthoRegion extends Region implements Disposable {

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

    /* property getters */
    public ReadOnlyDoubleProperty leftProperty   () { return left;   }
    public ReadOnlyDoubleProperty rightProperty  () { return right;  }
    public ReadOnlyDoubleProperty bottomProperty () { return bottom; }
    public ReadOnlyDoubleProperty topProperty    () { return top;    }

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





    private final ChangeListener<Number> horParameterListener = (obs, __old, __now) -> {
        final double
            nwidth = width.get (),
            nleft  = left.get (),
            nright = right.get ();
        if (obs == width && widthBindingPoint != widthProperty ())
            setWidth (nwidth);
        Mx = nwidth / (nright - nleft);
        Cx = -Mx * nleft;
    };

    private final ChangeListener<Number> verParameterListener = (obs, __old, __now) -> {
        final double
            nheight = height.get (),
            nbottom = bottom.get (),
            ntop    = top.get ();
        if (obs == height && heightBindingPoint != heightProperty ())
            setHeight (nheight);
        My = nheight / (nbottom - ntop);
        Cy = -My * ntop;
    };

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

    @Override
    public void dispose () {
        width.removeListener (horParameterListener);
        left .removeListener (horParameterListener);
        right.removeListener (horParameterListener);

        height.removeListener (verParameterListener);
        bottom.removeListener (verParameterListener);
        top   .removeListener (verParameterListener);
    }





    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData () {
        return Region.getClassCssMetaData ();
    }
}