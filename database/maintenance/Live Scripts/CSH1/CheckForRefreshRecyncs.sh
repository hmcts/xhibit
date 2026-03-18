
###################################################
#
# Name: CheckForRefreshRecyncs.sh
#
# Created: 13th August 2009
#
# Purpose: Checks to see how many cases have a charge_import_indicator of 'S' or 'P'
#          This shows the build up of cases waiting to be processed
#          If either gets above 20 then this can show a potential problem and will be alerted
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
RR_VALUE_P_CHECK_FILENAME=$SCRIPT_HOME/output/refresh_resync_checker_P.txt
RR_VALUE_S_CHECK_FILENAME=$SCRIPT_HOME/output/refresh_resync_checker_S.txt
LIMIT_BEFORE_WARNING=20
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com "
#EMAIL_RECIPIENTS="vishwanath.mallya@cgi.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk


## Remove old output files
## Remove old output files
if [ -f $RR_VALUE_P_CHECK_FILENAME ]
then
  rm $RR_VALUE_P_CHECK_FILENAME
fi

if [ -f $RR_VALUE_S_CHECK_FILENAME ]
then
  rm $RR_VALUE_S_CHECK_FILENAME
fi

# This value is the value in XHB_EMAIL2 so that the correct email is sent for the correct issue
EMAIL_ID=4
#EMAIL_ID2=1000002 # This is for Richard Schuchardt

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
# We can now check how many P and S there are in XHB_CASE
#
CANSENDEMAIL=false

## The output of this gets written to a files defined above
## If the contents of the file contain the number 0 then the counts need to be checked
sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/count_refresh_resyncs.sql P $RR_VALUE_P_CHECK_FILENAME
sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/count_refresh_resyncs.sql S $RR_VALUE_S_CHECK_FILENAME

CHKP=`sed -n '6,6p' $RR_VALUE_P_CHECK_FILENAME`
CHKS=`sed -n '6,6p' $RR_VALUE_S_CHECK_FILENAME`
if [ $CHKP -gt $LIMIT_BEFORE_WARNING ]
then
  echo There are more than $LIMIT_BEFORE_WARNING cases with status of P so send an email
  CANSENDEMAIL=true
else
  echo Less than ${LIMIT_BEFORE_WARNING}P so all ok for the moment
fi

if [ $CHKS -gt $LIMIT_BEFORE_WARNING ]
then
  echo There are more than $LIMIT_BEFORE_WARNING cases with status of S so send an email
  CANSENDEMAIL=true
else
  echo Less than ${LIMIT_BEFORE_WARNING}S so all ok for the moment
fi

if [ $CANSENDEMAIL = "true" ]
then
  echo Now going to send an email
  ####
  # Send the email as all checks have been done
  #
  SUBJECT='Count:P'$CHKP',S'$CHKS' Possible refresh resync in progress. Please investigate.'
  SUBJECT=\'$SUBJECT\'
  echo $SUBJECT
  #sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/send_email.sql $EMAIL_ID $MAIL_UPDATE_FILENAME
  #sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/send_email_with_subject.sql $EMAIL_ID $SUBJECT $MAIL_UPDATE_FILENAME
  #sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/send_email_with_subject.sql $EMAIL_ID2 $SUBJECT $MAIL_UPDATE_FILENAME
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m Please investigate resync issues"
  `$emailCmd`


  echo Finished ......
  exit 14
else
  echo All ok for the moment
  exit 14
fi
