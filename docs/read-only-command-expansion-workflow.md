# RouterFX Read-Only Command Expansion Workflow

## Purpose

This document defines the one required workflow for adding new read-only router commands.

It prevents ad-hoc command onboarding and keeps protocol, MVU wiring, and UI changes consistent.

Write commands are explicitly out of scope for this workflow.

## Mandatory Checklist Per Command

Apply all steps in order for every new command:

1. Identify command
   - Confirm command id and purpose from HAR/JS/protocol evidence.
2. Classify auth boundary
   - Mark command as pre-auth or authenticated.
3. Verify request body
   - Record required fields (`cmd`, `method`, `language`, and others).
4. Verify token requirement
   - Record whether request needs a token field.
5. Verify session requirement
   - Record whether request uses empty session id or authenticated session id.
6. Verify response shape
   - Capture envelope expectations and required payload fields.
7. Add immutable model
   - Create one value object for normalized fields needed by the app.
8. Add one API method
   - Add one boundary method in `RouterApi` + protocol request/response mapping.
9. Add one small UI panel
   - Bind model fields to one focused panel in the authenticated or pre-auth view.
10. Add tests
    - Protocol mapping tests (success + malformed/failure).
    - MVU/effect tests for message/state mapping.
    - UI test for the new panel binding where applicable.

## Applied Example: `cmd:394` (`GET_STATUS_BAR`)

This issue applies the checklist to `cmd:394` as the first authenticated read-only expansion beyond `cmd:205`.

1. Command id and purpose
   - `394` maps to `GET_STATUS_BAR`.
2. Auth boundary
   - Authenticated command.
3. Request body
   - `{"cmd":394,"method":"GET","sessionId":"<auth_sid>","language":"en"}`
4. Token requirement
   - No token required in observed request.
5. Session requirement
   - Requires authenticated session id.
6. Response shape (normalized fields used now)
   - Envelope: `success`, `cmd`
   - Payload fields used: `signal_lvl`, `network_type_str`, `sim_status`, `sms_unread`
7. Immutable model
   - `StatusBarState(signalLevel, networkType, sim, smsUnread)`
8. API method
   - `Result<StatusBarState> fetchStatusBar(Session session)`
9. UI panel
   - Added authenticated status-bar panel with:
     - Signal Level
     - Network Type
     - SIM
     - SMS Unread
10. Tests
    - Protocol tests added for `fetchStatusBar` success and malformed response.
    - Effect/state/UI wiring tests updated for authenticated flow with status-bar data.

## Boundaries and Guardrails

- Keep this workflow read-only only until stability is proven.
- Do not add reboot, Wi-Fi mutation, APN writes, or other write operations through this path.
- Add commands incrementally, one command per focused issue where possible.
- Keep command numbers inside protocol-edge request builders; UI/update code must remain command-agnostic.
