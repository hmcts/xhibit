#!/bin/bash

#################################################################################
#
# Name: processInboundLists_CPPX.sh
# Created: November 2019
# Revised: July 2022, September 2022, December 2022
#
# Author(s): Ervin Patterson, Scott Atwell, Luke Gittins
#
# Purpose: Ensure that all incoming CPP files are picked up and initial processing/INITIAL_VALIDATION performed
#
# How to use: This script should be scheduled to run via crontab every minute between 0700 and 1830 Monday to Friday
#			  July Update - This script can be ran either on its own to get all inbound documents or by passing in "1" as a parameter
#							it will only get inbound documents for courts specified in the text file.
#			  September 2022 Update - Amended the original processInboundCPPX.sh file such that it only deals with Listing data,
#									  and processes them all individually such that none will get deleted before it has been processed.
#									  Other data files from CP should be ignored.
#			  December 2022 Update 	- Fixed changes made in the rework. This script will now function as explained above in the September update.
#			  Pre-July 2024			- Some updates have been made since September 2022 that are undocumented
#			  July 2024 Update		- Changes for PDDA to only process selected courts
#
#################################################################################

#SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live
SCRIPT_HOME=/home/wmbroker/bin/cron/CPPX   # CTC
. $SCRIPT_HOME/setEnv.sh   # CTC

ORACLE_XHIBIT_DB_USER=xhibit
ORACLE_XHIBIT_DB_PASS=xhibit
PL_CONNECT_STRING=$ORACLE_XHIBIT_DB_USER/$ORACLE_XHIBIT_DB_PASS@$ORACLE_SID

# DSS details to be supplied
DSSDIRECTORY=$SCRIPT_HOME/dssinbound
#REMOTEDSSFOLDER=/ # NLE
REMOTEDSSFOLDER=$SCRIPT_HOME/dssinbound
#USERNAME=XhibittoCPP # NLE
USERNAME=wmbroker
PASSWORD=somepass
SERVERNAME=x.x.8.209

# The filename that can be created so that the batch job will stop running
FILE_TO_STOP_PROCESS=stopProcessingCPLists.file
# Wait 10 seconds between checking BAIS for any lists
WAIT_TIME_BETWEEN_LIST_CHECKS=10

# this will be a copy of directory from dss

FOLDER_FOR_STAGING=$SCRIPT_HOME/staging
FOLDER_FOR_VALID=$SCRIPT_HOME/processed/valid
FOLDER_FOR_INVALID=$SCRIPT_HOME/processed/invalid
ARCHIVED_VALID=$SCRIPT_HOME/archived/processed/valid
ARCHIVED_INVALID=$SCRIPT_HOME/archived/processed/invalid
LOGFILE=$SCRIPT_HOME/logs/processInboundLists_CPPX_logs_`date +"%d%m%y_%H%M%S"`.txt
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

echo Logs for ProcessInboundLists_CPPX.sh `date` > $LOGFILE


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

	echo "This script is already running with PID `pgrep $(basename $0)`" >> $LOGFILE
	echo "This script is already running with PID `pgrep $(basename $0)`"
	
	exit
else
	echo "Checked for process already running...passed" >> $LOGFILE
	echo "Checked for process already running...passed"
fi

echo "Step 1 - ended" >> $LOGFILE
echo "Step 1 - ended"

#######
## Next part of the script is underneath the functions below
#######


#######
## Functions below
#######

#########
#### Step 2a - look for any inbound list documents to retrieve from staging server
#########
function step2a () {
        echo "Step 2a - started" `date #+DATE: %d/%m/%y TIME: %H:%M:%S` >> $LOGFILE
        echo "Step 2a - started"
        cd $DSSDIRECTORY
        #echo We are here: $DSSDIRECTORY
        #echo Remote folder is $REMOTEDSSFOLDER/ and contains:: `ls $REMOTEDSSFOLDER/`
        echo Dollar-hash - $# >> $LOGFILE

        if [ $# -eq 0 ]
        then
                echo "*** Fetching inbound documents from all courts ***" >> $LOGFILE
                echo "*** Fetching inbound documents from all courts ***"

                #echo $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'mget *List* \n exit'
                sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'mget *List* \n exit'
        else
                echo "*** Fetching Inbound Documents for courts in specified file ***" >> $LOGFILE
                echo "*** Fetching Inbound Documents for courts in specified file ***"

                filename="$SCRIPT_HOME/ListOfCourts.txt"
                while read line; do
                        echo "Court - $line"; >> $LOGFILE
                        echo "Court - $line";
		  
						TRIMMED_CREST_COURT_ID=`echo $line`
		  
						echo "Crest Court ID - $TRIMMED_CREST_COURT_ID" >> $LOGFILE
						echo "Crest Court ID - $TRIMMED_CREST_COURT_ID"
						
						SFTP_CMD="mget \"*List*_"$TRIMMED_CREST_COURT_ID"_*\""
                        echo sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $SFTP_CMD >> $LOGFILE
                        echo sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $SFTP_CMD
                        sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $SFTP_CMD

                done < $filename
        fi

        echo Done the sftp >> $LOGFILE
        echo Done the sftp
        echo Location = `pwd` >> $LOGFILE
        echo Location = `pwd`

        echo "Step 2a - ended" `date #+DATE: %d/%m/%y TIME: %H:%M:%S` >> $LOGFILE
        echo "Step 2a - ended"
}


#########
#### Step 2b - process the files that have been retrieved
#########
function step2b () {
        echo "Step 2b - started" `date #+DATE: %d/%m/%y TIME: %H:%M:%S` >> $LOGFILE
        echo "Step 2b - started"
        cd $DSSDIRECTORY

        NUMINCOMINGFILES=`ls -1q $DSSDIRECTORY/*List* | wc -l | tr -d ' '`
        if [ $NUMINCOMINGFILES -gt 0 ]; then
                echo "There are $NUMINCOMINGFILES files to process" >> $LOGFILE
                echo "There are $NUMINCOMINGFILES files to process"

                for FILE in *List*
                do
                        echo "File to process: $FILE" >> $LOGFILE
                        echo "File to process: $FILE"
                        # Move file to staging folder for processing
                        mv $DSSDIRECTORY/$FILE $FOLDER_FOR_STAGING
                        step3 $FILE

                        ## Remove each file as it is processed
                        echo REMOVE_FILE_CMD="rm $FILE \n exit" >> $LOGFILE
                        echo REMOVE_FILE_CMD="rm $FILE \n exit"
                        REMOVE_FILE_CMD="rm $FILE \n exit"
                        echo REMOVE_FILE_CMD = $REMOVE_FILE_CMD >> $LOGFILE

                        echo sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $REMOVE_FILE_CMD >> $LOGFILE
                        echo sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $REMOVE_FILE_CMD
                        sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $REMOVE_FILE_CMD

                        echo Removed file from BAIS >> $LOGFILE
                        echo Removed file from BAIS
                done

                ## Do we need a job to remove files from inbound dss folder
                echo "Files moved to staging folder and removed from remote server" >> $LOGFILE
                echo "Files moved to staging folder and removed from remote server"

                touch $SCRIPT_HOME/messageLastReceivedTime.txt
        else
                echo "There are no files to process at this time" >> $LOGFILE
                echo "There are no files to process at this time"
        fi

        echo "Step 2b - ended" `date #+DATE: %d/%m/%y TIME: %H:%M:%S` >> $LOGFILE
        echo "Step 2b - ended"
}

#########
#### Step 3 - Check filename is valid for each document found
#########
function step3 () {
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
        echo `ls *List*`

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
                if echo ${PARTS[0]} | egrep 'DailyList|WarnedList|FirmList' ; then
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
        #search processed valid folder and filter on DailyList,WarnedList,FirmList

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
        step2a selected
        step2b

        ### Once we've did a loop wait for X seconds nd alook for any new lists
        sleep $WAIT_TIME_BETWEEN_LIST_CHECKS
done;

exit