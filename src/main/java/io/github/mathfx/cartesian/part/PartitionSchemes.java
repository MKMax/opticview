package io.github.mathfx.cartesian.part;

import java.util.Objects;

public enum PartitionSchemes {
    DECIMAL (DecimalPartitionScheme.getInstance ());

    public final PartitionScheme IMPL;

    PartitionSchemes (final PartitionScheme impl) {
        IMPL = Objects.requireNonNull (impl);
    }
}
