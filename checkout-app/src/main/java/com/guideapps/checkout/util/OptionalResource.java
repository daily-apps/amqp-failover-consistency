package com.guideapps.checkout.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class OptionalResource<T> {

    static public <T> OptionalResource<T> from(final Optional<T> optional) {
        return new OptionalResource<T>(optional);
    }

    private final Optional<T> optional;
    private OptionalResource(final Optional<T> optional) {
        this.optional = optional;
    }

    public <U> T ifPresent(final Function<? super T, T> mapper) throws Exception {
        return optional
                .map(c -> mapper.apply(c))
                .orElseThrow(() -> new Exception("Resource not found."));
    }

    public T okIfPresent(final Consumer<? super T> consumer) throws Exception {
        return optional
                .map(c -> {
                    consumer.accept(c);
                    return c;
                })
                .orElseThrow(() -> new Exception("Resource not found."));
    }
}