# RouterFX

RouterFX is a native Java desktop application for interacting directly with LTE/5G router firmware through its internal command interface.

Instead of automating the browser-based web UI, RouterFX talks to the router’s backend protocol (`/cgi-bin/http.cgi`) and exposes network status, radio metrics, and device configuration through a structured Java API and JavaFX interface.

The project began as a protocol reconstruction exercise and is designed to grow into a lightweight cross-router management client.