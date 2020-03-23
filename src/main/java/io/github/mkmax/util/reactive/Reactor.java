package io.github.mkmax.util.reactive;

import java.util.Collection;

public interface Reactor<T> {

    void attach (Emitter<?> dep);

    void detach (Emitter<?> dep);

    Collection<Emitter<?>> dependencies ();

}
