# RouterFX

**JaCoCo Test Coverage**: ![JaCoCo Coverage](https://raw.githubusercontent.com/IdelsTak/routerfx/main/.github/badges/jacoco.svg) ![JaCoCo Branch Coverage](https://raw.githubusercontent.com/IdelsTak/routerfx/main/.github/badges/jacoco-branches.svg)

RouterFX is a native Java desktop application for interacting directly with LTE/5G router firmware through its internal command interface.

Instead of automating the browser-based web UI, RouterFX talks to the router’s backend protocol (`/cgi-bin/http.cgi`) and exposes network status, radio metrics, and device configuration through a structured Java API and JavaFX interface.

The project began as a protocol reconstruction exercise and is designed to grow into a lightweight cross-router management client.

## Documentation map

- Primary architecture guide: [`docs/javafx-unified-architecture.md`](docs/javafx-unified-architecture.md)
- MVU implementation reference: [`docs/mvu-architecture.md`](docs/mvu-architecture.md)
- Router protocol evidence: [`docs/airtel-router-protocol-analysis.md`](docs/airtel-router-protocol-analysis.md)
- Minimal Java protocol client details: [`docs/minimal-java-proof-client-details.md`](docs/minimal-java-proof-client-details.md)
