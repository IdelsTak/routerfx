package com.github.idelstak.routerfx.shared.value;

import java.util.*;

public record SessionId(String value) {

    public SessionId {
        Objects.requireNonNull(value, "value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
        if (value.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("value must not contain whitespace");
        }
    }
}
