package io.github.mkmax.opticview.units;

import java.util.Objects;

public enum MetricDistance implements IUnit {

    KILOMETERS  ( "km",  "kilometers", 1e3d),
    HECTOMETERS ( "hm", "hectometers", 1e2d),
    DECAMETERS  ("dam",  "decameters", 1e1d),
    METERS      (  "m",      "meters", 1e0d),
    DECIMETERS  ( "dm",  "decimeters", 1e-1d),
    CENTIMETERS ( "cm", "centimeters", 1e-2d),
    MILLIMETERS ( "mm", "millimeters", 1e-3d),
    MICROMETERS ( "μm", "micrometers", 1e-6d),
    NANOMETERS  ( "nm",  "nanometers", 1e-9d),
    PICOMETERS  ( "pm",  "picometers", 1e-12d),
    FEMTOMETERS ( "fm", "femtometers", 1e-15d);

    /* +--------------------+ */
    /* | CONSTRUCTOR & DATA | */
    /* +--------------------+ */
    public final String MNEMONIC, NAME;
    public final double BASE;

    MetricDistance (String pMnemonic, String pName, double pBase) {
        MNEMONIC = Objects.requireNonNull (pMnemonic);
        NAME = Objects.requireNonNull (pName);
        BASE = pBase;
    }

    /* +-----------+ */
    /* | INTERFACE | */
    /* +-----------+ */
    @Override
    public String getMnemonic () {
        return MNEMONIC;
    }

    @Override
    public String getName () {
        return NAME;
    }

    @Override
    public boolean isConvertibleTo (IUnit dest) {
        return dest instanceof MetricDistance;
    }

    @Override
    public boolean isConvertibleFrom (IUnit source) {
        return source instanceof MetricDistance;
    }

    @Override
    public double convertTo (IUnit dest, double value) {
        return getMultiplierTo (dest) * value;
    }

    @Override
    public double convertFrom (IUnit source, double value) {
        return getMultiplierFrom (source) * value;
    }

    @Override
    public String toString () {
        return getName ();
    }

    /* +-----------+ */
    /* | UTILITIES | */
    /* +-----------+ */
    public double getMultiplierTo (IUnit to) {
        Objects.requireNonNull (to);
        if (to instanceof MetricDistance)
            return BASE / ((MetricDistance) to).BASE;
        throw new IncompatibleUnitsException (this, to,
            getName () + " cannot convert to " + to.getName ());
    }

    public double getMultiplierFrom (IUnit from) {
        Objects.requireNonNull (from);
        if (from instanceof MetricDistance)
            return ((MetricDistance) from).BASE / BASE;
        throw new IncompatibleUnitsException (this, from,
            getName () + "cannot be converted from " + from.getName ());
    }
}