#!/usr/bin/ksh

#################################################################################
#
# Name: deleteCase.sh
# Created: April 2019
# Purpose: Delete cases that have been marked for deletion as part of the front end
#          delete case process.
#
# How to use: This script should be scheduled to run every 5 mins. No arguments are required.
#
#################################################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

THISLOG=output/DeleteCase_`date +"%d%m%y_%H%M%S"`.txt
OUTPUT=`sqlplus -s ${ORACLE_XHIBIT_DB_USER}/${ORACLE_XHIBIT_DB_PASS}@${ORACLE_SID} << END
SET SERVEROUTPUT ON;
exec XHB_HOUSEKEEPING_PKG.DELETE_CASE_CTX();
`

echo "$OUTPUT" > $THISLOG

exit 