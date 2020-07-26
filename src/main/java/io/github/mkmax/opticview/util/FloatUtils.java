package io.github.mkmax.opticview.util;

import java.text.DecimalFormat;

/* extended operations on IEEE-754 32-bit and 64-bit floating point numbers */
public class FloatUtils {

    /* +-----------+ */
    /* | CONSTANTS | */
    /* +-----------+ */
    public static final class Binary32Constants {
        public static final int   EXP_BIAS       = -127;
        public static final int   SGN_BITMASK    = 0x80_00_00_00;
        public static final int   EXP_BITMASK    = 0x7f_80_00_00;
        public static final int   EXP_OFFSET     = 23;
        public static final int   SIGNIF_BITMASK = 0x00_7f_ff_ff;
        public static final float EPSILON        = 1.4e-45f;
    }

    public static final class Binary64Constants {
        public static final int    EXP_BIAS       = -1023;
        public static final long   SGN_BITMASK    = 0x80_00_00_00_00_00_00_00L;
        public static final long   EXP_BITMASK    = 0x7f_f0_00_00_00_00_00_00L;
        public static final int    EXP_OFFSET     = 52;
        public static final long   SIGNIF_BITMASK = 0x00_0f_ff_ff_ff_ff_ff_ffL;
        public static final double EPSILON        = 4.9e-324d;
    }

    /* +----------+ */
    /* | EQUALITY | */
    /* +----------+ */
    public static boolean equal (float a, float b) {
        return equal (a, b, Binary32Constants.EPSILON);
    }

    public static boolean equal (double a, double b) {
        return equal (a, b, Binary64Constants.EPSILON);
    }

    public static boolean equal (float a, float b, float epsilon) {
        return Math.abs (a - b) <= epsilon;
    }

    public static boolean equal (double a, double b, double epsilon) {
        return Math.abs (a - b) <= epsilon;
    }

    /* +----------+ */
    /* | CLAMPING | */
    /* +----------+ */
    public static float clamp (float val, float bottom, float top) {
        if (bottom > top)
            return clamp (val, top, bottom);
        return Math.max (Math.min (val, top), bottom);
    }

    public static double clamp (double val, double bottom, double top) {
        if (bottom > top)
            return clamp (val, top, bottom);
        return Math.max (Math.min (val, top), bottom);
    }

    /* +------------+ */
    /* | FRACTIONAL | */
    /* +------------+ */
    public static double fract (double val) {
        return val - Math.floor (val);
    }

    public static boolean isFractZero (double val) {
        return equal (fract (val), 0d);
    }

    /* +-----------+ */
    /* | STRINGIFY | */
    /* +-----------+ */
    public static String toPrecisionString (double val, int digits) {
        digits = Math.max (0, digits);
        if (digits == 0)
            return new DecimalFormat ("0").format (val);
        return new DecimalFormat ("0." + "0".repeat (digits)).format (val);
    }

    public static String toScientificString (double val, int digits) {
        digits = Math.max (0, digits);
        if (digits == 0)
            return new DecimalFormat ("0E0").format (val);
        return new DecimalFormat ("0." + "0".repeat (digits) + "E0").format (val);
    }
}
