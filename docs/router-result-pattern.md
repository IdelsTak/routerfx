# RouterFX Result Pattern at Boundaries

## Purpose

This document defines how RouterFX uses the `Result` pattern for router-facing operations.

Goal:

- model expected runtime failures as typed outcomes
- keep pure domain logic free from transport exceptions
- make MVU effect handling explicit and testable

## Why This Is Needed

Router communication has many normal failure states:

- wrong credentials
- session expiry
- timeout
- router offline
- malformed firmware response
- unsupported command

These are expected outcomes in this domain. They should be part of the contract, not hidden in uncaught exception flow.

## Boundary Rule

Use `Result<T>` at system boundaries only.

Use `Result<T>` for:

- HTTP transport adapter responses
- protocol command execution (`cmd:232`, `cmd:100`, `cmd:205`)
- parsing boundary from raw response to typed payload

Do not use `Result<T>` for:

- value object construction
- pure update/state transition logic
- UI formatting/projection helpers

## Contract Shape

```java
public sealed interface Result<T> {
    <U> U fold(Function<T, U> success, Function<RouterFault, U> failure);
    <U> Result<U> map(Function<T, U> mapper);
    <U> Result<U> flatMap(Function<T, Result<U>> mapper);
    record Success<T>(T value) implements Result<T> {}
    record Failure<T>(RouterFault fault) implements Result<T> {}
}
```

```java
public sealed interface RouterFault permits
    RouterFault.AuthFault,
    RouterFault.SessionExpiredFault,
    RouterFault.TimeoutFault,
    RouterFault.TransportFault,
    RouterFault.ProtocolFault,
    RouterFault.MalformedResponseFault,
    RouterFault.UnsupportedCommandFault {}
```

Use typed errors. Do not use `Failure<String>`.

## Exception Rule

Use `Result` when failure is expected at runtime.

Throw exceptions only for programmer/invariant failures:

- null constructor arguments
- invalid immutable value object state
- impossible internal states

This keeps fail-fast object validity while preserving explicit runtime boundary outcomes.

## RouterFX API Direction

Boundary interfaces should return `Result`:

```java
Result<Challenge> fetchChallenge();
Result<Session> login(Credentials credentials, Challenge challenge);
Result<RadioState> fetchRadioState(Session session);
```

The HTTP/protocol adapter maps transport/parsing/envelope failures to `Failure(error)`.
It does not leak raw transport exceptions outside the boundary.

## MVU Integration

Effects convert boundary `Result` to messages:

- `Result.Success<Session>` -> `LoginSucceeded`
- `Result.Failure<Session>` with `AuthFault` -> `LoginFailed(AuthRejected)`
- `Result.Failure<Session>` with `TimeoutFault` -> `LoginFailed(Timeout)`
- `Result.Success<RadioState>` -> `DashboardLoaded`
- `Result.Failure<RadioState>` with `SessionExpiredFault` -> `DashboardLoadFailed(SessionExpired)`

Update logic remains pure and deterministic. No exception handling inside update transitions.

## Composition Rule

Use `map` for success-value transformation, `flatMap` for boundary chaining, and `fold` at the edge to select the final output.

Current RouterFX chain style:

```java
api.fetchChallenge()
  .flatMap(challenge -> api.login(credentials, challenge))
  .flatMap(api::fetchRadioState);
```

At UI/CLI edges, keep decision and side effects separate by mapping to an intermediate outcome in `fold`, then reporting in a second step.

## Testing Guidance

For each boundary method, test:

- success mapping
- each expected failure mapping
- no raw exception text leaked to user-facing output paths

For MVU effects, test:

- `Result` to `Msg` mapping
- retry/reconnect policy decisions by error type

## Current Status

As of the current implementation state:

- package split toward vertical slices is in place (`shell.app`, `router.protocol`, `shared.value`, feature boundary packages)
- login/dashboard protocol command handling (`cmd:232`, `cmd:100`, `cmd:205`) is centralized at the protocol edge
- boundary interfaces return `Result<T>` for challenge, login, and dashboard reads
- protocol adapter maps transport/parsing/envelope failures to typed `RouterFault` values
- Proof CLI composes boundary calls with `flatMap` and resolves to CLI outcomes via `fold`
- unit tests cover both success and failure mapping for the boundary methods

## Non-Goals

- replacing exceptions in all layers
- wrapping pure computation methods in `Result`
- introducing third-party reactive libraries
