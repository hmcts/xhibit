#!/bin/bash

#################################################################################
#
# Name: processInboundPDIWP_CPPX.sh
# Created: November 2019
# Revised: July 2022, September 2022
#
# Author(s): Ervin Patterson, Scott Atwell, Luke Gittins
#
# Purpose: Ensure that all incoming CPP PD and IWP files are picked up and initial processing/INITIAL_VALIDATION performed
#
# How to use: This script should be scheduled to run via crontab every minute between 0700 and 1830 Monday to Friday
#			  July Update - This script can be ran either on its own to get all inbound documents or by passing in "1" as a parameter
#							it will only get inbound documents for courts specified in the text file.
#			  September 2022 Update - Amended the original processInboubdCPPX.sh file such that it only dealswith Public Display and
#									  Internet Web Page data. Other data files from CP should be ignored.
#			  December 2022 Update - Fixed changes made in the rework. This script will now function as explained above in the September update.
#
#################################################################################

#SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live
SCRIPT_HOME=/home/wmbroker/bin/cron/CPPX   # CTC
#. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live
. $SCRIPT_HOME/setEnv.sh   # CTC

ORACLE_XHIBIT_DB_USER=xhibit
ORACLE_XHIBIT_DB_PASS=xhibit
PL_CONNECT_STRING=$ORACLE_XHIBIT_DB_USER/$ORACLE_XHIBIT_DB_PASS@$ORACLE_SID

# DSS details to be supplied
DSSDIRECTORY=$SCRIPT_HOME/dssinbound
#REMOTEDSSFOLDER=/
REMOTEDSSFOLDER=$SCRIPT_HOME/dssinbound
#USERNAME=XhibittoCPP # NLE
USERNAME=wmbroker
PASSWORD=somepass
SERVERNAME=x.x.8.209

# The filename that can be created so that the batch job will stop running
FILE_TO_STOP_PROCESS=stopProcessingCP_PDIWP.file
# Wait 10 seconds between checking BAIS for any lists
WAIT_TIME_BETWEEN_LIST_CHECKS=10


# this will be a copy of directory from dss

FOLDER_FOR_STAGING=$SCRIPT_HOME/staging
FOLDER_FOR_VALID=$SCRIPT_HOME/processed/valid
FOLDER_FOR_INVALID=$SCRIPT_HOME/processed/invalid
ARCHIVED_VALID=$SCRIPT_HOME/archived/processed/valid
ARCHIVED_INVALID=$SCRIPT_HOME/archived/processed/invalid
LOGFILE=$SCRIPT_HOME/logs/processInboundPDIWP_CPPX_logs_`date +"%d%m%y_%H%M%S"`.txt
FOLDER_FOR_SQL=$SCRIPT_HOME/sql
NUM_CHAR_LIMIT=239
FINAL_VALIDATION=NP


#### Email variables
EMAIL_RECIPIENTS=scott.atwell@cgi.com
EMAIL_SENDER=xhibit@test.cgi.com
EMAIL_SCRIPT=$SCRIPT_HOME/bin/sendemail.pl
SMTP_SERVER=x.y.z.a
TODAY=`date #+DATE: %d/%m/%y TIME: %H:%M:%S`
SUBJECT="CPP Process = The script processInboundCPPX.sh has been running for more than 10 mins, please investigate"

echo Logs for ProcessInboundPDIWP_CPPX.sh `date` > $LOGFILE


#########
#### Step 1 - check if we are the only local instance
#########
echo "Step 1 - started" >> $LOGFILE
echo "Step 1 - started"
echo `basename $0`
PROCESS_NAME=`basename $0`
TEST=`pgrep -f $PROCESS_NAME`
NUMPROCESSES=`echo $TEST | wc -w`
#echo NUMPROCESSES = $NUMPROCESSES

## Note: Need to check for greater than 1 existing process as THIS process will be counted when the check is done
### Even though one cron job may be running, there could be "several" processed that get generated, and the "grep" itself is also counted
if [ $NUMPROCESSES -gt 4 ];then

        PROCESS_TIME=`ps -o etime= -p "$TEST"`
        PROCESS_TIME_MIN=`echo $PROCESS_TIME | cut -c 1-2`
        PROCESS_TIME_SEC=`echo $PROCESS_TIME| cut -c 4-5`
        let CONVERT_TO_SEC=$PROCESS_TIME_MIN*60
        let TOTAL_PROCESS_TIME_SEC=$CONVERT_TO_SEC+$PROCESS_TIME_SEC

        if [ $TOTAL_PROCESS_TIME_SEC -gt 600 ];then
			# email BAU team process has been running for more than 10 mins
			echo "The script processInboundCPPX.sh has been running for more than 10 mins please resolve " >> $LOGFILE
			emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY "
			`$emailCmd`

        else
			echo "This script is already running with PID `pgrep $(basename $0)`" >> $LOGFILE
        fi

	exit
fi


echo "Checked for process already running...passed" >> $LOGFILE
echo "Checked for process already running...passed"
echo "Step 1 - ended" >> $LOGFILE
echo "Step 1 - ended"

#######
## Functions below
#######

#########
#### Step 2a - look for any inbound PublicDisplay documents to retrieve from staging server
#########
function step2a (){
	echo "Step 2a - started" >> $LOGFILE
	echo "Step 2a - started"
	cd $DSSDIRECTORY
	echo We are here: $DSSDIRECTORY
	echo Remote folder is $REMOTEDSSFOLDER/ and contains:: `ls $REMOTEDSSFOLDER/`
	echo PARAMS PRESENT: $1
	if [ $1 -eq 0 ]
	then
		echo "*** Fetching All Inbound Public Display Documents ***" >> $LOGFILE
		echo "*** Fetching All Inbound Public Display Documents ***" 
		sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'mget PublicDisplay* \n exit'
		echo Listing PD files retrieved - all >> $LOGFILE
		ls $DSSDIRECTORY >> $LOGFILE
		echo Done the sftp >> $LOGFILE
		echo Done the sftp
		echo Location = `pwd` >> $LOGFILE
		echo Location = `pwd`
	
		echo "Step 2a - ended" `date #+DATE: %d/%m/%y TIME: %H:%M:%S` >> $LOGFILE
		echo "Step 2a - ended"
		#Params called here are 1=All Courts, 2=No Crest Court Id, 3=Type, Public Display
		step2c 0 0 0
	else
		echo "*** Fetching Inbound Public Display Documents for courts in specified file ***" >> $LOGFILE
		echo "*** Fetching Inbound Public Display Documents for courts in specified file ***"
	  
		filename="$SCRIPT_HOME/ListOfCourts.txt"
		while read line; do
			echo "Court - $line";
		  
			CREST_COURT_ID=$(sqlplus -S $PL_CONNECT_STRING <<-END-OF-SQL
							SET HEADING OFF;
							select crest_court_id from xhb_court where court_id = $line;
							exit;
							END-OF-SQL)
		  
			TRIMMED_CREST_COURT_ID=`echo $CREST_COURT_ID | sed -e 's/^[[:space]]*//'`  
		  
			echo "$TRIMMED_CREST_COURT_ID" >> $LOGFILE
			echo "$TRIMMED_CREST_COURT_ID"
			
			SFTP_CMD="mget \"PublicDisplay*_"$TRIMMED_CREST_COURT_ID"_*\""
			echo sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $SFTP_CMD
			sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $SFTP_CMD
		
		done < $filename
		echo Done the sftp >> $LOGFILE
		echo Done the sftp
		echo Location = `pwd` >> $LOGFILE
		echo Location = `pwd`
		
		echo "Step 2a - ended" `date #+DATE: %d/%m/%y TIME: %H:%M:%S` >> $LOGFILE
		echo "Step 2a - ended"
		#Params called here are 1=Specifed Courts, 2=Crest Court Id, 3=Type, Public Display
		step2c 1 $TRIMMED_CREST_COURT_ID 0
	fi
}

#########
#### Step 2b - look for any inbound WebPage documents to retrieve from staging server
#########
function step2b (){
	echo "Step 2b - started" >> $LOGFILE
	echo "Step 2b - started"
	cd $DSSDIRECTORY
	echo We are here: $DSSDIRECTORY
	echo Remote folder is $REMOTEDSSFOLDER/ and contains:: `ls $REMOTEDSSFOLDER/`
	
	if [ $1 -eq 0 ]
	then
		echo "*** Fetching All Inbound WebPage Documents ***" >> $LOGFILE
		echo "*** Fetching All Inbound WebPage Documents ***" 
		#echo $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'mget WebPage* \n exit'
		sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'mget WebPage* \n exit'
		
		echo Done the sftp >> $LOGFILE
		echo Done the sftp
		echo Location = `pwd` >> $LOGFILE
		echo Location = `pwd`
		
		echo "Step 2b - ended" `date #+DATE: %d/%m/%y TIME: %H:%M:%S` >> $LOGFILE
		echo "Step 2b - ended"
		#Params called here are 0=All Courts, 2=No crest court ID, 3=Type, WebPage
		step2c 0 0 1
	else
		echo "*** Fetching Inbound WebPage Documents for courts in specified file ***" >> $LOGFILE
		echo "*** Fetching Inbound WebPage Documents for courts in specified file ***"
	  
		filename="$SCRIPT_HOME/ListOfCourts.txt"
		while read line; do
			echo "Court - $line";
		  
			CREST_COURT_ID=$(sqlplus -S $PL_CONNECT_STRING <<-END-OF-SQL
							SET HEADING OFF;
							select crest_court_id from xhb_court where court_id = $line;
							exit;
							END-OF-SQL)
		  
			TRIMMED_CREST_COURT_ID=`echo $CREST_COURT_ID | sed -e 's/^[[:space]]*//'`  
		  
			echo "$TRIMMED_CREST_COURT_ID" >> $LOGFILE
			echo "$TRIMMED_CREST_COURT_ID"
			
			SFTP_CMD="mget \"WebPage*_"$TRIMMED_CREST_COURT_ID"_*\""
			echo sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $SFTP_CMD
			sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $SFTP_CMD
		done < $filename
		echo Done the sftp >> $LOGFILE
		echo Done the sftp
		echo Location = `pwd` >> $LOGFILE
		echo Location = `pwd`
		
		echo "Step 2b - ended" >> $LOGFILE
		echo "Step 2b - ended"
		#Params called here are 1=Specifed Courts, 2=Crest Court Id, 3=Type, WebPage
		step2c 1 $TRIMMED_CREST_COURT_ID 1
	fi
}

#########
#### Step 2c - process the incoming files.
#########
function step2c (){
	echo "Step 2c - started" `date #+DATE: %d/%m/%y TIME: %H:%M:%S` >> $LOGFILE
	echo "Step 2c - started"
	cd $DSSDIRECTORY
	SEARCH_CRITERIA=PublicDisplay
	#Check if param passed in from step2a/step2b wants specific courts or all courts.
	#This then sets the number of files that will be processed.
	if [ $1 -eq 0 ]
	then
		#All courts
		if [ $3 -eq 0 ]
		then
			#PublicDisplay Files
			NUMINCOMINGFILES=`ls -1q $DSSDIRECTORY/*PublicDisplay* | wc -l | tr -d ' '` 
		else
			#WebPage Files
			NUMINCOMINGFILES=`ls -1q $DSSDIRECTORY/*WebPage* | wc -l | tr -d ' '` 
			SEARCH_CRITERIA=WebPage
		fi
	else
		#Specified Courts
		echo "Crest Court ID within step2c: "$2
		if [ $3 -eq 0 ]
		then
			#PublicDisplay Files with specified court
			NUMINCOMINGFILES=`ls -1q $DSSDIRECTORY/*PublicDisplay*_"$2"_* | wc -l | tr -d ' '` 
			SEARCH_CRITERIA=PublicDisplay*_"$2"_
		else
			#WebPage Files with specified court
			NUMINCOMINGFILES=`ls -1q $DSSDIRECTORY/*WebPage*_"$2"_* | wc -l | tr -d ' '` 
			SEARCH_CRITERIA=WebPage*_"$2"_
		fi
	fi
	
	echo $NUMINCOMINGFILES >> $LOGFILE
	echo $NUMINCOMINGFILES
	echo $SEARCH_CRITERIA

	if [ $NUMINCOMINGFILES -gt 0 ]; then
		echo "There are $NUMINCOMINGFILES files to process" >> $LOGFILE
		#This is looping through all files in the directory with the criteria set above
		for FILE in *$SEARCH_CRITERIA*
		do
			echo "File to process: $FILE" >> $LOGFILE
			echo "File to process: $FILE"
			mv $DSSDIRECTORY/$FILE $FOLDER_FOR_STAGING
			step3 $FILE
		done
		echo "Files moved to staging folder and removed from remote server" >> $LOGFILE
		echo "Files moved to staging folder and removed from remote server"
		touch $SCRIPT_HOME/messageLastReceivedTime.txt
	else
		echo "There are no files to process at this time" >> $LOGFILE
		echo "There are no files to process at this time"
	fi

	echo "Step 2c - ended" >> $LOGFILE
	echo "Step 2c - ended"
}

#########
#### Step 3 - Check filename is valid for each document found
#########
function step3 (){
	echo "Step 3 - started" `date #+DATE: %d/%m/%y TIME: %H:%M:%S` >> $LOGFILE
	echo "Step 3 - started"

	echo File to validate is $1 >> $LOGFILE
	echo File to validate is $1

	echo Location = `pwd` >> $LOGFILE
	echo Location = `pwd`
	cd $FOLDER_FOR_STAGING
	echo Folder for Staging: $FOLDER_FOR_STAGING >> $LOGFILE
	echo Folder for Staging: $FOLDER_FOR_STAGING

	echo Argument filename passed in = $1 >> $LOGFILE
	echo Argument filename passed in = $1

	#copy filenames to array split in 3 parts and validate each part if all parts valid insert into database

	declare -a TOVALIDATE
	declare -a PARTS
	VALIDATEFILE=$1

	echo VALIDATEFILE=$VALIDATEFILE >> $LOGFILE
	echo VALIDATEFILE=$VALIDATEFILE
	echo `pwd`

	PARTS=($(echo $VALIDATEFILE | sed -e "s/_/ /g" -e "s/.xml/ /g") )
	echo -ne "\n checking filename " >> $LOGFILE
	echo "\"${PARTS[@]}\"" >> $LOGFILE
	echo "\"${PARTS[0]}\"" >> $LOGFILE
	echo "\"${PARTS[1]}\"" >> $LOGFILE
	echo "\"${PARTS[2]}\"" >> $LOGFILE
	echo "\"${PARTS[3]}\"" >> $LOGFILE   ####cpp-116###

	#####cpp-116#####
	### checking file name is in correct format of Type_Code_Date
	if [ ${#PARTS[3]} -eq 0 ] && [ $VALIDATEFILE !=  "*" ]; then
		echo "Filename $VALIDATEFILE is the correct format of Type_Code_Date starting filename validation .... "
		echo "Filename $VALIDATEFILE is the correct format of Type_Code_Date starting filename validation .... " >> $LOGFILE

		#pass crest_court_id to variable to validate court code in filename

		CREST_COURT_ID=$(sqlplus -S $PL_CONNECT_STRING <<-END-OF-SQL
				select crest_court_id from xhb_court where cpp_court='Y';
				exit;
				END-OF-SQL)

		### Check Document Type
		if echo ${PARTS[0]} | egrep 'WebPage|PublicDisplay' ; then
			echo ${PARTS[0]} "Document label ok" >> $LOGFILE
			echo ${PARTS[0]} "Document label ok"
		else
			echo ${PARTS[0]} "Is an Invalid filename" >> $LOGFILE
			echo ${PARTS[0]} "Is an Invalid filename"
			echo "moving $VALIDATEFILE to invalid folder" >> $LOGFILE
			echo "moving $VALIDATEFILE to invalid folder"
			# move file to processed invalid folder
			mv -f $FOLDER_FOR_STAGING/$VALIDATEFILE $FOLDER_FOR_INVALID  >> $LOGFILE
		fi

		### Check Court Code
		if echo "$CREST_COURT_ID" | grep ${PARTS[1]} ;then
			echo ${PARTS[1]} "Court code ok" >> $LOGFILE
			echo ${PARTS[1]} "Court code ok"
		else
			echo ${PARTS[1]} "Is an Invalid court code" >> $LOGFILE
			echo ${PARTS[1]} "Is an Invalid court code"
			echo "moving $VALIDATEFILE to processed/invalid " >> $LOGFILE
			echo "moving $VALIDATEFILE to processed/invalid "
			# move file to processed invalid folder
			mv -f $FOLDER_FOR_STAGING/$VALIDATEFILE $FOLDER_FOR_INVALID >> $LOGFILE
		fi

		### Check DateTime
		date +"${PARTS[2]}"
		RES=$?

		echo $RES
		if [ 0$res -eq 0 ];then
			echo ${PARTS[2]} date OK >> $LOGFILE
			echo ${PARTS[2]} date OK
			echo "moving $VALIDATEFILE to processed/valid " >> $LOGFILE
			echo "moving $VALIDATEFILE to processed/valid "
			mv $FOLDER_FOR_STAGING/$VALIDATEFILE $FOLDER_FOR_VALID >> $LOGFILE

		else
			echo ${PARTS[2]} " is and Invalid date" >> $LOGFILE
			echo ${PARTS[2]} " is and Invalid date"
			echo "moving $VALIDATEFILE to processed/invalid " >> $LOGFILE
			echo "moving $VALIDATEFILE to processed/invalid "
			#move file to processed invalid folder
			mv -f $FOLDER_FOR_STAGING/$VALIDATEFILE $FOLDER_FOR_INVALID >> $LOGFILE
		fi
		
	#####cpp-116##### 
	else
		echo "moving $VALIDATEFILE to processed/invalid " >> $LOGFILE
		#move file to processed invalid folder
		if [ ${#PARTS[3]} -gt 0 ]; then
			#echo "${PARTS[3]} is not a valid filename format" 
			echo "echo ${PARTS[3]} is not a valid filename format " >> $LOGFILE
			echo "echo ${PARTS[3]} is not a valid filename format "
			mv -f $FOLDER_FOR_STAGING/$VALIDATEFILE $FOLDER_FOR_INVALID >> $LOGFILE
		fi
	fi
	#search processed valid folder and filter on WebPage, PublicDisplay

	echo "Step 3 - ended" `date #+DATE: %d/%m/%y TIME: %H:%M:%S` >> $LOGFILE
	echo "Step 3 - ended"
}

### while running
### check if the file to terminate the job exists, if so then quit
### if not then get a list, process it and then delete it
echo "About to loop" >> $LOGFILE
while true; do
	## Check if the file to signal that this process should end is in place
	if [ -f $SCRIPT_HOME/$FILE_TO_STOP_PROCESS  ]; then
		echo "Stopping processing of lists as file is present" >> $LOGFILE
		echo "Stopping processing of lists as file is present"
		exit
	fi
	
	### Are there any list files to be picked up?
	### If so, pick them all up, process those that are picked up one by one
	### As each file is processed remove it in BAIS so that it gets picked up once and only once
	
	#######
	## Parameter Detection
	#######

	if [ $# -eq 0 ]
	then
		step2a 0
		step2b 0
	else
		step2a 1
		step2b 1
	fi
	echo "All steps Complete" >> $LOGFILE
	echo "All steps Complete"
	### Once we've did a loop wait for X seconds nd alook for any new lists
	sleep $WAIT_TIME_BETWEEN_LIST_CHECKS
done;

exit
