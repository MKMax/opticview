package io.github.mkmax.fxe.util;

import io.github.mkmax.util.math.LinearInterpolatord;
import io.github.mkmax.util.math.LinearInterpolatordc;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class ProjectingStackPane extends StackPane implements Disposable {

    private final ChangeListener<? super Number> reqDomainUpdate = this::updateDomain;
    private final ChangeListener<? super Number> reqRangeUpdate  = this::updateRange;

    private final List<UpdateListener<? super ProjectingStackPane>> listeners = new ArrayList<> ();

    private DoubleProperty left   = new SimpleDoubleProperty (-1d);
    private DoubleProperty right  = new SimpleDoubleProperty ( 1d);
    private DoubleProperty bottom = new SimpleDoubleProperty (-1d);
    private DoubleProperty top    = new SimpleDoubleProperty ( 1d);

    private LinearInterpolatord x = new LinearInterpolatord ();
    private LinearInterpolatord y = new LinearInterpolatord ();

    public ProjectingStackPane () {
        widthProperty  ().addListener (reqDomainUpdate);
        heightProperty ().addListener (reqRangeUpdate);
        left             .addListener (reqDomainUpdate);
        right            .addListener (reqDomainUpdate);
        bottom           .addListener (reqRangeUpdate);
        top              .addListener (reqRangeUpdate);
    }

    @Override
    public void dispose () {
        widthProperty  ().removeListener (reqDomainUpdate);
        heightProperty ().removeListener (reqRangeUpdate);
        left             .removeListener (reqDomainUpdate);
        right            .removeListener (reqDomainUpdate);
        bottom           .removeListener (reqRangeUpdate);
        top              .removeListener (reqRangeUpdate);
        listeners        .clear ();
    }

    /* +---------------------+ */
    /* | INTERACTING METHODS | */
    /* +---------------------+ */

    public LinearInterpolatordc getInterpolatorX () {
        return x;
    }

    public LinearInterpolatordc getInterpolatorY () {
        return y;
    }

    /* +------------+ */
    /* | PROPERTIES | */
    /* +------------+ */

    // LEFT

    public double getLeft () {
        return left.get ();
    }

    public void setLeft (double left) {
        this.left.set (left);
    }

    public DoubleProperty leftProperty () {
        return left;
    }

    // RIGHT

    public double getRight () {
        return right.get ();
    }

    public void setRight (double right) {
        this.right.set (right);
    }

    public DoubleProperty rightProperty () {
        return right;
    }

    // BOTTOM

    public double getBottom () {
        return bottom.get ();
    }

    public void setBottom (double bottom) {
        this.bottom.set (bottom);
    }

    public DoubleProperty bottomProperty () {
        return bottom;
    }

    // TOP

    public double getTop () {
        return top.get ();
    }

    public void setTop (double top) {
        this.top.set (top);
    }

    public DoubleProperty topProperty () {
        return top;
    }

    /* +-----------+ */
    /* | LISTENERS | */
    /* +-----------+ */

    public void addUpdateListener (UpdateListener<? super ProjectingStackPane> listener) {
        if (listener != null)
            listeners.add (listener);
    }

    public void removeUpdateListener (UpdateListener<? super ProjectingStackPane> listener) {
        if (listener != null)
            listeners.remove (listener);
    }

    private void dispatchUpdateEvent () {
        listeners.forEach (i -> i.onUpdate (this));
    }

    private void updateDomain (
        ObservableValue<? extends Number> _obs,
        Number                            _old,
        Number                            _now)
    {
        x.redefine (
            left.get (), 0d,
            right.get (), getWidth ()
        );
        dispatchUpdateEvent ();
    }

    private void updateRange (
        ObservableValue<? extends Number> _obs,
        Number                            _old,
        Number                            _now)
    {
        y.redefine (
            bottom.get (), getHeight (),
            top.get (), 0d
        );
        dispatchUpdateEvent ();
    }
}
