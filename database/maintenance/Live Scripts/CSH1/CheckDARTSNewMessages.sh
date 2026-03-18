
###################################################
#
# Name: CheckDARTSNewMessages.sh
#
# Created: 26th January 2010
#
# Purpose: Checks to see how many rows are in the DAR_NEW_MESSAGES table
#          This shows the build up of messages waiting to be sent
#          If it gets too high (.e.g > 400) then this can show a potential problem and will be alerted
#
#
# How to use: Run this script on its own. No arguments should be passed in.
#
# Return Codes: 11 = Exit due to validation failure
#               TODO 13 = Exit due to sql error or database access error
#               14 = Success
#
###################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh
SUCCESS=true
DAR_CHECK_FILENAME=$SCRIPT_HOME/output/number_of_new_darts_messages.txt
LIMIT_BEFORE_WARNING=400

## Remove old output files
if [ -f $DAR_CHECK_FILENAME ]
then
  rm $DAR_CHECK_FILENAME
fi

####
# Check that there are no arguments passed in
#
if [ $# -ne 0 ]
then
  echo "Error in $0 - Invalid Argument Count"
  SUCCESS=false
fi


####
# Now if we are still successful then continue, otherwise error
#
if [ $SUCCESS = "false" ]
then
  echo "Syntax: $0"
  echo
  exit 11
fi


####
# We can now check how many DARTS messages there are
#
CANSENDEMAIL=false

## The output of this gets written to a files defined above
## If the contents of the file contain the number 0 then the counts need to be checked
sqlplus <username>/<password>@${ORACLE_SID2} @$SCRIPT_HOME/sql/count_darts_new_messages.sql $DAR_CHECK_FILENAME

CHK=`sed -n '4,4p' $DAR_CHECK_FILENAME`
if [ $CHK -gt $LIMIT_BEFORE_WARNING ]
then
  echo There are more than $LIMIT_BEFORE_WARNING new DARTS messages so send an email
  CANSENDEMAIL=true
else
  echo Less than ${LIMIT_BEFORE_WARNING} new messages so all ok for the moment
fi

## Write the value to a file so we can see historical levels
OUTPUT_FILE=$SCRIPT_HOME/output/darts_message_size.log
echo `date +"%d-%m-%y %T"`, $CHK >> $OUTPUT_FILE


if [ $CANSENDEMAIL = "true" ]
then
  echo Now going to send an email
  ####
  # Send the email as all checks have been done
  #
  EMAIL_RECIPIENTS="xhibit_support@logica.com mojasloccsdarts@cgi.com"
  TODAY=`date '+DATE: %d/%m/%y TIME:%H:%M:%S'`

  SUBJECT='DARTS New Messages:'$CHK' Possible DARTS high volume of new messages in DAR_NEW_MESSAGES. Please investigate.'
  EMAILCMD="$SCRIPT_HOME/../sendemail.pl -f xhibitcsh@justice.gov.uk -t $EMAIL_RECIPIENTS -u DARTS New Messages Alert -s $SMTP_SERVER -m $TODAY ; $SUBJECT "

  $EMAILCMD

  echo Finished ......
  exit 14
else
  echo All ok for the moment
  exit 14
fi
