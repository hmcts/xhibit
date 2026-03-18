#!/usr/bin/ksh

#################################################################################
#
# Name: run_misc_hk.sh
# Created: September 2019
# Purpose: Runs the miscellaneous housekeeping jobs including deletion of 
#		   successful CAD run logs and aud_report_log records
#
#################################################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/HK/CTX
. $SCRIPT_HOME/../../setEnv.sh

THISLOG=$SCRIPT_HOME/output/run_misc_hk_`date +"%d%m%y_%H%M%S"`.log
OUTPUT=`sqlplus -s ${ORACLE_XHIBIT_DB_USER}/${ORACLE_XHIBIT_DB_PASS}@${ORACLE_SID} << END
SET SERVEROUTPUT ON;
exec XHB_HOUSEKEEPING_PKG.PROCESS_CAD_HK();
exec XHB_HOUSEKEEPING_PKG.PROCESS_REPORT_HK();
`

echo "$OUTPUT" > $THISLOG

exit 
