package com.github.idelstak.routerfx.shared.result;

import com.github.idelstak.routerfx.router.protocol.*;
import java.util.*;
import java.util.function.*;

public sealed interface Result<T> {

    <U> U fold(Function<T, U> success, Function<RouterFault, U> failure);

    default <U> Result<U> map(Function<T, U> mapper) {
        return fold(value -> new Success<>(mapper.apply(value)), Failure::new);
    }

    default <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        return fold(mapper, Failure::new);
    }

    record Success<T>(T value) implements Result<T> {

        public Success {
            Objects.requireNonNull(value, "value must not be null");
        }

        @Override
        public <U> U fold(Function<T, U> success, Function<RouterFault, U> failure) {
            Objects.requireNonNull(success, "success must not be null");
            Objects.requireNonNull(failure, "failure must not be null");
            return success.apply(value);
        }
    }

    record Failure<T>(RouterFault fault) implements Result<T> {

        public Failure {
            Objects.requireNonNull(fault, "fault must not be null");
        }

        @Override
        public <U> U fold(Function<T, U> success, Function<RouterFault, U> failure) {
            Objects.requireNonNull(success, "success must not be null");
            Objects.requireNonNull(failure, "failure must not be null");
            return failure.apply(fault);
        }
    }
}
