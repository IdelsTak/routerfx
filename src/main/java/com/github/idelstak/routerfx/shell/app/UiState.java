package com.github.idelstak.routerfx.shell.app;

import java.util.*;

public record UiState(
  boolean busy,
  boolean connected,
  String note,
  boolean canRefresh,
  boolean loginOverlayVisible
) {

    public UiState {
        Objects.requireNonNull(note, "note must not be null");
    }
}
