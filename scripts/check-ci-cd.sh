#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

MAVEN_LOCAL_REPO="$(pwd)/.m2/repository"
mkdir -p "$MAVEN_LOCAL_REPO"
MVN_BASE=(mvn -B -ntp -Dmaven.repo.local="$MAVEN_LOCAL_REPO")

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
xvfb-run --auto-servernum --server-args='-screen 0 1920x1080x24' "${MVN_BASE[@]}" -Pui-tests test
