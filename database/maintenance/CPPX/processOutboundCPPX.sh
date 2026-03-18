#!/bin/bash

#################################################################################
#
# Name: processOutboundCPPX.sh
# Created: November 2019
# Revised: January 2020
#
# Author(s): Ervin Patterson, Scott Atwell
#
# Purpose: Ensure that all acknowledgements get generated and sent over to CPP
#
# How to use: This script should be scheduled to run via crontab every minute between 0700 and 1830 Monday to Friday
#
#################################################################################


SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live
. $SCRIPT_HOME/../setEnv.sh

ORACLE_XHIBIT_DB_USER=xhibit
ORACLE_XHIBIT_DB_PASS=xhibit
PL_CONNECT_STRING=$ORACLE_XHIBIT_DB_USER/$ORACLE_XHIBIT_DB_PASS@$ORACLE_SID

# DSS details to be supplied
DSSDIRECTORY=$SCRIPT_HOME/dssinbound
REMOTEDSSFOLDER=/
USERNAME=XhibittoCPP_Responses
PASSWORD=<fill this in>
SERVERNAME=<fill this in>

# Setup folders and files
FOLDER_FOR_STAGING=$SCRIPT_HOME/staging
FOLDER_FOR_VALID=$SCRIPT_HOME/processed/valid
FOLDER_FOR_INVALID=$SCRIPT_HOME/processed/invalid
LOGFILE=$SCRIPT_HOME/logs/logs_`date +"%d%m%y_%H%M%S"`.txt
FOLDER_FOR_SQL=$SCRIPT_HOME/sql
FOLDER_FOR_ARCHIVED_INVALID=$SCRIPT_HOME/archived/processed/invalid
FOLDER_FOR_ACKNOWLEDGEMENT=$SCRIPT_HOME/acknowledgements
FOLDER_FOR_ARCHIVED_ACKNOWLEDGEMENT=$SCRIPT_HOME/archived/acknowledgements


#### Email variables
EMAIL_RECIPIENTS=scott.atwell@cgi.com
EMAIL_SENDER=xhibitcsh@test.justice.gov.uk
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=<fill this in>
TODAY=`date '+DATE: %d/%m/%y TIME:%H:%M:%S'`
SUBJECT="CPP Process - The script processOutboundCPPX.sh has been running for more than 10 mins please investigate"


# needs to be added to the run once script---ep
mkdir -p $FOLDER_FOR_ACKNOWLEDGEMENT 

echo Logs for ProcessOutboundCPPX.sh `date` > $LOGFILE 


#########
#### Step 1 - check if we are the only local instance
#########
echo "Step 1 - started" >> $LOGFILE
echo `basename $0` >> $LOGFILE
PROCESS_NAME=`basename $0`
TEST=`pgrep -f $PROCESS_NAME`
NUMPROCESSES=`echo $TEST | wc -l`
#echo NUMPROCESSES = $NUMPROCESSES

## Note: Need to check for greater than 1 existing process as THIS process will be counted when the check is done
if [ $NUMPROCESSES -gt 1 ];then

	PROCESS_TIME=`ps -o etime= -p "$TEST"`
	PROCESS_TIME_MIN=`echo $PROCESS_TIME | cut -c 1-2`
	PROCESS_TIME_SEC=`echo $PROCESS_TIME| cut -c 4-5`
    	let CONVERT_TO_SEC=$PROCESS_TIME_MIN*60
	let TOTAL_PROCESS_TIME_SEC=$CONVERT_TO_SEC+$PROCESS_TIME_SEC
	

        if [ $TOTAL_PROCESS_TIME_SEC -gt 600 ];then
                # email BAU team process has been running for more than 10 mins
                echo "The script processOutboundCPPX.sh has been running for more than 10 mins, sending an email to BAU team to investigate" >> $LOGFILE
                emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY "
                `$emailCmd`
        else
                echo "This script is already running with PID `pgrep $(basename $0)`" >> $LOGFILE
        fi

        exit

fi

echo Checked for process already running...passed >> $LOGFILE
echo "Step 1 - ended" >> $LOGFILE


#########
#### Step 2 - Check for invalid documents and create acknowledgment file
#########
echo "Step 2 - starting" >> $LOGFILE
cd $FOLDER_FOR_INVALID
NUMINVALIDFILES=`ls -1q $FOLDER_FOR_INVALID | wc -l`

if [ $NUMINVALIDFILES -gt 0 ]; then
	echo "There are $NUMINVALIDFILES to send acknowledgements for" >> $LOGFILE
	declare -a INVALIDFILES

	INVALIDFILES=(*)
	echo $INVALIDFILES

	for INVALIDFILENAME in ${INVALIDFILES[@]};
	do 			
		DOCUMENT_NAME=`echo $INVALIDFILENAME  | cut -d '.' -f1`
		DOCUMENT_NAME_INVALID=`echo "${DOCUMENT_NAME}_Response_$(date +"%d%m%y_%H%M%S").xml" `
		touch $DOCUMENT_NAME_INVALID
		echo "Invalid Document Filename" >> $DOCUMENT_NAME_INVALID
			
		mv $FOLDER_FOR_INVALID/$DOCUMENT_NAME_INVALID $FOLDER_FOR_ACKNOWLEDGEMENT  >> $LOGFILE 
		echo Moved files to ACKNOWLEDGMENT folder >> $LOGFILE 
			
		mv $FOLDER_FOR_INVALID/$INVALIDFILENAME $FOLDER_FOR_ARCHIVED_INVALID  >> $LOGFILE 				
		echo Moved files to ARCHIVED folder >> $LOGFILE
	done
else
	echo "There are no invalid files to process at this time." >> $LOGFILE
fi

echo "Step 2 - ended" >> $LOGFILE



#########
#### Step 3 - Check for records where validation has failed and create acknowledgment file
#########
echo "Step 3 - starting" >> $LOGFILE
cd $FOLDER_FOR_ACKNOWLEDGEMENT

VFDOCUMENTS_STAGING_INBOUND=$(sqlplus -S $PL_CONNECT_STRING <<-END-OF-SQL 	
	SET PAGESIZE 0;
	SET LINESIZE 240;
	SET HEAD OFF;
	SET FEEDBACK OFF;
	SET NEWPAGE 0;
	SET SPACE 0;
	SET ECHO OFF;
	SET VERIFY OFF;
	SET TERMOUT OFF;
	SET SHOWMODE OFF;
	select DOCUMENT_NAME from XHB_CPP_STAGING_INBOUND where VALIDATION_STATUS = 'VF' and ACKNOWLEDGMENT_STATUS is NULL;
	exit;
	END-OF-SQL)

declare -a VFDOCUMENTSARRAY	
VFDOCUMENTSARRAY=$VFDOCUMENTS_STAGING_INBOUND
NUMACKSHERE=`echo ${VFDOCUMENTSARRAY} | wc -w | tr -d ' '`
echo "Acknowledging ${NUMACKSHERE} records where validation has failed" >> $LOGFILE


for EACHVFDOCUMENT in ${VFDOCUMENTSARRAY[@]};
do
	VFDOCUMENT_NAME=`echo $EACHVFDOCUMENT  | cut -d '.' -f1`
	VFDOCUMENT_NAME_INVALID=`echo "${VFDOCUMENT_NAME}_Response_$(date +"%d%m%y_%H%M%S").xml" `
	touch $VFDOCUMENT_NAME_INVALID
	echo "Schema validation failed for document" > $VFDOCUMENT_NAME_INVALID
done				

UPDATEVFOUTPUT=$(sqlplus -S $PL_CONNECT_STRING <<-END-OF-SQL  
		UPDATE XHB_CPP_STAGING_INBOUND SET ACKNOWLEDGMENT_STATUS = 'AS' where VALIDATION_STATUS = 'VF' and ACKNOWLEDGMENT_STATUS is NULL;
		COMMIT;
		exit;
		END-OF-SQL)

echo $UPDATEVFOUTPUT >> $LOGFILE

echo "Step 3 - ended" >> $LOGFILE



#########
#### Step 4 - Check for records where validation was successful and create acknowledgment file
#########
echo "Step 4 - starting" >> $LOGFILE
cd $FOLDER_FOR_ACKNOWLEDGEMENT

VSDOCUMENTS_STAGING_INBOUND=$(sqlplus -S $PL_CONNECT_STRING <<-END-OF-SQL 	
	SET PAGESIZE 0;
	SET LINESIZE 240;
	SET HEAD OFF;
	SET FEEDBACK OFF;
	SET NEWPAGE 0;
	SET SPACE 0;
	SET ECHO OFF;
	SET VERIFY OFF;
	SET TERMOUT OFF;
	SET SHOWMODE OFF;
	select DOCUMENT_NAME from XHB_CPP_STAGING_INBOUND where VALIDATION_STATUS = 'VS' and ACKNOWLEDGMENT_STATUS is NULL;
	exit;
	END-OF-SQL)

declare -a VSDOCUMENTSARRAY
VSDOCUMENTSARRAY=$VSDOCUMENTS_STAGING_INBOUND
NUMACKSHERE=`echo ${VSDOCUMENTSARRAY} | wc -w | tr -d ' '`
echo "Acknowledging ${NUMACKSHERE} records where validation was successful" >> $LOGFILE

for EACHVSDOCUMENT in ${VSDOCUMENTSARRAY[@]};
do
	VSDOCUMENT_NAME=`echo $EACHVSDOCUMENT  | cut -d '.' -f1`
	VSDOCUMENT_NAME_INVALID=`echo "${VSDOCUMENT_NAME}_Response_$(date +"%d%m%y_%H%M%S").xml" `
	touch $VSDOCUMENT_NAME_INVALID
	#echo Acknowledgement file created >> $LOGFILE
done	

UPDATEVSOUTPUT=$(sqlplus -S $PL_CONNECT_STRING <<-END-OF-SQL  
		UPDATE XHB_CPP_STAGING_INBOUND SET ACKNOWLEDGMENT_STATUS = 'AS' where VALIDATION_STATUS = 'VS' and ACKNOWLEDGMENT_STATUS is NULL;
		COMMIT;
		exit;
		END-OF-SQL)
			
echo $UPDATEVSOUTPUT >> $LOGFILE

echo "Step 4 - ended" >> $LOGFILE



#########
#### Step 5 - Sftp acknowledgment files to Staging Server 
#########
echo "Step 5 - starting" >> $LOGFILE
## Only try sftp if there are files to send
NUMACKS=`ls -1q $FOLDER_FOR_ACKNOWLEDGEMENT | wc -l`

if [ $NUMACKS -gt 0 ]; then
	echo "Sftp'ing $NUMACKS acknowledgments to staging server" >> $LOGFILE
	sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'mput *\n exit' 

	## Finally archive acknowledgment files
	mv $FOLDER_FOR_ACKNOWLEDGEMENT/* $FOLDER_FOR_ARCHIVED_ACKNOWLEDGEMENT

	echo Done the sftp >> $LOGFILE
else
	echo "There are no acknowledgments to send at this time" >> $LOGFILE
fi

exit

