#!/bin/ksh
# runsql.sh
# I Lo, EDS, 14-May-2007
# K Shah 15-May-2007  Adapted for release 8.1.3.1
# Usage runsql.sh <dbname> <schema> <script.sql>
#
# Runs sql script in specified database/schema
# spools output to file
# assumes usernames and passwords are the same
#
# If public synonyms exist for all schema objects,
# then possible to recode to make more generic by connecting as sysdba
#
. /export/home/oracle/.profile
#

# script location, logs will be written to log subdirectory
SCRIPT_LOC=<set script location directory here>
cd $SCRIPT_LOC

export ORACLE_SID=$1
export ORAENV_ASK="NO"
. oraenv

SCHEMA=$2
SQL_SCRIPT=$3
SQL_LOGFILE=$SCRIPT_LOC/log/$SQL_SCRIPT.log.`date +"%y%m%d%H%M%S"`

sqlplus /nolog << EOF
connect $SCHEMA/$SCHEMA
spool $SQL_LOGFILE

@$SQL_SCRIPT




spool off
--  lines above left intentionally blank to accept default values where scripts prompt

exit
EOF
