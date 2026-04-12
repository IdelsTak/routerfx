# Implementing Reactive, Event-Driven MVU Architecture in JavaFX with Java 26

**To:** Engineering Team
**Subject:** Architecture Guide — MVU with Java 26 + JavaFX (no third-party reactive libraries)

Primary implementation reference: [RouterFX JavaFX App Unified Architecture](./javafx-unified-architecture.md)
Boundary outcome contract reference: [RouterFX Result Pattern at Boundaries](./router-result-pattern.md)

## Overview

This memo describes how to implement a clean Model-View-Update (MVU) architecture in JavaFX using only Java 26 and the JavaFX SDK. The pattern enforces unidirectional data flow, immutable state, and predictable rendering — without requiring RxJava, Reactor, or any other reactive library. All reactive primitives needed are already present in the JDK and JavaFX.

Implementation note for this repository:

- This document is an MVU reference memo and some snippets are intentionally simplified.
- The authoritative RouterFX target and current-state details live in `javafx-unified-architecture.md`.
- Current RouterFX code uses a framework-agnostic core `Store` plus a JavaFX `FxStore` adapter for thread-safe property publication.

![MVU Architecture — Unidirectional Data Flow](./mvu-architecture.png)

## Layer 1 — Model: Immutable State and Messages

The model layer has two responsibilities: representing application state as an immutable snapshot, and defining all possible events (messages) the system can respond to.

Use Java **records** for state. Records are shallow-immutable by default, provide structural equality, and read cleanly. Ensure collections inside records use `List.copyOf()` to prevent mutation.

```java
public record AppState(
    int count,
    String status,
    List<String> items
) {
    public AppState {
        items = List.copyOf(items); // defensive copy
    }
}
```

Use **sealed interfaces with nested records** to define your message types. The compiler enforces exhaustiveness - if you add a new message type and forget to handle it in `update()`, it is a compile error, not a runtime surprise.

```java
public sealed interface Msg permits
    Msg.Increment, Msg.Decrement, Msg.ItemAdded, Msg.ItemsLoaded {

    record Increment()                    implements Msg {}
    record Decrement()                    implements Msg {}
    record ItemAdded(String item)         implements Msg {}
    record ItemsLoaded(List<String> items) implements Msg {}
}
```

## Layer 2 — Update: Pure State Transitions

The `update()` function is the entire business logic of the application. It takes the current state and a message, and returns a new state. It must have **no side effects** - no I/O, no UI calls, no threading. This makes it trivially unit-testable.

```java
public interface Update {
    AppState apply(AppState state, Msg msg);
}

public final class StateUpdate implements Update {
    @Override
    public AppState apply(AppState state, Msg msg) {
        return switch (msg) {
            case Msg.ConnectRequested(var baseUrl, var credentials) ->
                connect(state, baseUrl, credentials);
            case Msg.RefreshRequested _ ->
                refresh(state);
            case Msg.Authenticated(var session, var radio) ->
                authenticate(state, session, radio);
            case Msg.Failed(var fault) ->
                fail(state, fault);
            default ->
                state;
        };
    }
}
```

Pattern matching on sealed types also lets you destructure message payloads inline (for example `ConnectRequested` and `Authenticated`), eliminating boilerplate getters.

## Layer 3 — Store: The Reactive Backbone

The `Store` is the single source of truth for immutable state transitions.

In RouterFX, JavaFX observation is handled by an adapter (`FxStore`) that mirrors `Store` snapshots into a `ReadOnlyObjectProperty<AppState>` on the JavaFX Application Thread.

```java
public final class Store {
    private final Update update;
    private final Effect effect;
    private final Executor executor;
    private final List<Consumer<AppState>> watchers = new ArrayList<>();
    private AppState state;

    public Store(AppState state, Update update, Effect effect, Executor executor) {
        this.state = state;
        this.update = update;
        this.effect = effect;
        this.executor = executor;
    }

    public synchronized AppState read() {
        return state;
    }

    public void dispatch(Msg msg) {
        var snapshot = mutate(msg);
        executor.execute(() -> effect.apply(snapshot, msg).ifPresent(this::dispatch));
    }

    public void watch(Consumer<AppState> watch) {
        var snapshot = register(watch);
        watch.accept(snapshot);
    }
}
```

Expose only `ReadOnlyObjectProperty` to the outside world from the JavaFX adapter. Views can observe state, but only the core store can write it. This is the boundary that enforces unidirectional flow.

## Layer 4 — View: Declarative Bindings

Views observe the `FxStore` state property and update UI elements reactively. JavaFX's `ObservableValue.map()` (available since JavaFX 19) lets you project specific fields from state into bindings without manual listeners.

```java
public final class FxStore {
    private final Store store;
    private final ReadOnlyObjectWrapper<AppState> state;

    public FxStore(Store store) {
        this.store = store;
        this.state = new ReadOnlyObjectWrapper<>(store.read());
        store.watch(snapshot -> Platform.runLater(() -> state.set(snapshot)));
    }

    public ReadOnlyObjectProperty<AppState> stateProperty() {
        return state.getReadOnlyProperty();
    }

    public void dispatch(Msg msg) {
        store.dispatch(msg);
    }
}
```

Views contain zero business logic. They translate state into UI and user gestures into messages.

## Handling Async Side Effects with Virtual Threads

Some messages trigger I/O - API calls, database queries, file reads. These must never block the JavaFX Application Thread. Virtual threads (GA since Java 21) handle this without callbacks or schedulers.

The pattern is: dispatch a message, let `Effect` do the blocking boundary work off the UI thread, then dispatch the result message back into the store. In RouterFX, JavaFX thread publication is handled at the `FxStore` adapter boundary.

```java
public interface Effect {
    Optional<Msg> apply(AppState state, Msg msg);
}

public final class FlowEffects implements Effect {
    private final List<Effect> effects;

    @Override
    public Optional<Msg> apply(AppState state, Msg msg) {
        for (Effect effect : effects) {
            Optional<Msg> result = effect.apply(state, msg);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }
}
```

For more complex multi-step effects (parallel API calls, fan-out/fan-in), use `StructuredTaskScope`:

```java
executor.execute(() -> {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var userTask  = scope.fork(() -> userService.fetch(id));
        var itemsTask = scope.fork(() -> itemService.fetchAll(id));
        scope.join().throwIfFailed();
        store.dispatch(new Msg.DashboardLoaded(userTask.get(), itemsTask.get()));
    } catch (Exception e) {
        store.dispatch(new Msg.Failed(new RouterFault.TransportFault(e.getMessage())));
    }
});
```

## Boundary Outcomes with Result

For RouterFX IO boundaries, represent expected runtime failures as `Result<T>`, not thrown transport exceptions.

```java
routerApi.login(credentials, challenge).fold(
    session -> {
        dispatch(new Msg.LoginSucceeded(session));
        return 0;
    },
    fault -> {
        dispatch(new Msg.LoginFailed(fault));
        return 0;
    }
);
```

This keeps failure handling explicit in effects and keeps `update()` exception-free.

## Wiring It All Together

The application entry point creates one core store, one `FxStore` adapter, and connects the view:

```java
public final class DesktopApp extends Application {

    @Override
    public void start(Stage stage) {
        var store = new Store(initialState, new StateUpdate(), effects, executor);
        var fxStore = new FxStore(store);
        var root = new DashboardPane(fxStore).view();

        Scene scene = new Scene(root, 960, 640);
        stage.setScene(scene);
        stage.setTitle("RouterFX");
        stage.show();
    }
}
```

The store is passed by constructor injection. Avoid passing it through deeply nested view hierarchies - prefer scoping sub-views to the parts of state they actually need, using `fxStore.stateProperty().map(...)` to narrow the observable.

## Key Design Rules

There are four rules that, if followed consistently, prevent the architecture from degrading:

**State is always in the store.** No state lives in view fields, static variables, or caches outside `AppState`. If the view needs it, it belongs in the model.

**Views never call `update()` directly.** All state changes go through `store.dispatch()`. This is what keeps the flow unidirectional.

**`update()` has no side effects.** It is a pure function. Pass a state and a message in, get a new state out. Nothing else happens. Testing it requires no JavaFX runtime, no mocks, no threads.

**Side effects always produce messages.** Async work completes by dispatching a message back to the store. It never mutates state directly, and it never touches the UI directly.

## Summary

Java 26 and JavaFX provide everything needed for a production-quality MVU application. Records and sealed interfaces give you a type-safe, immutable model layer. In RouterFX, `FxStore` publishes an observable `ReadOnlyObjectProperty<AppState>` backed by a framework-agnostic core store. `ObservableValue.map()` and `Bindings` replace reactive operators for view projection. Virtual threads and `StructuredTaskScope` replace schedulers and `flatMap` chains for async side effects. The result is an architecture that is simpler to debug, easier to test, and requires no external reactive framework.
