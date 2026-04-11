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
public sealed interface Result<T> permits Success, Failure {}

public record Success<T>(T value) implements Result<T> {}

public record Failure<T>(RouterError error) implements Result<T> {}
```

```java
public sealed interface RouterError permits
    AuthError,
    SessionExpiredError,
    TimeoutError,
    TransportError,
    ProtocolError,
    MalformedResponseError,
    UnsupportedCommandError {}
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

- `Success<Session>` -> `LoginSucceeded`
- `Failure<AuthError>` -> `LoginFailed(AuthRejected)`
- `Failure<TimeoutError>` -> `LoginFailed(Timeout)`
- `Success<RadioState>` -> `DashboardLoaded`
- `Failure<SessionExpiredError>` -> `DashboardLoadFailed(SessionExpired)`

Update logic remains pure and deterministic. No exception handling inside update transitions.

## Testing Guidance

For each boundary method, test:

- success mapping
- each expected failure mapping
- no raw exception text leaked to user-facing output paths

For MVU effects, test:

- `Result` to `Msg` mapping
- retry/reconnect policy decisions by error type

## Migration Plan (Current Proof Client)

1. Add `Result` and typed `RouterError` contracts in the shared boundary package.
2. Refactor `HttpRouterApi` to return `Result` and map all expected transport/protocol outcomes.
3. Refactor `ProofCli` to switch on `Result` instead of catch-all boundary handling.
4. Keep constructor/value-object validation as exceptions.
5. Add targeted unit tests for each `Failure` variant.

## Non-Goals

- replacing exceptions in all layers
- wrapping pure computation methods in `Result`
- introducing third-party reactive libraries

