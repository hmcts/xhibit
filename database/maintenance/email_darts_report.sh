#!/usr/bin/ksh

#################################################################################
#
# Name: email_darts_report.sh
# Created: January 2022
# Purpose: Fetches the Data used in the DARTS report and sends the results to
#		   a fixed list of email contatcs.
#
#################################################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

THISLOG=output/run_misc_hk_`date +"%d%m%y_%H%M%S"`.log
OUTPUT=`sqlplus -s ${ORACLE_XHIBIT_DB_USER}/${ORACLE_XHIBIT_DB_PASS}@${ORACLE_SID} << END
SET SERVEROUTPUT ON;
exec XHB_HOUSEKEEPING_PKG.generate_darts_report();
`

echo "$OUTPUT" > $THISLOG

exit 