package io.github.mkmax.opticview.ui.graph;

import io.github.mkmax.opticview.util.Numbers;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.io.InputStream;
import java.util.Objects;

/* a static collection of default tool implementations */
/* @TODO(max): convert tools to be compatible with BigDecimal (and use it for that matter) */
public final class GraphTools {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      MOTION TOOL                                          //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static final class Motion extends GraphStack.Tool {

        /* +------------------+ */
        /* | META INFORMATION | */
        /* +------------------+ */

        /* resources */
        private static final Image ICON_IMG;
        private static final Image CURSOR_IMG;

        /* load resources */
        static {
            final ClassLoader rcl = Thread
                .currentThread ()
                .getContextClassLoader ();
            final InputStream iconIn = rcl.getResourceAsStream ("assets/tools/motion/icon64.png");
            final InputStream cursorIn = rcl.getResourceAsStream ("assets/tools/motion/cursor64.png");

            Objects.requireNonNull (iconIn, "Failed to open Motion Tool icon image");
            Objects.requireNonNull (cursorIn, "Failed to open Motion Tool cursor image");

            ICON_IMG = new Image (iconIn);
            CURSOR_IMG = new Image (cursorIn);
        }

        @Override
        public String getName () {
            return "Motion";
        }

        @Override
        public Image getIcon () {
            return ICON_IMG;
        }

        /* +--------------------------+ */
        /* | INITIALIZATION & MEMBERS | */
        /* +--------------------------+ */
        private volatile boolean
            enabled = false,
            pressed = false;

        public Motion (GraphStack pstack) {
            setGraphStack (pstack);
        }

        /* +---------------------+ */
        /* | HANDLER & LISTENERS | */
        /* +---------------------+ */
        private final Cursor CURSOR = new ImageCursor (CURSOR_IMG);
        private final Object PRESS_LOCK = new Object ();
        private volatile double
            mouseX = 0d,
            mouseY = 0d;

        private final EventHandler<MouseEvent> onMousePressed = (e) -> {
            synchronized (PRESS_LOCK) {
                pressed = e.getButton () == MouseButton.PRIMARY || pressed;
                mouseX = e.getX ();
                mouseY = e.getY ();
            }
        };

        private final EventHandler<MouseEvent> onMouseReleased = (e) -> {
            synchronized (PRESS_LOCK) {
                pressed = e.getButton () != MouseButton.PRIMARY && pressed;
            }
        };

        private final EventHandler<MouseEvent> onMouseMoved = (e) -> {
            mouseX = e.getX ();
            mouseY = e.getY ();
        };

        private final EventHandler<MouseEvent> onMouseDragged = (e) -> {
            if (!pressed || !enabled)
                return;
            synchronized (PRESS_LOCK) {
                final GraphStack gs = getGraphStack ();
                final double
                    mouseNowX = e.getX (),
                    mouseNowY = e.getY ();
                final double
                    deltaX = (mouseNowX - mouseX),
                    deltaY = (mouseNowY - mouseY);
                final double
                    scaleX = (gs.getRight ().doubleValue () - gs.getLeft ().doubleValue ()) / gs.getWidth (),
                    scaleY = (gs.getBottom ().doubleValue () - gs.getTop ().doubleValue ()) / gs.getHeight ();
                final double
                    deltaVirtualX = deltaX * scaleX,
                    deltaVirtualY = deltaY * scaleY;
                final double
                    nLeft   = gs.getLeft ().doubleValue ()   - deltaVirtualX,
                    nRight  = gs.getRight () .doubleValue () - deltaVirtualX,
                    nBottom = gs.getBottom ().doubleValue () - deltaVirtualY,
                    nTop    = gs.getTop ().doubleValue ()    - deltaVirtualY;
                gs.setWindow (nLeft, nRight, nBottom, nTop);
                mouseX = mouseNowX;
                mouseY = mouseNowY;
            }
        };

        private static final double ZOOM_IN_MULT = 0.80d;
        private static final double ZOOM_OUT_MULT = 1.25d;

        private final EventHandler<ScrollEvent> onScroll = (e) -> {
            final double deltaY = e.getDeltaY ();
            if (Numbers.areEqual (deltaY, 0d))
                return;
            final GraphStack gs = getGraphStack ();
            final GraphStack.ToolRegion tr = gs.getToolRegion ();
            final double
                virtualX = tr.mapToVirtualX (mouseX).doubleValue (),
                virtualY = tr.mapToVirtualY (mouseY).doubleValue ();
            final double
                deltaLeft   = tr.getLeft ().doubleValue ()   - virtualX,
                deltaRight  = tr.getRight ().doubleValue ()  - virtualX,
                deltaBottom = tr.getBottom ().doubleValue () - virtualY,
                deltaTop    = tr.getTop ().doubleValue ()    - virtualY;
            final double multiplier = deltaY < 0d ? ZOOM_OUT_MULT : ZOOM_IN_MULT;
            final double
                nDeltaLeft   = deltaLeft   * multiplier,
                nDeltaRight  = deltaRight  * multiplier,
                nDeltaBottom = deltaBottom * multiplier,
                nDeltaTop    = deltaTop    * multiplier;
            gs.setWindow (
                nDeltaLeft   + virtualX,
                nDeltaRight  + virtualX,
                nDeltaBottom + virtualY,
                nDeltaTop    + virtualY
            );
        };

        /* CHANGE HANDLERS */
        @Override
        protected void onGraphStackChanged (GraphStack old, GraphStack now) {
            if (old != null) {
                final GraphStack.ToolRegion otr = old.getToolRegion ();
                otr.setOnMousePressed (null);
                otr.setOnMouseReleased (null);
                otr.setOnMouseMoved (null);
                otr.setOnMouseDragged (null);
                otr.setOnScroll (null);
            }
            final GraphStack.ToolRegion ntr = now.getToolRegion ();
            ntr.setOnMousePressed (onMousePressed);
            ntr.setOnMouseReleased (onMouseReleased);
            ntr.setOnMouseMoved (onMouseMoved);
            ntr.setOnMouseDragged (onMouseDragged);
            ntr.setOnScroll (onScroll); /* mouse zooming, @TODO(max): properly implement this */
        }

        /* INTERFACING HANDLERS */
        @Override
        protected void onEnabled () {
            enabled = true;
            getGraphStack ().getToolRegion ().setCursor (CURSOR);
        }

        @Override
        protected void onDisabled () {
            enabled = false;
            getGraphStack ().getToolRegion ().setCursor (Cursor.DEFAULT);
        }

        /* +------------+ */
        /* | DISPOSABLE | */
        /* +------------+ */
        @Override
        public void dispose () {
            final GraphStack gs = getGraphStack ();
            if (gs == null)
                return;
            final GraphStack.ToolRegion tr = gs.getToolRegion ();
            tr.setOnMousePressed (null);
            tr.setOnMouseReleased (null);
            tr.setOnMouseMoved (null);
            tr.setOnMouseDragged (null);
            tr.setOnScroll (null);
        }
    }
}
