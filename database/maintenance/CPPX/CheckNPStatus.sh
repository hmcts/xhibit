
###################################################
#
# Name: CheckNPStatus.sh
#
# Created: 6th April 2020
#
# Purpose: Checks to see if there are any rows in DSS, List or IWP tables where it's older
#		   than a day.
#
#
# How to use: Run this script on its own. No arguments should be passed in.
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
# The values of how many rows unprocessed will be written to the following locations #
DSS_CHECK_FILENAME=$SCRIPT_HOME/output/CheckNPStatusDSS.txt
LIST_CHECK_FILENAME=$SCRIPT_HOME/output/CheckNPStatusList.txt
IWP_CHECK_FILENAME=$SCRIPT_HOME/output/CheckNPStatusIWP.txt

# How many before we flag up in an email #
DSS_LIMIT_BEFORE_WARNING=0
LIST_LIMIT_BEFORE_WARNING=0
IWP_LIMIT_BEFORE_WARNING=0

# Used for DB #
PL_CONNECT_STRING=$ORACLE_XHIBIT_DB_USER/$ORACLE_XHIBIT_DB_PASS@$ORACLE_SID

#Log file# 
LOGFILE=$SCRIPT_HOME/logs/NP_logs_`date +"%d%m%y_%H%M%S"`.txt

# Email variables - need to be amended accordingly #
EMAIL_RECIPIENTS=scott.atwell@cgi.com
EMAIL_SENDER=xhibitcsh@test.justice.gov.uk
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER='<fill this in>'
TODAY=`date '+DATE: %d/%m/%y TIME: %H:%M:%S'`

echo "Before removing old NP files" >> $LOGFILE 
# Remove old output files as we don't want to use the old numbers#
if [ -f $DSS_CHECK_FILENAME ]
then
  rm $DSS_CHECK_FILENAME
fi
if [ -f $LIST_CHECK_FILENAME ]
then
  rm $LIST_CHECK_FILENAME
fi
if [ -f $IWP_CHECK_FILENAME ]
then
  rm $IWP_CHECK_FILENAME
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

echo "Before connecting to db to check NP Status" >> $LOGFILE 
## The output of this gets written to the files defined above
## If the contents of the file contain the number 0 then the counts need to be checked
sqlplus $PL_CONNECT_STRING @$SCRIPT_HOME/sql/count_DSS_NP.sql $DSS_CHECK_FILENAME
sqlplus $PL_CONNECT_STRING @$SCRIPT_HOME/sql/count_List_NP.sql $LIST_CHECK_FILENAME
sqlplus $PL_CONNECT_STRING @$SCRIPT_HOME/sql/count_IWP_NP.sql $IWP_CHECK_FILENAME

# Save the number of rows to each variable #
CHKDSS=`sed -n '4,4p' $DSS_CHECK_FILENAME`
CHKLIST=`sed -n '4,4p' $LIST_CHECK_FILENAME`
CHKIWP=`sed -n '4,4p' $IWP_CHECK_FILENAME`

##If any of the tables have any old unprocessed then we know we can send an email##
if [ $CHKDSS -gt $DSS_LIMIT_BEFORE_WARNING ] || [ $CHKLIST -gt $LIST_LIMIT_BEFORE_WARNING ] || [ $CHKIWP -gt $IWP_LIMIT_BEFORE_WARNING ]
then
  echo There are more than the limit of old unprocessed messages in NP Status >> $LOGFILE 
  CANSENDEMAIL=true
else
  echo Less than the allowed unprocessed so all ok for the moment in NP Status >> $LOGFILE 
fi


##if any are >0 then we know to send an email##
if [ $CANSENDEMAIL = "true" ]
then
  echo Now going to send an email for NP Status >> $LOGFILE 
  ## Write the value to a file so we can see historical levels
  OUTPUT_FILE=$SCRIPT_HOME/logs/NP_Files_Unprocessed.log
  echo `date +"%d-%m-%y %T"`, DSS = $CHKDSS , LIST = $CHKLIST , IWP = $CHKIWP >> $OUTPUT_FILE

  ####
  # Send the email as all checks have been done
  #
  #### Email variables

  # Initial Subject #
  SUBJECT="Unprocessed CPPX documents : "
  
  # If the number of DSS unprocessed > what we've set then add the DSS to subject
  if [ $CHKDSS -gt $DSS_LIMIT_BEFORE_WARNING ]
  then
	SUBJECT="$SUBJECT DSS = $CHKDSS"
  fi
  
   # If the number of List unprocessed > what we've set then add the List to subject
   if [ $CHKLIST -gt $LIST_LIMIT_BEFORE_WARNING ]
  then
	SUBJECT="$SUBJECT LIST = $CHKLIST"
  fi
  
   # If the number of IWP unprocessed > what we've set then add the UWP to subject
   if [ $CHKIWP -gt $IWP_LIMIT_BEFORE_WARNING ]
  then
	SUBJECT="$SUBJECT  IWP = $CHKIWP"
  fi
  
  # echo out the subject # 
 echo SUBJECT = $SUBJECT >> $LOGFILE 
 	emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY "
	`$emailCmd`
  
  echo Finished ...... >> $LOGFILE 
  exit 14
else
  echo All ok for the moment >> $LOGFILE 
  exit 14
fi
