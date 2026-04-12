#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

MVN_BASE=(mvn -B -ntp)
ORIGINAL_HOME="${HOME:-}"

echo "Checking workflow files"
if command -v actionlint >/dev/null 2>&1; then
  actionlint .github/workflows/*.yml
else
  echo "actionlint not found, running YAML parse fallback"
  python3 - <<'PY'
import pathlib
import sys
try:
    import yaml
except Exception as err:
    print(f"PyYAML unavailable: {err}")
    sys.exit(0)
for path in pathlib.Path('.github/workflows').glob('*.yml'):
    with path.open('r', encoding='utf-8') as stream:
        yaml.safe_load(stream)
print('Workflow YAML parse completed')
PY
fi

echo "Running Maven verification for CI pipeline"
"${MVN_BASE[@]}" -Punit-tests verify

echo "Running Maven UI test profile command"
if rg -n '@Tag\("ui"\)' src/test >/dev/null 2>&1; then
  UI_HOME="/tmp/routerfx-home"
  UI_MAVEN_OPTS="${MAVEN_OPTS:-}"
  echo "Preparing isolated JavaFX test home at $UI_HOME"
  mkdir -p "$UI_HOME/.openjfx"
  find "$UI_HOME/.openjfx" -name ".lock" -delete
  if [[ -n "$ORIGINAL_HOME" ]]; then
    UI_MAVEN_OPTS="$UI_MAVEN_OPTS -Dmaven.repo.local=$ORIGINAL_HOME/.m2/repository"
  fi
  HOME="$UI_HOME" \
  JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS:-} -Duser.home=$UI_HOME" \
  MAVEN_OPTS="$UI_MAVEN_OPTS" \
  "${MVN_BASE[@]}" -Pui-tests test
else
  echo "No UI-tagged tests found, skipping UI profile step"
fi
