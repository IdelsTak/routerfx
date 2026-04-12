package com.github.idelstak.routerfx.shared.value;

import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

final class PreLoginSessionIdTest {

    @Test
    void keepsValidLowercaseHexValue() {
        var value = new PreLoginSessionId("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef").value();
        assertThat("Expected valid pre-login session id to be preserved", value, is("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef"));
    }

    @Test
    void rejectsValueWithWrongLength() {
        var error = assertThrows(IllegalArgumentException.class, () -> new PreLoginSessionId("abc"));
        assertThat("Expected short pre-login session id to be rejected", error.getClass(), is(IllegalArgumentException.class));
    }

    @Test
    void rejectsNonHexValue() {
        var error = assertThrows(IllegalArgumentException.class, () -> new PreLoginSessionId("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"));
        assertThat("Expected non-hex pre-login session id to be rejected", error.getClass(), is(IllegalArgumentException.class));
    }
}
