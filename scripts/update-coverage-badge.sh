#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
CSV_FILE="$ROOT_DIR/target/site/jacoco/jacoco.csv"
INSTR_BADGE_FILE="$ROOT_DIR/.github/badges/jacoco.svg"
BRANCH_BADGE_FILE="$ROOT_DIR/.github/badges/jacoco-branches.svg"

if [[ ! -f "$CSV_FILE" ]]; then
  echo "JaCoCo CSV not found at $CSV_FILE"
  exit 1
fi

mkdir -p "$(dirname "$INSTR_BADGE_FILE")"

python3 - "$CSV_FILE" "$INSTR_BADGE_FILE" "$BRANCH_BADGE_FILE" <<'PY'
import csv
import sys

csv_path = sys.argv[1]
instruction_badge_path = sys.argv[2]
branch_badge_path = sys.argv[3]

instruction_missed = 0
instruction_covered = 0
branch_missed = 0
branch_covered = 0
with open(csv_path, newline='', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    for row in reader:
        instruction_missed += int(row['INSTRUCTION_MISSED'])
        instruction_covered += int(row['INSTRUCTION_COVERED'])
        branch_missed += int(row['BRANCH_MISSED'])
        branch_covered += int(row['BRANCH_COVERED'])

def color_for(percent):
    if percent >= 90:
        return '#4c1'
    if percent >= 80:
        return '#97CA00'
    if percent >= 70:
        return '#a4a61d'
    if percent >= 60:
        return '#dfb317'
    if percent >= 50:
        return '#fe7d37'
    return '#e05d44'

def make_badge(label, percent):
    message = f"{percent:.1f}%"
    color = color_for(percent)
    label_width = max(54, 7 * len(label) + 10)
    message_width = max(46, 7 * len(message) + 10)
    width = label_width + message_width
    label_x = label_width / 2
    message_x = label_width + message_width / 2
    return f'''<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="20" role="img" aria-label="{label}: {message}">
  <title>{label}: {message}</title>
  <linearGradient id="g" x2="0" y2="100%">
    <stop offset="0" stop-color="#fff" stop-opacity=".7"/>
    <stop offset=".1" stop-color="#aaa" stop-opacity=".1"/>
    <stop offset=".9" stop-opacity=".3"/>
    <stop offset="1" stop-opacity=".5"/>
  </linearGradient>
  <clipPath id="r">
    <rect width="{width}" height="20" rx="3" fill="#fff"/>
  </clipPath>
  <g clip-path="url(#r)">
    <rect width="{label_width}" height="20" fill="#555"/>
    <rect x="{label_width}" width="{message_width}" height="20" fill="{color}"/>
    <rect width="{width}" height="20" fill="url(#g)"/>
  </g>
  <g fill="#fff" text-anchor="middle" font-family="DejaVu Sans,Verdana,Geneva,sans-serif" font-size="11">
    <text x="{label_x}" y="15" fill="#010101" fill-opacity=".3">{label}</text>
    <text x="{label_x}" y="14">{label}</text>
    <text x="{message_x}" y="15" fill="#010101" fill-opacity=".3">{message}</text>
    <text x="{message_x}" y="14">{message}</text>
  </g>
</svg>
'''

inst_total = instruction_missed + instruction_covered
inst_percent = 0.0 if inst_total == 0 else (instruction_covered * 100.0 / inst_total)
branch_total = branch_missed + branch_covered
branch_percent = 0.0 if branch_total == 0 else (branch_covered * 100.0 / branch_total)

with open(instruction_badge_path, 'w', encoding='utf-8') as f:
    f.write(make_badge('coverage', inst_percent))
with open(branch_badge_path, 'w', encoding='utf-8') as f:
    f.write(make_badge('branches', branch_percent))
PY

echo "Updated coverage badges at $INSTR_BADGE_FILE and $BRANCH_BADGE_FILE"
