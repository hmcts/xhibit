#!/usr/bin/ksh

#################################################################################
#
# Name:    processMonthlyCAD.sh
# Created: April 2019
# Purpose: Runs the CAD process for all courts with XHB_COURT.IS_PILOT='Y'
#	   If params are entered then they need to be a valid start and end date
#	   If no params are entered then its assumed they will be the 1st and last day of the previous month
#
# How to use: This script should be scheduled to run on the 1st day of each month.
#
#################################################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CAD
. /opt/moj/home/wmbroker/bin/cron/setEnv.sh
SUCCESS=true

START_DATE=$1
END_DATE=$2

# These variables store the start and end dates of the previous month in the format DD-MON-YYYY
START_DATE_FILENAME=$SCRIPT_HOME/start_date.txt
END_DATE_FILENAME=$SCRIPT_HOME/end_date.txt
START_DATE=''
END_DATE=''

LOG_FILE=processMonthlyCAD.log


######### FUNCTIONS #########
# Function used to send emails
email() {
  echo $1 $2 $3 $4 >> ${LOG_FILE}
  EMAIL_RECIPIENTS=$1
  TODAY=`date '+DATE: %d/%m/%y TIME:%H:%M:%S'`

  SUBJECT=$2
  MESSAGE=$3
  ATTACHMENTS=$4

  EMAILCMD="/opt/moj/home/wmbroker/bin/sendemail.pl -f xhibitCAD@justice.gov.uk -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY ; $MESSAGE -a $ATTACHMENTS "

  $EMAILCMD
}


# Function to get the 2 digit month from the first 3 chars
getMonth() {
  inMonth=$1
  echo Getting 2 digit month for $inMonth >> ${LOG_FILE}

  case $inMonth in
    "JAN" ) retval=1; return "$retval";;
    "FEB" ) retval=2; return "$retval";;
    "MAR" ) retval=3; return "$retval";;
    "APR" ) retval=4; return "$retval";;
    "MAY" ) retval=5; return "$retval";;
    "JUN" ) retval=6; return "$retval";;
    "JUL" ) retval=7; return "$retval";;
    "AUG" ) retval=8; return "$retval";;
    "SEP" ) retval=9; return "$retval";;
    "OCT" ) retval=10; return "$retval";;
    "NOV" ) retval=11; return "$retval";;
    "DEC" ) retval=12; return "$retval";;
      *   ) retval=13; return "$retval";;
  esac

  echo Shouldnt get here!!  >> ${LOG_FILE}
 
}

######### END OF FUNCTION DECLARATIONS #########




# Check if any values were supplied and they are valid dates
echo "Values entered: $1 $2" >> ${LOG_FILE}
echo Num Params: ${#} >> ${LOG_FILE}


# Check the params (if supplied) are valid
# If no args are passed in then fine, set START_DATE and END_DATE accordingly
  if [ "${#}" = "0" ]
  then
    sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/getFirstDayOfLastMonth.sql $START_DATE_FILENAME
    sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/getLastDayOfLastMonth.sql $END_DATE_FILENAME
    echo "No args passed in - setting START_DATE and END_DATE to last month automatically" >> ${LOG_FILE}

    START_DATE=`sed -n '4,4p' $START_DATE_FILENAME`
    END_DATE=`sed -n '4,4p' $END_DATE_FILENAME`
    SUCCESS=true

  elif [ "${#}" != "2" ]
  then
    echo "Invalid args passed in setting START_DATE and END_DATE - Invalid Argument Count" >> ${LOG_FILE}
    SUCCESS=false

  else
    echo "Passed in correct no of args...assuming they are valid dates" >> ${LOG_FILE}
    SUCCESS=true
  fi


if [ $SUCCESS == "false" ]
then
  echo "Finishing now...bye" >> ${LOG_FILE}
  exit
fi


echo Start Date = $START_DATE >> ${LOG_FILE}
echo End Date = $END_DATE >> ${LOG_FILE}


##############################
#### Now the inputs are correct, run the CAD process

EXECPROCLOG=RUNCAD_`date +"%d%m%y_%H%M%S"`.txt
OUTPUT=`sqlplus -s <username>/<password>@csdbprd1 << END
SET SERVEROUTPUT ON;
exec XHB_EXECUTE_DMI_CAD_FILE();
`

echo "$OUTPUT" > $EXECPROCLOG

### Now check for any errors
CAD_RUN_ID_FILE=$SCRIPT_HOME/CAD_RunID.txt
ERRORS_FILE=$SCRIPT_HOME/CAD_Errors.txt

# Get the run id
sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/getCADRunID.sql $CAD_RUN_ID_FILE
CAD_RUN_ID=`sed -n '4,4p' $CAD_RUN_ID_FILE`
echo Latest CADRunID is $CAD_RUN_ID >> ${LOG_FILE}

# Get and check for any errors
sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/checkForCADErrors.sql $ERRORS_FILE $CAD_RUN_ID
COUNT_OF_ERRORS=`sed -n '6,6p' $ERRORS_FILE`

echo There are $COUNT_OF_ERRORS errors in latest CAD run >> ${LOG_FILE}

if [ $COUNT_OF_ERRORS -eq 0 ]
then
  echo No errors in CAD file, carrying on >> ${LOG_FILE}
else
  echo There are errors, sending an email for investigation >> ${LOG_FILE}
  email 'xhibit_support@cgi.com' 'CAD process has run with errors. Please investigate.' 'See subject...' ''
  email 'scott.atwell@cgi.com' 'CAD process has run with errors. Please investigate.' 'See subject...' ''
  email 'rupali.joshi@cgi.com' 'CAD process has run with errors. Please investigate.' 'See subject...' ''
  email 'uk-gen-mojascad@cgi.com' 'CAD process has run with errors. Please investigate.' 'See subject...' ''
  exit
fi


### Now get the CLOB data in the correct file - the filename must be C30xx.txt where xx is the month no, e.g. 3001.txt for January and 3012.xtt for December
#THISMONTH=`date '+%m'`

# Some string manipulation needed to convert the start date e.g. 01-MAY-19 which is part of a text file generated from SQL so that we can get the month name and finally convert into 2 digit month no for use with the 
# file we need to generate for CAD
STARTDATE=`sed -n '4,4p' $START_DATE_FILENAME`
STARTDATE=`echo ${STARTDATE} | cut -c4-6`

getMonth $STARTDATE
retVal=$?
ACTUALMONTH=$retVal

# Add a leading zero if needed for month number
if [ $retVal -le 9 ]
then
  ACTUALMONTH=0$retVal
fi

echo ACTUALMONTH=$ACTUALMONTH >> ${LOG_FILE}

CAD_CLOB_FILE=C30$ACTUALMONTH.txt
echo $CAD_CLOB_FILE >> ${LOG_FILE}
sqlplus <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/getCADFile.sql $CAD_CLOB_FILE


##############
#Zip the CAD file to be sent
echo "Zipping files.." >> ${LOG_FILE}
zip -jr $CAD_CLOB_FILE.zip $CAD_CLOB_FILE

ERROR_CODE=$?

if [ "$ERROR_CODE" != 0 ]; then
        echo "ERROR: Unable to zip files. error code=$ERROR_CODE" >> ${LOG_FILE}
	EMAIL_MESSAGE="ERROR: Unable to zip files. error code=$ERROR_CODE"
        email "xhibit_support@cgi.com" "$EMAIL_MESSAGE" "" ""
        email "scott.atwell@cgi.com" "$EMAIL_MESSAGE" "" ""
        email "rupali.joshi@cgi.com" "$EMAIL_MESSAGE" "" ""
        email "uk-gen-mojascad@cgi.com" "$EMAIL_MESSAGE" "" ""
        exit 1
fi

echo "Zipped CAD file." >> ${LOG_FILE}


# send zip file to DSS via sftp
STAGING_SERVER=x.y.198.9
STAGING_SERVER_USERID=xhibit_to_dss
STAGING_SERVER_PWD=JAM834mus!

echo "sftping file.." >> $LOG_FILE
sftp $STAGING_SERVER_USERID@$STAGING_SERVER <<EOF
lcd $STAGING3_FOLDER
put $CAD_CLOB_FILE.zip
EOF
ERROR_CODE=$?

if [ "$ERROR_CODE" != 0 ]; then
        echo "ERROR: Unable to sftp file. error code=$ERROR_CODE" >> $LOG_FILE
	EMAIL_MESSAGE="ERROR: Unable to sftp file. error code=$ERROR_CODE"
	email "xhibit_support@cgi.com" "$EMAIL_MESSAGE" "" ""
	email "scott.atwell@cgi.com" "$EMAIL_MESSAGE" "" ""
	email "rupali.joshi@cgi.com" "$EMAIL_MESSAGE" "" ""
        exit 1
fi

echo "sftp complete.." >> $LOG_FILE


### Archive data produced during this run
dateTime=`date '+%Y%m%d%H%M%S'`
mv C30*.txt* $SCRIPT_HOME/CAD_Files/
mv $EXECPROCLOG $SCRIPT_HOME/sql_Outputs/$EXECPROCLOG-${dateTime}

mv $CAD_RUN_ID_FILE $CAD_RUN_ID_FILE-${dateTime}
mv $CAD_RUN_ID_FILE* $SCRIPT_HOME/sql_Outputs/

mv $ERRORS_FILE $ERRORS_FILE-${dateTime}
mv $ERRORS_FILE* $SCRIPT_HOME/sql_Outputs/

mv $START_DATE_FILENAME $START_DATE_FILENAME-${dateTime}
mv $START_DATE_FILENAME* $SCRIPT_HOME/sql_Outputs/

mv $END_DATE_FILENAME $END_DATE_FILENAME-${dateTime}
mv $END_DATE_FILENAME* $SCRIPT_HOME/sql_Outputs/

mv $LOG_FILE $LOG_FILE-${dateTime}
mv $LOG_FILE* $SCRIPT_HOME/logs/

# Assume the above has worked
EMAIL_MESSAGE="CAD has been successfully run for period $START_DATE to $END_DATE"
echo $EMAIL_MESSAGE >> ${LOG_FILE}
email "xhibit_support@cgi.com" "$EMAIL_MESSAGE" "" ""
email "scott.atwell@cgi.com" "$EMAIL_MESSAGE" "" ""
email "rupali.joshi@cgi.com" "$EMAIL_MESSAGE" "" ""

exit

