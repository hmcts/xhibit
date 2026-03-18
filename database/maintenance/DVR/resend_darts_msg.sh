###################################################
#
# Name: resend_darts_msg.sh
#
# Created: January 2022
#
# Purpose: Resend the failed DARTS messages to DARTS
#
# Return Codes: 11 = Exit due to failure
#               14 = Success
#
###################################################
#
# Set the run folder #
#SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/DVR   # for NLE/Live
SCRIPT_HOME=/home/wmbroker/bin/cron/DVR   # CTC
#
# Set environment variables #
#. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live
. $SCRIPT_HOME/setEnv.sh   # CTC
#
# Log file # 
LOGFILE=$SCRIPT_HOME/logs/DARTS_resend_msg_logs_`date +"%d%m%y_%H%M%S"`.txt
#
# Run DB package
echo "Before connecting to db to resend failed DARTS messages" >> $LOGFILE 
DARTS_OUTPUT=`sqlplus -s ${ORACLE_DARTS_DB_USER}/${ORACLE_DARTS_DB_PASS}@${ORACLE_SID} <<endsql
  SET SERVEROUTPUT ON;
  WHENEVER SQLERROR EXIT SQL.SQLCODE
  exec dar_message_pkg.resend_failed_messages;
  exit
  endsql`

err_code=$?
if [ $err_code != 0 ]; 
then
	if [ $err_code -eq 144 ]; then
		ERRORMSG="Invalid value in config 'darts.resend.max.message_batch'."
		echo $ERRORMSG;
		echo $ERRORMSG >> $LOGFILE
	elif [ $err_code -eq 143 ]; then
		ERRORMSG="Invalid value in config 'darts.resend.history_cutoff_limit_hrs'."
		echo $ERRORMSG;
		echo $ERRORMSG >> $LOGFILE
	elif [ $err_code -eq 142 ]; then
		ERRORMSG="Invalid value in config 'darts.resend.dontrun_period'."
		echo $ERRORMSG;
		echo $ERRORMSG >> $LOGFILE
	else
		echo "Unknown Error Occurred. Error Code: " $err_code;
		echo "Unknown Error Occurred. Error Code: " $err_code >> $LOGFILE
	fi
	exit 11
fi
    
echo Finished ...... >> $LOGFILE 
exit 14