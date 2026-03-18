#!/bin/bash

ORACLE_FILE="oracle_table_stats.csv"
POSTGRES_FILE="postgres_table_stats.csv"
REPORT_FILE="comparison_report.csv"
MISMATCH_FILE="mismatches.csv"

# Temp files
ORACLE_CLEAN="oracle_clean.csv"
POSTGRES_CLEAN="postgres_clean.csv"
ORACLE_SORTED="oracle_sorted.csv"
POSTGRES_SORTED="postgres_sorted.csv"

# Validate input files
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

# Strip invalid lines (must have 3 comma-separated fields)
egrep '^[^,]+,[^,]+,[^,]+$' "$ORACLE_FILE" > "$ORACLE_CLEAN"
egrep '^[^,]+,[^,]+,[^,]+$' "$POSTGRES_FILE" > "$POSTGRES_CLEAN"

# Sort files
sort "$ORACLE_CLEAN" > "$ORACLE_SORTED"
sort "$POSTGRES_CLEAN" > "$POSTGRES_SORTED"

# Write headers
echo "TABLE_NAME,ORACLE_COUNT,POSTGRES_COUNT,ORACLE_LAST_UPDATE,POSTGRES_LAST_UPDATE,MATCH_STATUS" > "$REPORT_FILE"
echo "TABLE_NAME,ORACLE_COUNT,POSTGRES_COUNT,ORACLE_LAST_UPDATE,POSTGRES_LAST_UPDATE" > "$MISMATCH_FILE"

# Run comparison
nawk -F, -v mismatch_file="$MISMATCH_FILE" '
  FILENAME == ARGV[1] {
    oracle_count[$1] = $2
    oracle_date[$1]  = $3
    next
  }
  FILENAME == ARGV[2] {
    table    = $1
    pg_count = $2
    pg_date  = $3

    oc = (table in oracle_count) ? oracle_count[table] : "N/A"
    od = (table in oracle_date)  ? oracle_date[table]  : "N/A"

    status = "MATCH"
    if (oc != pg_count || od != pg_date) {
      status = "MISMATCH"
      print table "," oc "," pg_count "," od "," pg_date >> mismatch_file
      mismatch_count++
    }

    print table "," oc "," pg_count "," od "," pg_date "," status
  }
  END {
    print "" >> "'"$REPORT_FILE"'"
    print "Summary: " mismatch_count " mismatch(es) found." >> "'"$REPORT_FILE"'"
  }
' "$ORACLE_SORTED" "$POSTGRES_SORTED" >> "$REPORT_FILE"

# Cleanup temp files
rm -f "$ORACLE_CLEAN" "$POSTGRES_CLEAN" "$ORACLE_SORTED" "$POSTGRES_SORTED"

# Final message
echo "Full comparison complete."
echo "Full report: $REPORT_FILE"
echo "Mismatches only: $MISMATCH_FILE (if any)"
