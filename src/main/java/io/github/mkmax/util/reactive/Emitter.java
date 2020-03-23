package io.github.mkmax.util.reactive;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Collection;

public interface Emitter<T> {

    int state ();

    void use (Consumer<T> func);

    void modify (Function<T, T> func);

    void subscribe (Reactor<?> dep);

    void unsubscribe (Reactor<?> dep);

    Collection<Reactor<?>> dependants ();

}
