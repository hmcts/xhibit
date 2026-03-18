#!/usr/bin/bash

###################################################
#
# Name: PurgeUsers.sh
#
# Created: 24th November 2009
#
# Amendments: 12th January 2010 - Added to write audit record for each entry truncated
#
# Purpose: Writes to a file the number of logins at the end of the day where the AUD_USER_LOGINS login flag is 'Y'
#          Writes to this file the number of logins at the end of the day in AUD_USER_LOGINS where teh login flag is 'Y' or 'N'
#          Truncates the table AUD_USER_LOGINS
#
#
# How to use: This script will be run automatically via a crontab job
#
# Return Codes: 11 = Exit due to validation failure
#               TODO 13 = Exit due to sql error or database access error
#               14 = Success
#
###################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh
SUCCESS=true

# This variable stores the number of users logged in to be sent via email
PURGE_USERS_FILENAME=$SCRIPT_HOME/output/purge_users.txt

# This variable stores the name of the audit file to append to
# It assumes the file exists
AUD_USERS_FILENAME=$SCRIPT_HOME/../output/aud_users_evening.txt

# Output types available are "file" and "db"
OUTPUT_TYPE=file

## Remove old output files
if [ -f $PURGE_USERS_FILENAME ]
then
  rm $PURGE_USERS_FILENAME
fi

# This value is the value in XHB_EMAIL2 so that the correct email is sent for the correct issue
# XHIBIT Support
EMAIL_ID=3

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
# At the moment set this to true
#
CANSENDEMAIL=true

## Log the number of logged in users to a file for later use
echo sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/count_users_logged_in.sql $PURGE_USERS_FILENAME
sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/count_users_logged_in.sql $PURGE_USERS_FILENAME


## Now write the data for each user to the audit users log
# 1. Get the next item in the AUD_USER_LOGINS table where LOGGED_IN=Y
# 2. For this item get extraneous details such as court name
# 3. Write details to log
# 4. Change the LOGGED_IN value for this entry to LOGGED_IN=N
STILL_LOOPING=true
NEXT_ENTRY=$SCRIPT_HOME/output/next_aud_user_logins_entry_to_be_deleted.txt
SQL_CHECK_FILENAME=$SCRIPT_HOME/output/unlock_user_details.txt

echo About to begin looping....
echo sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/get_next_user_logged_in.sql $NEXT_ENTRY
sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/get_next_user_logged_in.sql $NEXT_ENTRY
USER_ID=`sed -n '4,4p' $NEXT_ENTRY`
USER_ID=$( echo "$USER_ID" | tr -d ' ' )
echo Next user to unlock is $USER_ID

while [ $STILL_LOOPING = "true" ]
do
  echo Looping again...
  sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/get_next_user_logged_in.sql $NEXT_ENTRY
  USER_ID=`sed -n '4,4p' $NEXT_ENTRY`
  USER_ID=$( echo "$USER_ID" | tr -d ' ' )
  echo Next USER_ID to unlock $USER_ID
  echo Char count for user id =`echo $USER_ID | wc -c`

  # Check to see that the userid is not empty, i.e. more than 0 chars long
  if [ `echo $USER_ID | wc -c` = 1 ]
  then
    echo No user ids left logged in
    STILL_LOOPING=false
  else
    echo Continue to find the next user id

    sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/get_unlock_user_details.sql $USER_ID $SQL_CHECK_FILENAME
    TERMINAL_NAME=`sed -n '6,6p' $SQL_CHECK_FILENAME`
    TERMINAL_NAME=$( echo "$TERMINAL_NAME" | tr -d ' ' )
    echo Terminal Name is $TERMINAL_NAME

    COURT_NAME=`sed -n '13,13p' $SQL_CHECK_FILENAME`
    COURT_NAME=$( echo "$COURT_NAME" | tr -d ' ' )
    echo Court is $COURT_NAME

    if [ $OUTPUT_TYPE = "file" ]
    then
      echo About to write entry to audit log.
      echo `date +"%d-%m-%y %T"`, $COURT_NAME, $TERMINAL_NAME, $USER_ID >>$AUD_USERS_FILENAME
    else
      CURR_DATETIME=`date +"%d-%m-%y %T"`
      echo Insert into database
      #sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/insert_audit_record.sql $CURR_DATETIME $COURT_NAME $TERMINAL_NAME $USER_ID
    fi

    echo About to unlock user $USER_ID
    sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/unlock_user.sql $USER_ID
  fi
done

echo All users unlocked now.
echo About to truncate table
## Truncate the table
sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/truncate_users_logged_in.sql

USERS_FLAGY=`sed -n '4,6p' $PURGE_USERS_FILENAME`

if [ $USERS_FLAGY -lt 100 ]
then
  CANSENDEMAIL=false
else 
  CANSENDEMAIL=true
fi

if [ $CANSENDEMAIL = "true" ]
then
  echo Now going to send an email
  ####
  # Send the email as all checks have been done
  #
  SUBJECT='Number of users logged in with flag = Y and active session updated in last 15 mins: '$USERS_FLAGY
  SUBJECT=\'$SUBJECT\'
  echo $SUBJECT
MAIL_UPDATE_FILENAME=$SCRIPT_HOME/output/mail.txt
  sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/send_email_with_subject.sql $EMAIL_ID $SUBJECT $MAIL_UPDATE_FILENAME
  sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/send_email_with_subject.sql $EMAIL_ID1 $SUBJECT $MAIL_UPDATE_FILENAME

  echo Finished ......
  exit 14
else
  echo All ok for the moment
  exit 14
fi

