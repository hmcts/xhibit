
###################################################
#
# Name: CheckMergeFailures.sh
#
# Created: 27th July 2020
#
# Purpose: Checks for any records which have a Merge Failed status
#	   It will run periodically and email if any are found,although a threshold could be set but will initially be 0
#
#
# How to use: Run this script on its own. Pass in filename to write output to.
#
# Return Codes: 11 = Exit due to validation failure
#               14 = Success
#
###################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live

. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live

SUCCESS=true
# The values of how many rows that have merge failed will be written to the following locations #
MERGE_FAILED_CHECK_FILENAME=$SCRIPT_HOME/output/CheckCPPMergeFailedStatus.txt

# How many before we flag up in an email #
NUM_MERGE_FAILS_LIMIT_BEFORE_WARNING=0

# Used for DB #
PL_CONNECT_STRING=$ORACLE_XHIBIT_DB_USER/$ORACLE_XHIBIT_DB_PASS@$ORACLE_SID

#Log file# 
LOGFILE=$SCRIPT_HOME/logs/CPPMergeFailes_logs_`date +"%d%m%y_%H%M%S"`.txt

# Email variables - need to be amended accordingly #
EMAIL_RECIPIENTS=scott.atwell@cgi.com
EMAIL_SENDER=xhibitcsh@justice.gov.uk
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
TODAY=`date '+DATE: %m/%d/%y TIME:%H:%M:%S'`

echo "Before removing old files" >> $LOGFILE 
# Remove old output files as we don't want to use the old numbers#
if [ -f $MERGE_FAILED_CHECK_FILENAME ]
then
  rm $MERGE_FAILED_CHECK_FILENAME
fi

####
# Check that there are no arguments passed in
#
if [ $# -ne 0 ]
then
  echo "Error in $0 - Invalid Argument Count" >> $LOGFILE 
  SUCCESS=false
fi


####
# Now if we are still successful then continue, otherwise error
#
if [ $SUCCESS = "false" ]
then
  echo "Syntax: $0" >> $LOGFILE 
  echo >> $LOGFILE 
  exit 11
fi

####
# We can now check how many rows not processed there are 
#
CANSENDEMAIL=false

echo "Before connecting to db to check Merge Fails" >> $LOGFILE 
## The output of this gets written to the files defined above
## It checks for last hour for any merge failures
sqlplus $PL_CONNECT_STRING @$SCRIPT_HOME/sql/count_Merge_Fails_Status.sql $MERGE_FAILED_CHECK_FILENAME

# Save the number of rows to each variable #
CHKMF=`sed -n '4,4p' $MERGE_FAILED_CHECK_FILENAME`

##If any of the tables have any old unprocessed then we know we can send an email##
if [ $CHKMF -gt $NUM_MERGE_FAILS_LIMIT_BEFORE_WARNING ]
then
  echo There are more than the limit of merge failurs for the permitted time period >> $LOGFILE 
  CANSENDEMAIL=true
else
  echo Less than the allowed merge failures so all ok for now >> $LOGFILE 
fi


##if any are >0 then we know to send an email##
if [ $CANSENDEMAIL = "true" ]
then
  echo Now going to send an email for MF Status >> $LOGFILE 
  ## Write the value to a file so we can see historical levels
  OUTPUT_FILE=$SCRIPT_HOME/logs/MergeFails_AsPerJobToCheck.log
  echo `date +"%d-%m-%y %T"`, MERGE_FAILS in past hour = $CHKMF >> $OUTPUT_FILE

  ####
  # Send the email as all checks have been done
  #
  #### Email variables

  # Initial Subject #
  SUBJECT="CPPX documents where merge has failed in either XHB_CPP_LIST or XHB_CPP_FORMATTING: $CHKMF records in past hour"
  
  # echo out the subject # 
  echo SUBJECT = $SUBJECT >> $LOGFILE 
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY "
  echo EmailCmd: $emailCmd >> $LOGFILE
  `$emailCmd`
  
  echo Finished ...... >> $LOGFILE 
  exit 14
else
  echo All ok for now >> $LOGFILE 
  exit 14
fi
