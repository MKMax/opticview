package io.github.mkmax.opticview.units;

import java.util.Objects;

/* An exception thrown by conversion to and from unrelated units. */
public class IncompatibleUnitsException extends RuntimeException {

    private final IUnit first, second;

    public IncompatibleUnitsException (IUnit pFirst, IUnit pSecond, String pMessage) {
        super (pMessage);
        first = Objects.requireNonNull (pFirst);
        second = Objects.requireNonNull (pSecond);
    }

    public IUnit getFirstUnit () {
        return first;
    }

    public IUnit getSecondUnit () {
        return second;
    }
}
