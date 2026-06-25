#!/bin/bash

###################################################
#
# Name: caseMigrate.sh
#
# Created: April 2026
#
# Purpose: Takes in a list of cases to migrate via CSV file and sets them as migrated. The output of each record is logged to a log file.
#
#
# How to use: You need to pass the following arguments into this script:
#			- 1st argument - input.csv (The input file containing the list of cases to migrate)
#			- 2nd argument - log.txt (The text file all the logs will be written to. This will show the status of processing for each record)
#
# Example execution: ./caseMigrate.sh input.csv log.txt
#
###################################################

SCRIPT_HOME=$(pwd)  # Current Directory

#. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live
. $SCRIPT_HOME/setEnv.sh   # CTC

# Initialise DB Connection string
DB_CONN=$ORACLE_XHIBIT_DB_USER/$ORACLE_XHIBIT_DB_PASS@$ORACLE_SID


# Check arguments
# ------------------

if [ -z "$1" ]; then
  echo "No CSV Provided"
  exit 1
fi

if [ -z "$2" ]; then
  echo "No log file provided"
  exit 1
fi

csv_file="$1"
log_file="$2"

# Check CSV exists at the given location
if [ ! -f "$csv_file" ]; then
  echo "CSV file not found at location: $csv_file"
  exit 1
fi

# Check Log file exists at the given location
if [ ! -f "$log_file" ]; then
  echo "Log file not found at location: $log_file"
  exit 1
fi


# Process data
# ---------------

run_sql() {
  local query="$1"
  sqlplus -s $DB_CONN <<EOF
SET HEADING OFF
SET FEEDBACK OFF
SET PAGESIZE 0
SET VERIFY OFF
SET TRIMSPOOL ON
SET DEFINE OFF
SET SERVEROUTPUT ON

$query

EXIT;
EOF
}

# Set the first_line flag and row number
first_line=true
record_number=0

# Loop through the csv records line by line and log the output
while IFS=',' read -r col1 col2 col3 col4 || [ -n "$col1$col2$col3$col4" ]; do

  # Skip the first line (csv headers)
  if [ "$first_line" = true ]; then
    first_line=false
    continue
  fi
  
  # Track record number
  record_number=$((record_number + 1))

  # Trim leading and trailing whitespace around data
  col1=$(printf '%s' "$col1" | xargs)
  col2=$(printf '%s' "$col2" | xargs)
  col3=$(printf '%s' "$col3" | xargs)
  col4=$(printf '%s' "$col4" | xargs)

  echo "Processing record $record_number: $col1, $col2, $col3, $col4" >> "$log_file"
  

  # 1) Data Validation
  # -------------------
  echo " 1) Validating record data" >> "$log_file"
  valid=true
  
  # Validate Case Number (i.e LNNNNNNNN)
  if [[ ! "$col1" =~ ^[A-Za-z][0-9]{8}$ ]]; then
    echo "    - ERROR Invalid Case Number: $col1" >> "$log_file"
    valid=false
  fi
  
  # Validate Court ID (i.e NNN)
  if [[ ! "$col2" =~ ^[0-9]{3}$ ]]; then
    echo "    - ERROR Invalid Crest Court ID: $col2" >> "$log_file"
    valid=false
  fi
  
  # Validate Migrated To Location (i.e "ARM" or "ARM & CP")
  if [[ ! "$col3" =~ ^(ARM|ARM[[:space:]]&[[:space:]]CP)$ ]]; then
    echo "    - ERROR Invalid Migrated To Location: $col3" >> "$log_file"
    valid=false
  fi

  # Log Data Validation
  if [ "$valid" = false ]; then
	echo "    Data Validation Failed" >> "$log_file"
	echo "---------------------------" >> "$log_file"
  	echo "" >> "$log_file"
	continue
  fi
  echo "    Data Validation Passed" >> "$log_file"
  
  
  # 2) Find Court ID
  # ----------------
  echo " 2) Finding Court ID from Crest Court ID: $col2" >> "$log_file"
  
  # Find court_id and trim whitespace on result
  court_id=$(run_sql "SELECT court_id FROM XHB_COURT WHERE crest_court_id = '$col2';")
  court_id=$(echo "$court_id" | xargs)

  # Log finding the Court ID
  if [ -z "$court_id" ]; then
    echo "    - ERROR No match found in DB for Crest Court ID: $col2" >> "$log_file"
    echo "---------------------------" >> "$log_file"
  	echo "" >> "$log_file"
    continue
  fi
  echo "    Court ID found: $court_id" >> "$log_file"

  
  # 3) Find Case ID
  # ----------------
  case_type="${col1:0:1}"
  case_number="${col1:1}"
  echo " 3) Finding Case ID for Case: $case_type$case_number with Court ID: $court_id" >> "$log_file"
  
  # Find case_id and trim whitespace on result
  case_id=$(run_sql "SELECT case_id FROM XHB_CASE WHERE case_type = '$case_type' AND case_number = '$case_number' AND court_id = '$court_id';")
  case_id=$(echo "$case_id" | xargs)

  # Log finding the Case ID
  if [ -z "$case_id" ]; then
    echo "    - ERROR No match found in DB for Case: $case_type$case_number with Court ID: $court_id" >> "$log_file"
    echo "---------------------------" >> "$log_file"
  	echo "" >> "$log_file"
    continue
  fi
  echo "    Case ID found: $case_id" >> "$log_file"
  
  
  # 4) Insert Migration record
  # ----------------
  echo " 4) Insert the XHB_MIGRATE_CASE record" >> "$log_file"
  
  insertRecord=$(run_sql "
  BEGIN
	INSERT INTO XHB_MIGRATE_CASE (case_id, migrated, migration_to, migration_to_urn, migration_date)
	VALUES ($case_id, 'Y', '$col3', '$col4', SYSDATE);
	
	DBMS_OUTPUT.PUT_LINE('SUCCESS');
	
	COMMIT;
  END;
  /
  ")
  
  # Check if the insert command failed
  if [[ "$insertRecord" != *SUCCESS* ]]; then
	echo "    - ERROR Insert failed" >> "$log_file"
    echo "---------------------------" >> "$log_file"
  	echo "" >> "$log_file"
    continue
  fi
  echo "    Case: $case_type$case_number successfully migrated" >> "$log_file"
  echo "---------------------------" >> "$log_file"
  echo "" >> "$log_file"
  
  
done < <(tr -d '\r' < "$csv_file")

echo "Processed all records in $csv_file and logged to $log_file"