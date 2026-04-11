# Minimal Java Proof Client Details (HAR + JS Only)

Implementation context: [RouterFX JavaFX App Unified Architecture](./javafx-unified-architecture.md)
Implementation note: code packages are now split by capability (`shell.app`, `router.protocol`, `shared.value`, plus feature slices), while this document remains protocol evidence for minimal login and dashboard-read behavior.

## 1. Minimal login contract

### `cmd:232` (challenge)

Request (from HAR `20`):

```json
{"cmd":232,"method":"GET","sessionId":"","language":"en"}
```

Response body shape:

1. **Raw full JSON is unknown in this HAR export**.
   - HAR `20` has `response.content.size = 109` and `mimeType = application/json`, but `response.content.text` is empty.
2. Fields required by JS call sites:
   - `token` is read for login hashing at [login.js:138](/home/kamau/MEGA/routerfx-dev/login.js:138).
   - `buffer` and `netx_login_time` are read in lockout text path at [login.js:298](/home/kamau/MEGA/routerfx-dev/login.js:298)-[login.js:300](/home/kamau/MEGA/routerfx-dev/login.js:300).

### `cmd:100` (login)

Request construction path:

1. Start from login click handler [login.js:130](/home/kamau/MEGA/routerfx-dev/login.js:130).
2. Fetch `cmd:232` token [login.js:133](/home/kamau/MEGA/routerfx-dev/login.js:133)-[login.js:138](/home/kamau/MEGA/routerfx-dev/login.js:138).
3. Build and send `cmd:100` [login.js:153](/home/kamau/MEGA/routerfx-dev/login.js:153)-[login.js:160](/home/kamau/MEGA/routerfx-dev/login.js:160).

HAR `21` request body:

```json
{"cmd":100,"method":"POST","sessionId":"439ffff1d15a47497855b82c771852b3a55af43a5bf03843f2564f13e47f74b2","username":"admin","passwd":"fc1d509c3547f87df284852ddfe2fd0819fb511b494142bc2e98ebcb34c0f418","isAutoUpgrade":"0","language":"en"}
```

Success condition in JS before redirect:

1. Failure branch is entered only when:
   - `t.login_fail == "fail"` OR `t.login_fail2 == "fail"` ([login.js:162](/home/kamau/MEGA/routerfx-dev/login.js:162)).
2. Redirect branch runs otherwise (the `else`):
   - sets `sessionStorage.sessionId = t.sessionId`
   - sets `sessionStorage.canLogin = t.AUTH`
   - sets `sessionStorage.level = t.user_level`
   - redirects to `index.html`
   - [login.js:166](/home/kamau/MEGA/routerfx-dev/login.js:166)-[login.js:170](/home/kamau/MEGA/routerfx-dev/login.js:170)
3. `t.success` is not used as a gate; it is only referenced as an expression in the success branch (`t.success, ...`) [login.js:166](/home/kamau/MEGA/routerfx-dev/login.js:166).

### `cmd:205` (first dashboard data)

HAR `43` request body:

```json
{"cmd":205,"method":"GET","language":"en","sessionId":"9a86cd1b7e47060df9c4c25d82286cdd7f123ca826b106f34295a32e046a8532"}
```

HAR `43` response body is valid JSON envelope:

```json
{"success":true,"cmd":205,...}
```

with many network metrics fields (full list in section 7).

## 2. Required request fields

Evidence-backed minimum for these 3 commands:

1. `232`:
   - `cmd`, `method`, `sessionId` (empty), `language` (HAR `20`).
2. `100`:
   - `cmd`, `method`, `sessionId` (pre-login generated), `username`, `passwd`, `isAutoUpgrade`, `language` (HAR `21`, login builder lines [login.js:153](/home/kamau/MEGA/routerfx-dev/login.js:153)-[login.js:160](/home/kamau/MEGA/routerfx-dev/login.js:160)).
3. `205`:
   - `cmd`, `method`, `language`, authenticated `sessionId` (HAR `43`).

Wrapper defaults affecting payload:

1. If missing, wrapper sets `method = GET` [main.js:1448](/home/kamau/MEGA/routerfx-dev/main.js:1448).
2. If missing, wrapper sets `language = Page.language` [main.js:1451](/home/kamau/MEGA/routerfx-dev/main.js:1451).
3. If missing, wrapper sets `sessionId = sessionStorage.sessionId || ""` [main.js:1452](/home/kamau/MEGA/routerfx-dev/main.js:1452).
4. For `POST` methods only, wrapper injects `token = sessionStorage.token` [main.js:1449](/home/kamau/MEGA/routerfx-dev/main.js:1449)-[main.js:1450](/home/kamau/MEGA/routerfx-dev/main.js:1450).

No other default JSON fields are added by wrapper.

## 3. Required response fields

### For `232`

1. Required by login path:
   - `token` [login.js:138](/home/kamau/MEGA/routerfx-dev/login.js:138)
2. Also consumed in lockout display path:
   - `buffer`, `netx_login_time` [login.js:298](/home/kamau/MEGA/routerfx-dev/login.js:298)-[login.js:300](/home/kamau/MEGA/routerfx-dev/login.js:300)
3. Full raw field set: **unknown** (HAR omits body text).

### For `100`

1. Used for failure handling:
   - `login_fail`, `login_fail2`, `login_times`, `login_time` [login.js:162](/home/kamau/MEGA/routerfx-dev/login.js:162)-[login.js:166](/home/kamau/MEGA/routerfx-dev/login.js:166)
2. Used for success continuation:
   - `sessionId`, `AUTH`, `user_level` [login.js:167](/home/kamau/MEGA/routerfx-dev/login.js:167)-[login.js:169](/home/kamau/MEGA/routerfx-dev/login.js:169)
3. Full raw `cmd:100` response JSON from HAR: **unknown** (HAR `21` has empty `content.text`).

### For `205`

1. Envelope expected by wrapper:
   - `cmd`, `success` [main.js:1485](/home/kamau/MEGA/routerfx-dev/main.js:1485)-[main.js:1489](/home/kamau/MEGA/routerfx-dev/main.js:1489)
2. Domain payload:
   - full observed field list in section 7 from HAR `43`.

## 4. Session lifecycle

1. Pre-login generated session id:
   - `Md5.md5(Math.random().toString()) + Md5.md5(Math.random().toString())` [login.js:156](/home/kamau/MEGA/routerfx-dev/login.js:156)
2. Encoding/length evidence:
   - HAR `21` pre-login `sessionId` is 64 lowercase hex chars (`439f...74b2`), matching two MD5 hex strings concatenated.
3. After login, JS replaces active session with response value:
   - `sessionStorage.sessionId = t.sessionId` [login.js:167](/home/kamau/MEGA/routerfx-dev/login.js:167)
4. Subsequent requests use returned session id:
   - HAR `21` sent `439f...74b2`
   - first authenticated request HAR `22` uses `9a86...8532`
   - `205` HAR `43` also uses `9a86...8532`
5. Later replacement value:
   - No evidence of replacing `sessionId` again in this trace.
   - Therefore: keep using login response `sessionId` (until session expiry / auth error).

## 5. Header advice

Using JS wrapper and HAR only:

1. Wrapper does not explicitly set custom headers in `$.ajax` call [main.js:1459](/home/kamau/MEGA/routerfx-dev/main.js:1459)-[main.js:1465](/home/kamau/MEGA/routerfx-dev/main.js:1465).
2. HAR shows browser sending:
   - `Content-Type: application/x-www-form-urlencoded; charset=UTF-8`
   - `X-Requested-With: XMLHttpRequest`
   - `Referer`, `Origin`, `User-Agent`
3. Necessity verdict from this evidence:
   - `Content-Type`: **likely needed** for compatibility with observed behavior (present on every request in HAR).
   - `X-Requested-With`: **unknown if required** (present in HAR, not set explicitly by wrapper code).
   - `Referer`: **unknown if required**.
   - `Origin`: **unknown if required**.
   - `User-Agent`: **unknown if required**.

No JS or HAR evidence proves server-side rejection without these headers.

## 6. Error handling notes

Wrapper-level envelope handling:

1. Parses text response into JSON via first `{` [main.js:1016](/home/kamau/MEGA/routerfx-dev/main.js:1016)-[main.js:1020](/home/kamau/MEGA/routerfx-dev/main.js:1020).
2. If `t.success` true => success callback [main.js:1487](/home/kamau/MEGA/routerfx-dev/main.js:1487)-[main.js:1489](/home/kamau/MEGA/routerfx-dev/main.js:1489).
3. If not success and `message` is `NO_AUTH` or `LOGIN_TIMEOUT`:
   - clear session, redirect to login [main.js:1490](/home/kamau/MEGA/routerfx-dev/main.js:1490)-[main.js:1495](/home/kamau/MEGA/routerfx-dev/main.js:1495).
4. Login-specific fail signaling:
   - uses `login_fail`/`login_fail2` checks (not wrapper `success`) [login.js:162](/home/kamau/MEGA/routerfx-dev/login.js:162).

Additional numeric error message mapping exists (`500..507`) at [main.js:2393](/home/kamau/MEGA/routerfx-dev/main.js:2393)-[main.js:2422](/home/kamau/MEGA/routerfx-dev/main.js:2422), but no direct HAR evidence ties these to login in this capture.

## 7. cmd 205 stable schema

From HAR `43`, one observed response includes all keys below.  
Across firmware/modes, global always/optional guarantees are **unknown** from one trace.

Practical split by observed behavior in this response:

1. Present with non-empty values in this sample:
   - `success`, `cmd`, `RSRP`, `RSSI`, `RSRQ`, `SINR`, `PCI`, `FREQ`, `ENODEBID`, `CELL_ID`, `CQI`, `dl_mcs`, `ul_mcs`, `PLMN`, `signal_lvl`, `bandwidth`, `ECGI`, `network_type_str`, `network_operator`, `currentband`, `ul64qam_support`, `dl256qam_support`, `max_ul_qam`, `max_dl_qam`, `rank_4g`, `flow_dl`, `flow_ul`, `bler_4g`, `mon_total_flow`, `current_real_wan_prio`, `onlineTime`, `onlineDuration`, `rscp`, `rssi3G`, `ecio`, `cellId3G`, `psc`, `tac_4g`, `bandwidth3G`, `dial_time`, `ecno`, `first_mode`, `systime`.
2. Present but empty / placeholder in this sample (mode-specific or unavailable):
   - `RSRP_5G`, `RSSI_5G`, `RSRQ_5G`, `SINR_5G`, `PCI_5G`, `FREQ_5G`, `ENODEBID_5G`, `CELL_ID_5G`, `CQI_5G`, `bandwidth_5g`, `currentband_5g`, `dl_mcs_5g`, `ul_mcs_5g`, `max_ul_qam_5g`, `max_dl_qam_5g`, `rank_5g`, `bler_5g`, `NCGI`, `ca_freq`, `ca_pci`, `ca_rsrp`, `ca_rsrq`, `ca_rssi`, `ca_state`, `lte_dl_cc`, `nr_dl_cc`, `ca_bandwidth`, `uarfcn`, `currentband3G`, `network_redcap`, `mimo_status`, `CA_COMBINATION`.

For a minimal Java client, safest required parse set is envelope (`success`,`cmd`) plus only fields you actively need.

## 8. Evidence appendix

### HAR evidence (entry indexes)

1. `cmd:232` request: HAR `20` request body `{"cmd":232,"method":"GET","sessionId":"","language":"en"}`.
2. `cmd:100` request: HAR `21` request body with hashed password and pre-login session id.
3. Session switch evidence:
   - HAR `21` sessionId `439f...74b2`
   - HAR `22`/`43` sessionId `9a86...8532`
4. `cmd:205` request/response: HAR `43`.
5. `cmd:233` token response:
   - HAR `35`/`39`: `{"success":true,"cmd":233,"token":"2fe6...99be"}`
6. `cmd:269` token usage:
   - HAR `40` request includes `"token":"2fe6...99be"`
7. Cookies:
   - no cookie headers or set-cookie headers in HAR entries.

### JS source evidence

1. Login flow and success/failure gates:
   - [login.js:130](/home/kamau/MEGA/routerfx-dev/login.js:130)-[login.js:180](/home/kamau/MEGA/routerfx-dev/login.js:180)
2. Password formula:
   - [login.js:158](/home/kamau/MEGA/routerfx-dev/login.js:158)
   - [sha256.js:129](/home/kamau/MEGA/routerfx-dev/sha256.js:129)-[sha256.js:134](/home/kamau/MEGA/routerfx-dev/sha256.js:134)
3. Pre-login sessionId generation:
   - [login.js:156](/home/kamau/MEGA/routerfx-dev/login.js:156)
4. Wrapper defaults and token injection:
   - [main.js:1448](/home/kamau/MEGA/routerfx-dev/main.js:1448)-[main.js:1453](/home/kamau/MEGA/routerfx-dev/main.js:1453)
5. Wrapper success/error envelope behavior:
   - [main.js:1485](/home/kamau/MEGA/routerfx-dev/main.js:1485)-[main.js:1501](/home/kamau/MEGA/routerfx-dev/main.js:1501)
6. Session timeout handling:
   - [main.js:1490](/home/kamau/MEGA/routerfx-dev/main.js:1490)-[main.js:1495](/home/kamau/MEGA/routerfx-dev/main.js:1495)
