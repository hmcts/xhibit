#!/usr/bin/ksh

#################################################################################
#
# Name: run_judge_archive.sh
# Created: August 2019
# Purpose: Runs the judge housekeeping jobs
#
# How to use: This script should be scheduled to run once a week. 
# 			  No arguments are required.
#
#################################################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

THISLOG=output/run_judge_archive_`date +"%d%m%y_%H%M%S"`.log
OUTPUT=`sqlplus -s ${ORACLE_XHIBIT_DB_USER}/${ORACLE_XHIBIT_DB_PASS}@${ORACLE_SID} << END
SET SERVEROUTPUT ON;
exec XHB_HOUSEKEEPING_PKG.PROCESS_JUDGES();
`

echo "$OUTPUT" > $THISLOG

exit 