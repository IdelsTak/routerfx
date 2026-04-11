# Airtel Router Web UI Protocol Reconstruction (HAR + JS Evidence)

Implementation context: [RouterFX JavaFX App Unified Architecture](./javafx-unified-architecture.md)

## Request sequence

From login page load to first authenticated dashboard data (`cmd:205`), based on HAR entry order:

1. Pre-login polling while unauthenticated:
   - `cmd:113` and `cmd:133` repeatedly with `sessionId:""` (HAR indexes `0..19`).
2. Login click flow:
   - `cmd:232` (`GET`) with empty session (HAR `20`), body:
     - `{"cmd":232,"method":"GET","sessionId":"","language":"en"}`
   - `cmd:100` (`POST`) login request (HAR `21`), body includes generated `sessionId`, `username`, transformed `passwd`.
3. First authenticated load after redirect:
   - First authenticated CGI request is `cmd:80` (HAR `22`) with non-empty `sessionId`:
     - `sessionId:"9a86cd1b...046a8532"`
   - Followed by: `113` (HAR `28`), `394` (`30`), `113` (`31`), `134` (`32`), `80` (`33`,`34`), `233` (`35`), `269` (`37`), `233` (`39`), `269` (`40`), then `205` (`43`).
4. First authenticated dashboard/network metrics payload:
   - `cmd:205` at HAR `43`.

Evidence:
- HAR timeline extracted from `192.168.1.1.har` (entry indexes listed above).
- Login redirect behavior in JS: on successful login, `location.href = Page.getUrl("index.html")` at [login.js:170](/home/kamau/MEGA/routerfx-dev/login.js:170).

## Authentication model

1. Cookie `sessionId` auth: **no evidence**.
   - HAR has no request `Cookie` header and no response `Set-Cookie` headers for CGI entries.
   - Login code clears browser cookies on mount at [login.js:334](/home/kamau/MEGA/routerfx-dev/login.js:334)-[login.js:338](/home/kamau/MEGA/routerfx-dev/login.js:338).
2. JSON body `sessionId`: **yes, primary evidence**.
   - Request wrapper injects `sessionId` into request JSON body at [main.js:1452](/home/kamau/MEGA/routerfx-dev/main.js:1452).
   - Login success stores body session value: `sessionStorage.sessionId = t.sessionId` at [login.js:167](/home/kamau/MEGA/routerfx-dev/login.js:167).
   - All authenticated HAR requests carry non-empty JSON `sessionId` (e.g., HAR `22`, `43`).
3. Other token:
   - `cmd:232` response token is used for password transformation (`var s = n.token`) at [login.js:138](/home/kamau/MEGA/routerfx-dev/login.js:138).
   - Wrapper adds `token` to `POST` requests: [main.js:1449](/home/kamau/MEGA/routerfx-dev/main.js:1449)-[main.js:1450](/home/kamau/MEGA/routerfx-dev/main.js:1450).
   - `cmd:233` returns token in HAR (`{"success":true,"cmd":233,"token":"..."}` at HAR `35`, `39`).

## Password transformation

Exact formula used by login path:

1. Get challenge token from `cmd:232` response:
   - [login.js:133](/home/kamau/MEGA/routerfx-dev/login.js:133)-[login.js:138](/home/kamau/MEGA/routerfx-dev/login.js:138)
2. Build login payload:
   - `passwd: sha256_digest(s + r)` where:
   - `s = token from cmd:232`
   - `r = plaintext password from input`
   - [login.js:142](/home/kamau/MEGA/routerfx-dev/login.js:142), [login.js:158](/home/kamau/MEGA/routerfx-dev/login.js:158)
3. `sha256_digest` implementation:
   - Defined in [sha256.js:129](/home/kamau/MEGA/routerfx-dev/sha256.js:129)-[sha256.js:134](/home/kamau/MEGA/routerfx-dev/sha256.js:134)
   - Returns lowercase hex via [sha256.js:123](/home/kamau/MEGA/routerfx-dev/sha256.js:123)-[sha256.js:127](/home/kamau/MEGA/routerfx-dev/sha256.js:127)

Therefore:

`passwd = SHA256_HEX_LOWER( token_from_cmd232 + plaintext_password )`

No delimiter is present in source between token and password.

## Command map

Observed `/cgi-bin/http.cgi` commands in HAR, grouped by `cmd`:

1. `80` (`INIT_PAGE`)
   - Likely purpose: UI/feature bootstrap (theme, logo, capabilities).
   - Evidence: enum [main.js:63](/home/kamau/MEGA/routerfx-dev/main.js:63); rich config response payload in HAR `22`, `33`, `34`.
2. `100` (`LOGIN`)
   - Likely purpose: authenticate user.
   - Evidence: enum [main.js:69](/home/kamau/MEGA/routerfx-dev/main.js:69); login payload construction [login.js:154](/home/kamau/MEGA/routerfx-dev/login.js:154)-[login.js:160](/home/kamau/MEGA/routerfx-dev/login.js:160); HAR `21`.
3. `113` (`GET_SYS_STATUS`)
   - Likely purpose: status polling.
   - Evidence: enum [main.js:76](/home/kamau/MEGA/routerfx-dev/main.js:76); polling code [main.js:1995](/home/kamau/MEGA/routerfx-dev/main.js:1995)-[main.js:2013](/home/kamau/MEGA/routerfx-dev/main.js:2013); HAR many entries.
4. `133` (`ROUTER_INFO`)
   - Likely purpose: router/device info fetch.
   - Evidence: enum [main.js:85](/home/kamau/MEGA/routerfx-dev/main.js:85); requester [main.js:2308](/home/kamau/MEGA/routerfx-dev/main.js:2308)-[main.js:2314](/home/kamau/MEGA/routerfx-dev/main.js:2314); HAR `1,2,6,7,11,13,16,18`.
5. `134` (`QUERY_SIM_STATUS`)
   - Likely purpose: SIM PIN/PUK lock state.
   - Evidence: enum [main.js:86](/home/kamau/MEGA/routerfx-dev/main.js:86); HAR `32` response keys `lock_pin_flag`, `puk_left_times`.
6. `205` (`SYS_INFO_NETWORK`)
   - Likely purpose: dashboard network/radio metrics.
   - Evidence: enum [main.js:108](/home/kamau/MEGA/routerfx-dev/main.js:108); HAR `43` detailed signal/cell metrics.
7. `232` (`GET_NEXT_LOGIN_TIME`)
   - Likely purpose: login timing/token endpoint.
   - Evidence: enum [main.js:135](/home/kamau/MEGA/routerfx-dev/main.js:135); called before login [login.js:133](/home/kamau/MEGA/routerfx-dev/login.js:133)-[login.js:138](/home/kamau/MEGA/routerfx-dev/login.js:138); HAR `20`.
8. `233` (`RESET_LOGIN_TIME`)
   - Likely purpose: issue/refresh token and login timer state.
   - Evidence: enum [main.js:136](/home/kamau/MEGA/routerfx-dev/main.js:136); `resetTime()` stores `e.token` at [main.js:2365](/home/kamau/MEGA/routerfx-dev/main.js:2365)-[main.js:2373](/home/kamau/MEGA/routerfx-dev/main.js:2373); HAR `35`,`39`.
9. `269` (`SET_HASH`)
   - Likely purpose: set current page hash/state server-side.
   - Evidence: enum [main.js:169](/home/kamau/MEGA/routerfx-dev/main.js:169); `Page.setHash` implementation [main.js:1605](/home/kamau/MEGA/routerfx-dev/main.js:1605)-[main.js:1613](/home/kamau/MEGA/routerfx-dev/main.js:1613); HAR `37`,`40` with `setHash:"#WAN_INFO#0"`.
10. `394` (`GET_STATUS_BAR`)
   - Likely purpose: top/status bar snapshot.
   - Evidence: enum [main.js:238](/home/kamau/MEGA/routerfx-dev/main.js:238); HAR `30`,`44` response has status-bar fields (`sms_unread`, signal/network flags).

Other important command IDs from source (not all present in this HAR):

1. Wi-Fi: `2` (`WIRELESS_CONFIG`), `132` (`WPS_CONFIG`), `209` (`WIFI_INFO`), `210` (`WIFI5G_INFO`), `211` (`WIRELESS5G_CONFIG`), `230`, `231`, `224`, `225`, `278`.
2. Clients: `223` (`DHCPCLIENT_INFO`), `164` (`ROUTER_TABLE`).
3. SMS: `12` (`SMS_INFO`), `13` (`SMS_SEND`), `14` (`SMS_DELETE`), `15` (`SMS_DELETE_ALL`), `16` (`SMS_SETTING`), `125`, `126`.
4. Reboot: `6` (`SYS_REBOOT`), `99` (`REBOOT`).
5. APN: `213` (`APN_CONFIG`), `130` (`MULTI_APN_INFO`), `248`.
6. Network mode: `218` (`NETWORKSET_MODE`), `277` (`NETWORKMODE_SWITCH`).

Evidence: command enum in [main.js:27](/home/kamau/MEGA/routerfx-dev/main.js:27)-[main.js:307](/home/kamau/MEGA/routerfx-dev/main.js:307).

## Minimum required headers and cookies

What HAR proves:

1. Cookies:
   - No cookie request/response usage observed in HAR for CGI calls.
2. Body format:
   - Router expects JSON text body sent over `POST /cgi-bin/http.cgi` (all observed calls).
3. Content type used by browser:
   - `Content-Type: application/x-www-form-urlencoded; charset=UTF-8`
   - Body is still raw JSON string (not form key/value pairs), as seen in HAR request bodies.
4. Other repeated headers:
   - `X-Requested-With`, `Referer`, `Origin`, `User-Agent`, etc. appear consistently.

What is unknown from this evidence set:

1. Strict minimum header set required by firmware is **unknown** (HAR shows success with browser headers but does not include negative tests).
2. Whether `Referer`, `Origin`, `X-Requested-With`, UA are enforced is **unknown**.

## cmd 205 schema

Schema grouped by logical domains from HAR `43` response:

1. Envelope:
   - `success`, `cmd`
2. Radio quality (4G):
   - `RSRP`, `RSSI`, `RSRQ`, `SINR`, `CQI`, `dl_mcs`, `ul_mcs`, `rank_4g`, `bler_4g`
3. Radio quality (5G counterparts):
   - `RSRP_5G`, `RSSI_5G`, `RSRQ_5G`, `SINR_5G`, `CQI_5G`, `dl_mcs_5g`, `ul_mcs_5g`, `rank_5g`, `bler_5g`
4. Cell identity/frequency:
   - `PLMN`, `PCI`, `FREQ`, `ENODEBID`, `CELL_ID`, `ECGI`, `tac_4g`
5. Band/capability:
   - `currentband`, `bandwidth`, `ul64qam_support`, `dl256qam_support`, `max_ul_qam`, `max_dl_qam`, `max_ul_qam_5g`, `max_dl_qam_5g`
6. Traffic/session:
   - `flow_dl`, `flow_ul`, `mon_total_flow`, `onlineTime`, `onlineDuration`, `dial_time`
7. CA/NR extras:
   - `NCGI`, `ca_freq`, `ca_pci`, `ca_rsrp`, `ca_rsrq`, `ca_rssi`, `ca_state`, `lte_dl_cc`, `nr_dl_cc`, `ca_bandwidth`, `CA_COMBINATION`
8. 3G legacy:
   - `rscp`, `rssi3G`, `ecio`, `ecno`, `cellId3G`, `uarfcn`, `psc`, `currentband3G`, `bandwidth3G`
9. Additional status/meta:
   - `signal_lvl`, `network_type_str`, `network_operator`, `current_real_wan_prio`, `first_mode`, `systime`, `network_redcap`, `mimo_status`

## Minimal replay flow

Minimum non-browser flow to authenticate and fetch `cmd:205` (evidence-backed):

1. Send `cmd:232` with empty `sessionId`:
   - `{"cmd":232,"method":"GET","sessionId":"","language":"en"}`
2. Read challenge token from `cmd:232` response:
   - login JS reads `n.token` at [login.js:138](/home/kamau/MEGA/routerfx-dev/login.js:138).
3. Compute login payload:
   - `sessionId = Md5.md5(Math.random().toString()) + Md5.md5(Math.random().toString())` [login.js:156](/home/kamau/MEGA/routerfx-dev/login.js:156)
   - `passwd = sha256_digest(token + plainPassword)` [login.js:158](/home/kamau/MEGA/routerfx-dev/login.js:158), [sha256.js:129](/home/kamau/MEGA/routerfx-dev/sha256.js:129)-[sha256.js:134](/home/kamau/MEGA/routerfx-dev/sha256.js:134)
4. Send `cmd:100`:
   - HAR `21` request body confirms required fields.
5. Capture authenticated session id from login response and store for next requests:
   - JS expects `t.sessionId` and persists it [login.js:167](/home/kamau/MEGA/routerfx-dev/login.js:167).
6. Call `cmd:205` with authenticated body `sessionId`:
   - HAR `43` succeeds with body `{"cmd":205,"method":"GET","language":"en","sessionId":"<auth_sid>"}`.

Unknown in this HAR:

1. Raw login response payload text for HAR `21` is absent in exported HAR content; field-level response body proof is therefore **unknown** from HAR alone, but JS expectation is explicit.

## Java client design

Minimal Java client for login + `cmd:205` only:

1. `RouterTransport`
   - `String postJson(String path, String jsonBody, Map<String,String> headers)`
   - Keeps HTTP/header concerns isolated.
2. `AuthService`
   - `Challenge getLoginChallenge()` for `cmd:232`
   - `Session login(String username, String password)` for `cmd:100`
   - Implements:
     - random login `sessionId` generation formula from [login.js:156](/home/kamau/MEGA/routerfx-dev/login.js:156)
     - password transform from [login.js:158](/home/kamau/MEGA/routerfx-dev/login.js:158) + [sha256.js:129](/home/kamau/MEGA/routerfx-dev/sha256.js:129)
3. `NetworkService`
   - `Cmd205Response getSysInfoNetwork(String sessionId)` for `cmd:205`
4. Domain models
   - `Challenge{token}`
   - `Session{sessionId, auth, userLevel}` (fields beyond `sessionId` optional if returned)
   - `Cmd205Response` grouped by domains above.
5. Error model
   - Parse router JSON envelope (`success`, `message`, `cmd`) and return typed `Result.Failure` values at the router boundary.

Evidence for boundary separation:
- Commands and meanings in [main.js:27](/home/kamau/MEGA/routerfx-dev/main.js:27)-[main.js:307](/home/kamau/MEGA/routerfx-dev/main.js:307).
- Login path in [login.js:130](/home/kamau/MEGA/routerfx-dev/login.js:130)-[login.js:170](/home/kamau/MEGA/routerfx-dev/login.js:170).

## Risks and unknowns

1. Firmware behavior drift risk:
   - Command semantics and required fields may change across firmware; enum breadth in [main.js:27](/home/kamau/MEGA/routerfx-dev/main.js:27)-[main.js:307](/home/kamau/MEGA/routerfx-dev/main.js:307) indicates many model/feature variants.
2. Header enforcement uncertainty:
   - HAR confirms what browser sends, not what firmware minimally requires.
3. Missing raw payloads in this HAR export:
   - `cmd:100` response text is absent though response size is non-zero (HAR `21`), so exact raw login response body cannot be quoted from HAR.
4. Token/session policy uncertainty beyond observed path:
   - POST token behavior is shown in wrapper; full server-side validation rules are unknown without negative tests.

## Appendix: raw evidence

### A. Key JS code path for `cmd:100`

1. Login click handler starts `cmd:232`:
   - [login.js:130](/home/kamau/MEGA/routerfx-dev/login.js:130)-[login.js:137](/home/kamau/MEGA/routerfx-dev/login.js:137)
2. Capture token:
   - [login.js:138](/home/kamau/MEGA/routerfx-dev/login.js:138)
3. Build `cmd:100` payload:
   - [login.js:153](/home/kamau/MEGA/routerfx-dev/login.js:153)-[login.js:160](/home/kamau/MEGA/routerfx-dev/login.js:160)
4. Persist authenticated `sessionId`:
   - [login.js:167](/home/kamau/MEGA/routerfx-dev/login.js:167)

### B. SHA-256 implementation evidence

1. `sha256_digest` definition:
   - [sha256.js:129](/home/kamau/MEGA/routerfx-dev/sha256.js:129)-[sha256.js:134](/home/kamau/MEGA/routerfx-dev/sha256.js:134)
2. Hex output generation:
   - [sha256.js:123](/home/kamau/MEGA/routerfx-dev/sha256.js:123)-[sha256.js:127](/home/kamau/MEGA/routerfx-dev/sha256.js:127)

### C. HAR request/response snippets (selected)

1. HAR `20` request body (`cmd:232`):
   - `{"cmd":232,"method":"GET","sessionId":"","language":"en"}`
2. HAR `21` request body (`cmd:100`):
   - `{"cmd":100,"method":"POST","sessionId":"439ffff1d15a47497855b82c771852b3a55af43a5bf03843f2564f13e47f74b2","username":"admin","passwd":"fc1d509c3547f87df284852ddfe2fd0819fb511b494142bc2e98ebcb34c0f418","isAutoUpgrade":"0","language":"en"}`
3. HAR `35` response body (`cmd:233`):
   - `{"success":true,"cmd":233,"token":"2fe6fb9a24ebca77734e0fdaae0199be"}`
4. HAR `40` request body (`cmd:269` with token):
   - `{"cmd":269,"method":"POST","setHash":"#WAN_INFO#0","token":"2fe6fb9a24ebca77734e0fdaae0199be","language":"en","sessionId":"9a86cd1b7e47060df9c4c25d82286cdd7f123ca826b106f34295a32e046a8532"}`
5. HAR `43` request body (`cmd:205`):
   - `{"cmd":205,"method":"GET","language":"en","sessionId":"9a86cd1b7e47060df9c4c25d82286cdd7f123ca826b106f34295a32e046a8532"}`
6. HAR `43` response body (`cmd:205`):
   - JSON object with `success:true`, `cmd:205`, and network/radio metrics (`RSRP`, `RSSI`, `RSRQ`, `SINR`, `PLMN`, `flow_dl`, `onlineTime`, etc.).
