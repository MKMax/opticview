package io.github.mkmax.opticview.ui.graph;

import io.github.mkmax.opticview.ui.layout.OrthoRegion;
import io.github.mkmax.opticview.ui.layout.OrthoStackPane;
import io.github.mkmax.opticview.util.IDisposable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GraphStack extends OrthoStackPane implements IDisposable {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                        DEVICE                                             //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static abstract class Device extends OrthoRegion implements IDisposable {
        /* +--------------------------+ */
        /* | INITIALIZATION & MEMBERS | */
        /* +--------------------------+ */
        private GraphData graphdata;

        /* we don't mandate a constructor to validate graphdata because
         * if onGraphDataChanged is fired, and an object is used by an
         * the extending class which has not been initialized yet (because
         * this class is initializing), it will throw an NPE.
         */

        /* +-------------------+ */
        /* | GETTERS & SETTERS | */
        /* +-------------------+ */

        /* GRAPH DATA */
        public GraphData getGraphData () {
            return graphdata;
        }

        protected void setGraphData (GraphData ndata) {
            GraphData old = graphdata;
            graphdata = Objects.requireNonNull (ndata, "A non-null GraphData object must be specified");
            onGraphDataChanged (old, graphdata);
        }

        /* +----------------------+ */
        /* | HANDLER DECLARATIONS | */
        /* +----------------------+ */
        protected abstract void onGraphDataChanged (GraphData old, GraphData now);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                        TOOL                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static abstract class Tool implements IDisposable {
        /* +--------------------------+ */
        /* | INITIALIZATION & MEMBERS | */
        /* +--------------------------+ */
        private GraphStack graphstack;

        /* we don't mandate a constructor to validate graphstack because
         * if onGraphDataChanged is fired, and an object is used by an
         * the extending class which has not been initialized yet (because
         * this class is initializing), it will throw an NPE.
         */

        /* +-------------------+ */
        /* | GETTERS & SETTERS | */
        /* +-------------------+ */

        /* GRAPH STACK */
        public GraphStack getGraphStack () {
            return graphstack;
        }

        protected void setGraphStack (GraphStack nstack) {
            GraphStack old = graphstack;
            graphstack = Objects.requireNonNull(nstack, "A non-null GraphStack object must be specified");
            onGraphStackChanged (old, graphstack);
        }

        /* +----------------------+ */
        /* | HANDLER DECLARATIONS | */
        /* +----------------------+ */

        /* CHANGE HANDLERS */
        protected abstract void onGraphStackChanged (GraphStack old, GraphStack now);

        /* INTERFACING HANDLERS */
        protected abstract void onEnabled (); /* invoked when user selects the tool */
        protected abstract void onDisabled (); /* invoked when the user deselects this tool or selects another */

        /* +------------------+ */
        /* | META INFORMATION | */
        /* +------------------+ */
        public abstract String getName ();
        public abstract Image getIcon ();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                     TOOL REGION                                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static final class ToolRegion extends OrthoRegion implements IDisposable {
        /* +------------+ */
        /* | PROPERTIES | */
        /* +------------+ */
        private final SimpleObjectProperty<Tool> activetool = new SimpleObjectProperty<> ();
        public ObjectProperty<Tool> activeToolProperty ()
            { return activetool; }
        public Tool getActiveTool ()
            { return activetool.get (); }
        public void setActiveTool (Tool ntool)
            { activetool.set (ntool); }

        /* +-----------+ */
        /* | LISTENERS | */
        /* +-----------+ */
        private final ChangeListener<Tool> activetoollistener = (__obs, old, now) -> {
            if (old != null) old.onDisabled ();
            if (now != null) now.onEnabled ();
        };

        { activetool.addListener (activetoollistener); }

        /* +----------------+ */
        /* | INITIALIZATION | */
        /* +----------------+ */
        public ToolRegion (Tool activetool) {
            setActiveTool (activetool);
        }

        public ToolRegion () {
            /* do nothing; already initialized */
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                   CUSTOM LISTENERS                                        //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    @FunctionalInterface
    public interface ToolAdditionListener {
        void onAdd (GraphStack gs, Tool tool);
    }

    @FunctionalInterface
    public interface ToolRemovalListener {
        void onRemove (GraphStack gs, Tool tool);
    }

    @FunctionalInterface
    public interface DeviceAdditionListener {
        void onAdd (GraphStack gs, Device dev);
    }

    @FunctionalInterface
    public interface DeviceRemovalListener {
        void onRemove (GraphStack gs, Device dev);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                     GRAPH STACK                                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /* +------------+ */
    /* | GRAPH DATA | */
    /* +------------+ */
    private GraphData dataref;

    public GraphData getGraphData () {
        return dataref;
    }

    public void setGraphData (GraphData ndata) {
        dataref = Objects.requireNonNull (ndata, "A data object reference must be specified");
        immuDevices.forEach (d -> d.setGraphData (ndata));
    }

    /* +--------------------------------+ */
    /* | INITIALIZATION & OTHER MEMBERS | */
    /* +--------------------------------+ */
    private final ToolRegion toolregion = new ToolRegion ();

    /* CORE STACK TOOLS */
    private final Tool motion = new GraphTools.Motion (this);

    private final ObservableList<Tool> tools = FXCollections.observableArrayList ();
    private final List<Tool> immuTools = Collections.unmodifiableList (tools);

    /* CORE STACK DEVICES (from back to front in viewing order) */
    private final GraphGrid grid;
    private final GraphView view;
    private final GraphLegend legend;

    private final ObservableList<Device> devices = FXCollections.observableArrayList ();
    private final List<Device> immuDevices = Collections.unmodifiableList (devices);

    /* constructors */
    public GraphStack (GraphData ref) {
        dataref = Objects.requireNonNull (ref, "A data object reference must be specified");

        /* ---- INIT TOOLS ---- */
        tools.addAll (motion);
        toolregion.setActiveTool (motion);
        toolregion.bindOrtho (this);

        /* ---- INIT DEVICES ---- */
        grid = new GraphGrid ();
        view = new GraphView (dataref);
        legend = new GraphLegend (dataref);

        grid.bindOrtho (this);
        view.bindOrtho (this);
        legend.bindOrtho (this);

        devices.addAll (grid, view, legend);

        /* ---- FINALIZE ---- */
        getChildren ().addAll (devices);
        getChildren ().add (toolregion);
    }

    public GraphStack () {
        this (new GraphData ());
    }

    /* +-------------------+ */
    /* | GETTERS & SETTERS | */
    /* +-------------------+ */

    /* TOOL REGION @NOTE(max): we may want to create an interface to hide critical properties of the region */
    public ToolRegion getToolRegion () {
        return toolregion;
    }

    /* TOOL LIST */
    public List<Tool> getImmutableToolList () {
        return immuTools;
    }

    /* DEVICE LIST */
    public List<Device> getImmutableDeviceList () {
        return immuDevices;
    }

    /* +-------------------------+ */
    /* | TOOL ADDITION LISTENERS | */
    /* +-------------------------+ */
    private final List<ToolAdditionListener> toolAdditionListeners = new ArrayList<> ();
    public void registerToolAdditionListener (ToolAdditionListener lis)
        { if (lis != null) toolAdditionListeners.add (lis); }
    public void removeToolAdditionListener (ToolAdditionListener lis)
        { toolAdditionListeners.remove (lis); }

    /* +------------------------+ */
    /* | TOOL REMOVAL LISTENERS | */
    /* +------------------------+ */
    private final List<ToolRemovalListener> toolRemovalListeners = new ArrayList<> ();
    public void registerToolRemovalListener (ToolRemovalListener lis)
        { if (lis != null) toolRemovalListeners.add (lis); }
    public void removeToolRemovalListener (ToolRemovalListener lis)
        { toolRemovalListeners.remove (lis); }

    /* +---------------------------+ */
    /* | DEVICE ADDITION LISTENERS | */
    /* +---------------------------+ */
    private final List<DeviceAdditionListener> deviceAdditionListeners = new ArrayList<> ();
    public void registerDeviceAdditionListener (DeviceAdditionListener lis)
        { if (lis != null) deviceAdditionListeners.add (lis); }
    public void removeDeviceAdditionListener (DeviceAdditionListener lis)
        { deviceAdditionListeners.remove (lis); }

    /* +--------------------------+ */
    /* | DEVICE REMOVAL LISTENERS | */
    /* +--------------------------+ */
    private final List<DeviceRemovalListener> deviceRemovalListeners = new ArrayList<> ();
    public void registerDeviceRemovalListener (DeviceRemovalListener lis)
        { if (lis != null) deviceRemovalListeners.add (lis); }
    public void removeDeviceRemovalListener (DeviceRemovalListener lis)
        { deviceRemovalListeners.remove (lis); }

    /* +----------------------+ */
    /* | UNDERLYING LISTENERS | */
    /* +----------------------+ */
    private final ListChangeListener<Tool> toolChangeListener = (change) -> {
        while (change.next ()) {
            if (change.wasAdded ()) {
                for (Tool added : change.getAddedSubList ())
                    toolAdditionListeners.forEach (l -> l.onAdd (this, added));
            }
            else if (change.wasRemoved ()) {
                for (Tool removed : change.getRemoved ())
                    toolRemovalListeners.forEach (l -> l.onRemove (this, removed));
            }
        }
    };

    private final ListChangeListener<Device> deviceChangeListener = (change) -> {
        while (change.next ()) {
            if (change.wasAdded ()) {
                for (Device added : change.getAddedSubList ())
                    deviceAdditionListeners.forEach (l -> l.onAdd (this, added));
            }
            else if (change.wasRemoved ()) {
                for (Device removed : change.getRemoved ())
                    deviceRemovalListeners.forEach (l -> l.onRemove (this, removed));
            }
        }
    };

    {
        tools.addListener (toolChangeListener);
        devices.addListener (deviceChangeListener);
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */
    @Override
    public void dispose () {
        super.dispose ();
        immuTools.forEach (Tool::dispose);
        immuDevices.forEach (Device::dispose);
        /* free listeners and whatnot */
        tools.removeListener (toolChangeListener);
        devices.removeListener (deviceChangeListener);
    }
}
