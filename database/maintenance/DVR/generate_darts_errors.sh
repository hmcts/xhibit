###################################################
#
# Name: generate_darts_messages.sh
#
# Created: January 2022
#
# Purpose: Sends an email with the amount of DARTS 404 handler errors to a fixed list of email contacts
#
#
# How to use: You can pass the following format of arguments in to run this report;
#			- No arguments to generate previous days errors
#			- 1 argument of start date - i.e ./generate_darts_errors.sh ##/##/####
#			- 2 arguments of start and end date - i.e ./generate_darts_errors.sh ##/##/#### ##/##/####
#			- OR this script can be ran automatically from generate_darts_messages to use the same time frame
#
# Valid Dates example: 01/01/2022 24/01/2022
#
# Return Codes: 11 = Exit due to validation failure
#               14 = Success
#
###################################################
#
#SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/DVR   # for NLE/Live
SCRIPT_HOME=/home/wmbroker/bin/cron/DVR   # CTC
#. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live
	. $SCRIPT_HOME/setEnv.sh   # CTC
#Log file# 
LOGFILE=$SCRIPT_HOME/logs/DARTS_errors_logs_`date +"%d%m%y_%H%M%S"`.txt
#

# Email variables - need to be amended accordingly #
EMAIL_RECIPIENTS=scott.atwell@cgi.com
EMAIL_SENDER=xhibitcsh@test.justice.gov.uk
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER='<fill this in>'
TODAY=`date '+DATE: %d/%m/%y TIME: %H:%M:%S'`
#

echo "Before connecting to db to get DARTS Error data" >> $LOGFILE 
DARTS_ERRORS=`sqlplus -s ${ORACLE_DARTS_DB_USER}/${ORACLE_DARTS_DB_PASS}@${ORACLE_SID} <<endsql
  SET SERVEROUTPUT ON;
  SET FEEDBACK OFF;
  WHENEVER SQLERROR EXIT SQL.SQLCODE
  exec DAR_HOUSEKEEPING_PKG.get_darts_errors('$1', '$2');
  exit
  endsql`
  
err_code=$?
if [ $err_code != 0 ]; 
then
	if [ $err_code -eq 137 ]; then
		echo "Ensure Start Date is before End Date";
	elif [ $err_code -eq 47 ]; then
		echo "Invalid Date Format, use (DD/MM/YYYY)";
	elif [ $err_code -eq 142 ]; then
		echo "Date Range is Limited to a Maximum of 60 Days";
	else
		echo "Unknown Error Occurred. Error Code: " $err_code;
	fi
	exit 1
fi

echo 'Now going to send an email with subject of failed 404 : Handler Errors' >> $LOGFILE 
  
####
# Send the email as all checks have been done
#
#### Email variables

# Initial Subject #
SUBJECT="DARTS Message handler 404 failures: $DARTS_ERRORS"

# echo out the subject # 
echo SUBJECT = $SUBJECT >> $LOGFILE 
emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY "
`$emailCmd`

echo Finished ...... >> $LOGFILE 
exit 14