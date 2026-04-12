# RouterFX JavaFX App Unified Architecture

## Purpose

This document defines one implementation architecture for the proposed JavaFX app.
It combines:

- the MVU design in `mvu-architecture.md`
- boundary outcome design in `router-result-pattern.md`
- protocol constraints from the router analysis docs
- coding constraints from `agent-docs/coding-guidelines/yegor-best-practices.md`

The goal is predictable state flow, testable decision logic, and strict object boundaries.

## Document Update Policy

This document is primarily a target architecture guide, not a line-by-line mirror of current code.

- Keep architecture intent sections stable even when implementation is still evolving.
- Update `Current implementation status` as code changes land.
- Only change target architecture sections when architecture direction itself changes by explicit decision.
- Avoid rewriting target-state examples to match temporary implementation shortcuts.

## Scope

This architecture covers the first product slice:

- login challenge fetch (`cmd:232`)
- login submit (`cmd:100`)
- initial network dashboard load (`cmd:205`)

All other command families are out of scope for this document and follow the same extension pattern.

## Architecture Summary

The app uses unidirectional MVU flow:

1. User action in JavaFX view emits a `Msg`.
2. `Store` receives the message and asks `Update` for the next `State`.
3. `Update` returns a new immutable state (pure decision logic only).
4. `Store` publishes the new state snapshot to observers.
5. `FxStore` mirrors snapshots into a JavaFX `ReadOnlyObjectProperty<AppState>`.
6. Views are bound to projected `FxStore` state and re-render automatically.
7. Side effects (HTTP, hashing, time, thread orchestration) run outside `Update` and publish result messages back into the store.

This keeps domain behavior deterministic and side effects isolated at system edges.

## Design Principles

The implementation must follow these non-negotiable rules:

- One source of truth: all UI-visible state lives in `AppState`.
- Immutable state: model types use records and defensive copies.
- Exhaustive events: messages use sealed interfaces and record variants.
- Pure update: state transitions contain no IO, no JavaFX UI calls, no thread calls.
- Side effects at edges only: effect handlers call router APIs and dispatch result messages.
- Boundary contracts use `Result<T>` for expected runtime failures.
- No anemic global helpers: avoid utility classes and static business methods.
- Narrow contracts: behavior exposed through interfaces, implemented by final classes.
- Constructor validity: objects reject invalid state at construction boundaries.
- Small cohesive objects: each class has one reason to change.

## Runtime Building Blocks

### 1) Model (State)

`AppState` is a root immutable record that composes feature records.

- `SessionState` handles auth/session lifecycle.
- `DashboardState` handles network metrics and refresh status.
- `UiState` handles local UX status such as busy flags and error banners.

Collections are copied with `List.copyOf(...)` in compact constructors.

### 2) Messages (Events)

`Msg` is a sealed interface with nested record variants.
Examples:

- `LoginRequested(username, password)`
- `ChallengeReceived(token)`
- `LoginSucceeded(sessionId, auth, userLevel)`
- `LoginFailed(reason)`
- `DashboardRequested()`
- `DashboardLoaded(metrics)`
- `DashboardLoadFailed(reason)`

Every new variant must be handled in update logic through exhaustive pattern matching.

### 3) Update (Pure Transition Engine)

`Update` is an interface that maps `(state, msg) -> state`.
Implementation (`StateUpdate`) is a final object, not a static helper.

Responsibilities:

- derive next immutable state
- set intent flags that request effects (for example `needsLogin=true`, `needsDashboardLoad=true`)
- clear intent flags once corresponding result messages arrive

Forbidden responsibilities:

- network calls
- hashing
- clock/thread APIs
- direct JavaFX node mutation

### 4) Store (Single State Owner)

`Store` owns:

- current immutable `AppState`
- a message dispatch API
- synchronous state transition application through `Update`

On dispatch:

1. compute next state via `Update`
2. publish next state
3. trigger effect orchestration for newly raised intents

The store is the only writable owner of application state.

JavaFX thread publication is handled by `FxStore`, which mirrors core store snapshots to a JavaFX `ReadOnlyObjectProperty<AppState>` via `Platform.runLater(...)`.

### 5) Effect Orchestrator

`Effect` is an interface with implementations per side-effect family.

Examples:

- `LoginEffect`
- `RefreshEffect`

`FlowEffects` composes feature effects and delegates based on message type.

Effects run blocking work on virtual threads and return only messages, never direct state mutation.

Pattern:

1. read current state snapshot
2. call protocol client(s)
3. map response to result `Msg`
4. dispatch result `Msg` back to the store

### 6) View Layer (JavaFX)

Views are thin bindings from state projection to JavaFX controls.

- bind labels/buttons/visibility to projected state
- emit messages from UI events
- contain no business decision logic

Views never call protocol clients directly.

## Router Protocol Slice Mapping

The initial MVU flow maps to known router commands:

1. `LoginRequested` intent triggers `cmd:232` challenge request.
2. `ChallengeReceived` and credentials drive `cmd:100` login request.
3. `LoginSucceeded` stores authenticated session context in state.
4. `DashboardRequested` triggers `cmd:205` load with authenticated `sessionId`.
5. `DashboardLoaded` updates dashboard metrics state.

Envelope handling is centralized in edge adapters so update logic stays protocol-agnostic.

## Package Structure (Vertical Slices)

Use capability-oriented packages, not technical-layer buckets.

- `routerfx.auth.login` : login state/messages/update/effects/view wiring
- `routerfx.dashboard.network` : dashboard state/messages/update/effects/view wiring
- `routerfx.shell.app` : root state composition, root store, app bootstrap
- `routerfx.shell.cli` : CLI entry flow, secure password input, CLI reporting
- `routerfx.router.protocol` : HTTP transport + command adapters + response mapping
- `routerfx.shared.value` : small immutable value objects used across slices

Avoid package names such as `util`, `manager`, `service`, `model`, or `dao`.

Current implementation status:

- `com.github.idelstak.routerfx.shell.app` is implemented for app bootstrap, root state, update, and store wiring.
- `com.github.idelstak.routerfx.shell.cli` is implemented for CLI flow and secure password input.
- `com.github.idelstak.routerfx.router.protocol` is implemented for HTTP/protocol edge adapters.
- `com.github.idelstak.routerfx.shared.value` is implemented for immutable shared value records.
- `Store` + `FxStore` hybrid state publication is implemented and enforces JavaFX thread affinity at the adapter boundary.
- `FlowEffects` delegates to feature-scoped effect objects in vertical slices (`auth.login.LoginEffect`, `dashboard.network.RefreshEffect`, `dashboard.network.PeriodicRefreshEffect`).
- `DesktopApp` + `DashboardPane` are implemented for login, common pre-login dashboard metrics, authenticated dashboard metrics, and refresh flow.
- `DesktopAppTest` TestFX coverage is implemented for connect/refresh success paths, failure paths, and loading-state button behavior.

## Coding Guideline Alignment

To keep MVU aligned with Yegor-style constraints, implement with these adaptations:

- Use interfaces for behavior contracts (`Update`, `Effect`, `Transport`).
- Implement contracts in final classes with constructor-injected dependencies.
- Avoid static methods for business logic; keep behavior in objects.
- Keep class attributes to a small bounded set; split objects when responsibilities grow.
- Prefer records for immutable state and value objects.
- Avoid getters as data leaks in domain behavior objects; state records remain the explicit MVU snapshot contract.
- Keep side effects in adapters/decorators at the boundary.
- Keep constructor/value invariants fail-fast with contextual exceptions.
- Return typed `Result` failures for expected runtime boundary outcomes.

Where a guideline and JavaFX framework mechanics conflict, framework constraints apply at the boundary only, and domain/update objects stay guideline-compliant.

## Concurrency Model

- JavaFX Application Thread owns UI node mutation.
- Virtual threads run blocking IO and hashing operations.
- `FxStore` marshals JavaFX property publication onto the JavaFX thread.
- Effect handlers never block UI thread.

For multi-call workflows, use structured concurrency and convert completion/failure to result messages.

## Error and Recovery Strategy

- Parse protocol envelope at adapter boundary.
- Convert boundary outcomes to `Result<T>` with typed `RouterFault` variants.
- Do not leak transport exceptions across boundary interfaces.
- Dispatch failure messages (`LoginFailed`, `DashboardLoadFailed`) and render user-facing state.
- Never swallow invariant exceptions; enrich and fail fast where object validity is broken.

## Test Strategy

Test coverage follows risk and MVU boundaries:

- update logic: pure unit tests per message transition
- effect handlers: boundary tests for request/response mapping and failure conversion
- protocol adapters: contract tests for envelope parsing
- JavaFX views: TestFX behavior checks for message emission and state binding

Priority is deterministic transition coverage for login and initial dashboard load.

## Extension Pattern

For each new router feature:

1. add state slice to `AppState`
2. add message variants
3. extend pure update transitions
4. add effect adapter for new command set
5. bind new view controls to projected state

No feature may bypass store dispatch or mutate state outside update flow.

## Implementation Checklist

A change is architecture-compliant when all are true:

- all user-visible state is in immutable records
- all transitions are in update object(s) and are side-effect free
- all side effects return messages and never mutate state directly
- store is the only state writer
- view code has no business decisions
- package names follow capability slices
- behavior contracts are interface-first and implemented by final classes
- router boundary methods return `Result<T>` for expected runtime failures
