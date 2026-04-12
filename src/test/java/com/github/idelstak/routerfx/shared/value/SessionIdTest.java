package com.github.idelstak.routerfx.shared.value;

import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

final class SessionIdTest {

    @Test
    void rejectsBlankValue() {
        var error = assertThrows(IllegalArgumentException.class, () -> new SessionId("   "));
        assertThat("Expected blank session id to be rejected", error.getClass(), is(IllegalArgumentException.class));
    }

    @Test
    void rejectsWhitespaceInValue() {
        var error = assertThrows(IllegalArgumentException.class, () -> new SessionId("sess 001"));
        assertThat("Expected whitespace session id to be rejected", error.getClass(), is(IllegalArgumentException.class));
    }
}
