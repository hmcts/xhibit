###################################################
#
# Name: generate_darts_email.sh
#
# Created: January 2022
#
# Purpose: Sends an email with the DARTS report data to a fixed list of email contacts
#
# How to use: You can pass the following format of arguments in to run this report;
#			- No arguments to generate previous days data
#			- 1 argument of start date - i.e ./generate_darts_email ##/##/#### 
#			- 2 arguments of start and end date - i.e ./generate_darts_email ##/##/#### ##/##/####
#
# Valid Dates Example: 01/01/2022 24/01/2022
#
# Return Codes: 11 = Exit due to validation failure
#               14 = Success
#
###################################################
#
#SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/DVR   # for NLE/Live
SCRIPT_HOME=/home/wmbroker/bin/cron/DVR   # CTC
#
#. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live
. $SCRIPT_HOME/setEnv.sh   # CTC
#
# The values of how many rows unprocessed will be written to the following locations #
DARTS_REPORT_DATA=$SCRIPT_HOME/output/DartsReportData.csv
#
# Used for DB #
PL_CONNECT_STRING=$ORACLE_XHIBIT_DB_USER/$ORACLE_XHIBIT_DB_PASS@$ORACLE_SID
#
#Log file# 
LOGFILE=$SCRIPT_HOME/logs/DARTS_logs_`date +"%d%m%y_%H%M%S"`.txt
#
# Email variables - need to be amended accordingly #
EMAIL_RECIPIENTS=scott.atwell@cgi.com
EMAIL_SENDER=xhibitcsh@test.justice.gov.uk
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER='<fill this in>'
TODAY=`date '+DATE: %d/%m/%y TIME: %H:%M:%S'`
#
#
echo "Removing Old DARTS Report Data" >> $LOGFILE 
# Remove old output files as we don't want to use the old numbers#
if [ -f $DARTS_REPORT_DATA ]
then
  rm $DARTS_REPORT_DATA
fi

####
# Check that there are no arguments passed in
#
if [ $# -eq 0 ]
then
  echo "No Arguments Passed in, call Data for Previous Day" >> $LOGFILE 
elif [ $# -eq 2 ]
then
  echo "2 Arguments Passed in calling data from $1 to $2" >> $LOGFILE 
fi

echo "Before connecting to db to generate DARTS Report" >> $LOGFILE 
DARTS_OUTPUT=`sqlplus -s ${ORACLE_XHIBIT_DB_USER}/${ORACLE_XHIBIT_DB_PASS}@${ORACLE_SID} <<endsql
  SET SERVEROUTPUT ON;
  SET FEEDBACK OFF;
  WHENEVER SQLERROR EXIT SQL.SQLCODE
  exec XHB_HOUSEKEEPING_PKG.generate_darts_report('$1', '$2');
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
  
echo "$DARTS_OUTPUT" > $DARTS_REPORT_DATA

# Save the number of rows to each variable #
DARTSRPT=`sed -n '4,4p' $DARTS_REPORT_DATA`

echo Now going to send an email Containing DARTS Report >> $LOGFILE 

####
# Send the email as all checks have been done
#
#### Email variables

# Initial Subject #
SUBJECT="Reconciliation Report"

# echo out the subject # 
echo SUBJECT = $SUBJECT >> $LOGFILE 
emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY "
`$emailCmd`

echo Finished ...... >> $LOGFILE 
exit 14