package io.github.mkmax.opticview.units;

public interface IUnit {

    /* Obtains the human readable mnemonic of this unit.
     * For example, meters should return "m".
     *
     * The mnemonic may be any UTF compatible string.
     */
    String getMnemonic ();

    /* Obtains the human readable name of this unit.
     * For example, meters should return "meters".
     */
    String getName ();

    /* Returns true if this unit can be converted into the specified
     * unit.
     */
    boolean isConvertibleTo (IUnit dest);

    /* Returns true if this unit can be converted to from the specified
     * unit.
     */
    boolean isConvertibleFrom (IUnit source);

    /* Converts a number in the current units to the destination units.
     * For example, if this object is MetricDistance.METERS, dest
     * is MetricDistance.KILOMETERS, and value is 1000, this should
     * return a value of 1.
     */
    double convertTo (IUnit dest, double value);

    /* Converts a number in the source units to the current units.
     * For example, if this object is MetricDistance.METERS, source
     * is MetricDistance.KILOMETERS, and value is 1, this should
     * return a value of 1000.
     */
    double convertFrom (IUnit source, double value);

}
