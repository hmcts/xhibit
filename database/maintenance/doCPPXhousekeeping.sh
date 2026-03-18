#!/usr/bin/ksh

#################################################################################
#
# Name: doCPPXhousekeeping.sh
# Created: January 2020
# Purpose: Runs the CPPX housekeeping jobs
#
#################################################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

THISLOG=output/doCPPXhousekeeping_`date +"%d%m%y_%H%M%S"`.log
OUTPUT=`sqlplus -s ${ORACLE_XHIBIT_DB_USER}/${ORACLE_XHIBIT_DB_PASS}@${ORACLE_SID} << END
SET SERVEROUTPUT ON;
exec XHB_HOUSEKEEPING_PKG.PROCESS_CPP();
`

echo "$OUTPUT" > $THISLOG

exit 