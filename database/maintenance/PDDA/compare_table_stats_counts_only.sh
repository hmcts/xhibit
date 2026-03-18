#!/bin/bash

ORACLE_FILE="oracle_table_stats.csv"
POSTGRES_FILE="postgres_table_stats.csv"
REPORT_FILE="comparison_report_counts_only.csv"
MISMATCH_FILE="mismatches.csv"

# Temporary files
ORACLE_CLEAN="oracle_clean.csv"
POSTGRES_CLEAN="postgres_clean.csv"
ORACLE_SORTED="oracle_sorted.csv"
POSTGRES_SORTED="postgres_sorted.csv"

# Check required files exist and are readable
for file in "$ORACLE_FILE" "$POSTGRES_FILE"; do
  if [ ! -f "$file" ]; then
    echo "ERROR: File not found: $file"
    exit 1
  fi
  if [ ! -r "$file" ]; then
    echo "ERROR: File not readable: $file"
    exit 1
  fi
done

# Prepare input by removing malformed rows (must have 3 columns)
egrep '^[^,]+,[^,]+,[^,]+$' "$ORACLE_FILE" > "$ORACLE_CLEAN"
egrep '^[^,]+,[^,]+,[^,]+$' "$POSTGRES_FILE" > "$POSTGRES_CLEAN"

# Sort inputs
sort "$ORACLE_CLEAN" > "$ORACLE_SORTED"
sort "$POSTGRES_CLEAN" > "$POSTGRES_SORTED"

# Write headers
echo "TABLE_NAME,ORACLE_COUNT,POSTGRES_COUNT,MATCH_STATUS" > "$REPORT_FILE"
echo "TABLE_NAME,ORACLE_COUNT,POSTGRES_COUNT" > "$MISMATCH_FILE"

# Do comparison
nawk -F, -v mismatch_file="$MISMATCH_FILE" '
  FILENAME == ARGV[1] {
    oracle_count[$1] = $2
    next
  }
  FILENAME == ARGV[2] {
    table    = $1
    pg_count = $2
    oc = (table in oracle_count) ? oracle_count[table] : "N/A"
    status = (oc == pg_count) ? "MATCH" : "MISMATCH"
    print table "," oc "," pg_count "," status

    if (status == "MISMATCH") {
      print table "," oc "," pg_count >> mismatch_file
      mismatch_count++
    }
  }
  END {
    print "" >> "'"$REPORT_FILE"'"
    print "Summary: " mismatch_count " mismatch(es) found." >> "'"$REPORT_FILE"'"
  }
' "$ORACLE_SORTED" "$POSTGRES_SORTED" >> "$REPORT_FILE"

# Cleanup
rm -f "$ORACLE_CLEAN" "$POSTGRES_CLEAN" "$ORACLE_SORTED" "$POSTGRES_SORTED"

echo "Comparison complete."
echo "Full report: $REPORT_FILE"
echo "Mismatches: $MISMATCH_FILE (if any)"
