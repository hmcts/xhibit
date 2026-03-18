
###################################################
#
# Name: CheckMessageAreBeingReceived.sh
#
# Created: 18th August 2020
#
# Purpose: Checks that we are still seeing messages from CPP
#	   It will run during working hours, every hour and will cater for issues where Staging Server
#	   is not sending on messages as well as CPP not sending any
#	   It will email if its unsuccessful
#
#
# How to use: Run this script on its own.
#
# Return Codes: 11 = Exit due to validation failure
#               14 = Success
#
###################################################

#SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live
SCRIPT_HOME=/home/wmbroker/bin/cron/CPPX   # CTC

#. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live
. $SCRIPT_HOME/setEnv.sh   # CTC

SUCCESS=true

#Log file# 
LOGFILE=$SCRIPT_HOME/logs/CPPCheckForRecentMessages_logs_`date +"%d%m%y_%H%M%S"`.txt

# Email variables - need to be amended accordingly #
EMAIL_RECIPIENTS=xhibit_support@cgi.com
EMAIL_SENDER=xhibitcsh@justice.gov.uk
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
TODAY=`date '+DATE: %m/%d/%y TIME:%H:%M:%S'`

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
# We can now check if if messages have been received recently
#
CANSENDEMAIL=false
echo About to check >> $LOGFILE

## The output of this gets written to the files defined above
## It checks for last hour for any merge failures
find $SCRIPT_HOME/messageLastReceivedTime.txt -mtime -1
THISCHECK=`find $SCRIPT_HOME/messageLastReceivedTime.txt -mtime -1`

if [ -z $THISCHECK ];
then
  echo File has not been updated to indicate messages have been received >> $LOGFILE
  CANSENDEMAIL=true
else
  echo Recent messages received! >> $LOGFILE
fi


## if staging servre conn is offline then send an email ##
if [ $CANSENDEMAIL = "true" ]
then
  echo Now going to send an email for conn check >> $LOGFILE 
  OUTPUT_FILE=$SCRIPT_HOME/logs/CheckForRecentMessages.log
  echo `date +"%d-%m-%y %T"`, There are no messages in last 1 hour of working day activity >> $OUTPUT_FILE

  ####
  # Send the email as all checks have been done
  #
  #### Email variables

  # Initial Subject #
  SUBJECT="CPPX checks indicate no messages have been received in the past working hour"
  
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
