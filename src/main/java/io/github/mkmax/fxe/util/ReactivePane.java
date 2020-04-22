package io.github.mkmax.fxe.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class ReactivePane extends Pane implements Disposable {

    private final ChangeListener<? super Number> WIDTH   = this::width;
    private final ChangeListener<? super Number> HEIGHT  = this::height;

    private final ChangeListener<? super Number> PWIDTH  = this::parentWidth;
    private final ChangeListener<? super Number> PHEIGHT = this::parentHeight;
    private final ChangeListener<? super Parent> PARENT  = this::parent;

    public ReactivePane () {
        widthProperty  ().addListener (WIDTH);
        heightProperty ().addListener (HEIGHT);

        registerParentListeners (getParent ());
        parentProperty ().addListener (PARENT);
    }

    @Override
    public void dispose () {
        widthProperty  ().removeListener (WIDTH);
        heightProperty ().removeListener (HEIGHT);

        removeParentListeners (getParent ());
        parentProperty ().removeListener (PARENT);
    }

    /* +--------------------------+ */
    /* | LISTENER IMPLEMENTATIONS | */
    /* +--------------------------+ */

    /* WIDTH */

    private void width (
        ObservableValue<? extends Number> __obs,
        Number                              old,
        Number                              now)
    {
        onWidthChanged (old.doubleValue (), now.doubleValue ());
    }

    /* HEIGHT */

    private void height (
        ObservableValue<? extends Number> __obs,
        Number                              old,
        Number                              now)
    {
        onHeightChanged (old.doubleValue (), now.doubleValue ());
    }

    /* PARENT WIDTH */

    private void parentWidth (
        ObservableValue<? extends Number> __obs,
        Number                              old,
        Number                              now)
    {
        onParentWidthChanged (old.doubleValue (), now.doubleValue ());
    }

    /* PARENT HEIGHT */

    private void parentHeight (
        ObservableValue<? extends Number> __obs,
        Number                              old,
        Number                              now)
    {
        onParentHeightChanged (old.doubleValue (), now.doubleValue ());
    }

    /* PARENT */

    private void parent (
        ObservableValue<? extends Parent> __obs,
        Parent                              old,
        Parent                              now)
    {
        removeParentListeners (old);
        registerParentListeners (now);

        onParentChanged (old, now);
        onParentWidthChanged (
            old instanceof Region ? ((Region) old).getWidth () : 0d,
            now instanceof Region ? ((Region) now).getWidth () : 0d
        );
        onParentHeightChanged (
            old instanceof Region ? ((Region) old).getHeight () : 0d,
            now instanceof Region ? ((Region) now).getHeight () : 0d
        );
    }

    /* +-----------+ */
    /* | UTILITIES | */
    /* +-----------+ */

    private void registerParentListeners (final Parent parent) {
        if (parent == null)
            return;
        if (parent instanceof Region) {
            final Region region = (Region) parent;
            region.widthProperty  ().addListener (PWIDTH);
            region.heightProperty ().addListener (PHEIGHT);
        }
    }

    private void removeParentListeners (final Parent parent) {
        if (parent == null)
            return;
        if (parent instanceof Region) {
            final Region region = (Region) parent;
            region.widthProperty  ().removeListener (PWIDTH);
            region.heightProperty ().removeListener (PHEIGHT);
        }
    }

    /* +---------------------------------+ */
    /* | ABSTRACTED + OPTIONAL CALLBACKS | */
    /* +---------------------------------+ */

    protected void onWidthChanged (double old, double now) { }

    protected void onHeightChanged (double old, double now) { }

    protected void onParentWidthChanged (double old, double now) { }

    protected void onParentHeightChanged (double old, double now) { }

    protected void onParentChanged (Parent old, Parent now) { }

    /* +---------+ */
    /* | UTILITY | */
    /* +---------+ */

    protected double getParentWidth () {
        final Parent p = getParent ();
        if (p instanceof Region)
            return ((Region) p).getWidth ();
        else
            return 0d;
    }

    protected double getParentHeight () {
        final Parent p = getParent ();
        if (p instanceof Region)
            return ((Region) p).getHeight ();
        else
            return 0d;
    }
}
