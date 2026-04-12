package com.github.idelstak.routerfx.shared.value;

import java.util.*;

public record PreLoginSessionId(String value) {

    public PreLoginSessionId {
        Objects.requireNonNull(value, "value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
        if (value.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("value must not contain whitespace");
        }
        if (value.length() != 64) {
            throw new IllegalArgumentException("value must be 64 characters");
        }
        if (!value.matches("[0-9a-f]{64}")) {
            throw new IllegalArgumentException("value must be lowercase hexadecimal");
        }
    }
}
