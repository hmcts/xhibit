#!/bin/bash

#################################################################################
#
# Name: processInboundCPPX.sh
# Created: November 2019
# Revised: July 2022
#
# Author(s): Ervin Patterson, Scott Atwell, Luke Gittins
#
# Purpose: Ensure that all incoming CPP files are picked up and initial processing/INITIAL_VALIDATION performed
#
# How to use: This script should be scheduled to run via crontab every minute between 0700 and 1830 Monday to Friday
#			  July Update - This script can be ran either on its own to get all inbound documents or by passing in "1" as a parameter
#							it will only get inbound documents for courts specified in the text file.
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
REMOTEDSSFOLDER=/
USERNAME=wmbroker # CTC
#USERNAME=XhibittoCPP # NLE
PASSWORD=somepass
SERVERNAME=x.x.8.209

# this will be a copy of directory from dss

FOLDER_FOR_STAGING=$SCRIPT_HOME/staging
FOLDER_FOR_VALID=$SCRIPT_HOME/processed/valid
FOLDER_FOR_INVALID=$SCRIPT_HOME/processed/invalid
ARCHIVED_VALID=$SCRIPT_HOME/archived/processed/valid
ARCHIVED_INVALID=$SCRIPT_HOME/archived/processed/invalid
LOGFILE=$SCRIPT_HOME/logs/logs_`date +"%d%m%y_%H%M%S"`.txt
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


#### JAVA setup variables for inserting the CLOB
JAVA_HOME=$SCRIPT_HOME/jre1.6.0_45
PATH=$JAVA_HOME/bin:$PATH
CLASSPATH=$SCRIPT_HOME/jar/CPPX_Add_CLOB_1.6.jar:$SCRIPT_HOME/jar/wlfullclient.jar:$SCRIPT_HOME/jar/ojdbc6.jar:$SCRIPT_HOME/jar/log4j-1.2.13.jar:$SCRIPT_HOME/properties
MAINCLASS=uk.gov.courtservice.xhibit.cpp.scripts.AddCLOBObject
INITHEAP=64m
MAXHEAP=256m
ADDCLOBFROMFILE=Y
CLOBSTRING=USEFILEINSTEAD
WL_DATASOURCE_NAME=XhibitOracleTxDataSource # CTC
#WL_DATASOURCE_NAME=XhibitOracleTxExtDataSource # NLE
#WL_PROVIDER_URL=t3://10.63.208.150:7071 # NLE AS2
WL_PROVIDER_URL=t3://192.168.100.4:7071 # AS1
#WL_PROVIDER_URL=t3://192.168.8.223:7003 # SA Local Dev
JDBC_CONNECTION_STRING=jdbc:oracle:thin:@10.63.127.85:1521:o10tst4
#WL_CONTEXT_FACTORY=weblogic.jndi.WLInitialContextFactory
#NUM_TIMES_TO_TRY_TO_GET_DATASOURCE=5
DB_TEST_REQUIRED=false
CLOBID=0


echo Logs for ProcessInboundCPPX.sh `date` > $LOGFILE


#########
#### Step 1 - check if we are the only local instance
#########
echo "Step 1 - started" >> $LOGFILE
echo `basename $0`
PROCESS_NAME=`basename $0`
TEST=`pgrep -f $PROCESS_NAME`
NUMPROCESSES=`echo $TEST | wc -w`
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
                echo "The script processInboundCPPX.sh has been running for more than 10 mins please resolve " >> $LOGFILE
		emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY "
		`$emailCmd`

        else
                echo "This script is already running with PID `pgrep $(basename $0)`" >> $LOGFILE
        fi

	exit
fi


echo "Checked for process already running...passed"  >> $LOGFILE
echo "Step 1 - ended" >> $LOGFILE



#########
#### Step 2 - look for any inbound documents to retrieve from staging server
#########
echo "Step 2 - started" >> $LOGFILE
cd $DSSDIRECTORY
#echo We are here: $DSSDIRECTORY
#echo Remote folder is $REMOTEDSSFOLDER/ and contains:: `ls $REMOTEDSSFOLDER/`

#echo $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'rm *Restore* \n exit'
sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'rm *Restore* \n exit'

if [ $# -eq 0 ]
then
	echo "*** Fetching All Inbound Documents ***" 
	#echo $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'mget * \n exit'
	sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'mget * \n exit'
else
	echo "*** Fetching Inbound Documents for courts in specified file ***"
  
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
	
	SFTP_CMD="mget \"*_"$TRIMMED_CREST_COURT_ID"_*\""
	echo sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $SFTP_CMD
	sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $SFTP_CMD
	
	done < $filename
fi

echo Done the sftp >> $LOGFILE
echo Location = `pwd` >> $LOGFILE

NUMINCOMINGFILES=`ls -1q $DSSDIRECTORY | wc -l | tr -d ' '`
echo $NUMINCOMINGFILES
if [ $NUMINCOMINGFILES -gt 0 ]; then
        echo "There are $NUMINCOMINGFILES files to process" >> $LOGFILE

        for FILE in *
        do
                echo "File to process: $FILE" >> $LOGFILE
                mv $FILE $FOLDER_FOR_STAGING
        done

        ## Do we need a job to remove files from inbound dss folder
        sftp $USERNAME@$SERVERNAME:$REMOTEDSSFOLDER <<< $'rm * \n exit'
        echo "Files moved to staging folder and removed from remote server" >> $LOGFILE
		
		touch $SCRIPT_HOME/messageLastReceivedTime.txt
else
        echo "There are no files to process at this time" >> $LOGFILE
fi

echo Moved files that have been sftpd into staging folder >> $LOGFILE
echo "Step 2 - ended" >> $LOGFILE



#########
#### Step 3 - Check filename is valid for each document found
#########
echo "Step 3 - started" >> $LOGFILE

echo Location = `pwd` >> $LOGFILE
cd $FOLDER_FOR_STAGING
echo $FOLDER_FOR_STAGING >> $LOGFILE



#copy filenames to array split in 3 parts and validate each part if all parts valid insert into database

declare -a TOVALIDATE
declare -a PARTS
TOVALIDATE=(*)
echo $TOVALIDATE

for VALIDATEFILE in ${TOVALIDATE[@]};
do
    echo $VALIDATEFILE
    if [ -s $VALIDATEFILE ];
    then
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
            echo "Filename is the correct format of Type_Code_Date starting filename validation .... "
            echo  "Filename $VALIDATEFILE is the correct format of Type_Code_Date starting filename validation .... " >> $LOGFILE

            #pass crest_court_id to variable to validate court code in filename

                CREST_COURT_ID=$(sqlplus -S $PL_CONNECT_STRING <<-END-OF-SQL
                        select crest_court_id from xhb_court where cpp_court='Y';
                        exit;
                        END-OF-SQL)

                ### Check Document Type
                if echo ${PARTS[0]} | egrep 'DailyList|WarnedList|FirmList|WebPage|PublicDisplay' ; then
                    echo ${PARTS[0]} "Document label ok" >> $LOGFILE
                else
                    echo ${PARTS[0]} "Is an Invalid filename"  >> $LOGFILE
                    echo "moving $VALIDATEFILE to invalid folder"  >> $LOGFILE
                    # move file to processed invalid folder
                    mv -f $VALIDATEFILE $FOLDER_FOR_INVALID  >> $LOGFILE
                fi

                ### Check Court Code
                if [ -f $FOLDER_FOR_STAGING/$VALIDATEFILE  ];then
                    if echo "$CREST_COURT_ID" | grep ${PARTS[1]} ;then
                        echo ${PARTS[1]} "Court code ok" >> $LOGFILE
                    else
                        echo ${PARTS[1]} "Is an Invalid court code"
                        echo "moving $VALIDATEFILE to processed/invalid " >> $LOGFILE
                        # move file to processed invalid folder
                        mv -f $VALIDATEFILE $FOLDER_FOR_INVALID >> $LOGFILE
                    fi
                fi

                ### Check DateTime
                if [ -f $FOLDER_FOR_STAGING/$VALIDATEFILE  ];then
                        date +"${PARTS[2]}"
                        RES=$?

                        echo $RES
                        if [ 0$res -eq 0 ];then
                                echo ${PARTS[2]} date OK >> $LOGFILE
                                echo "moving $VALIDATEFILE to processed/valid " >> $LOGFILE
                                mv $VALIDATEFILE $FOLDER_FOR_VALID >> $LOGFILE

                        else
                                echo ${PARTS[2]} " is and Invalid date" >> $LOGFILE
                                echo "moving $VALIDATEFILE to processed/invalid " >> $LOGFILE
                    #move file to processed invalid folder
                    mv -f $VALIDATEFILE $FOLDER_FOR_INVALID >> $LOGFILE
                        fi
            fi
            
        #####cpp-116##### 
        else
            echo "moving $VALIDATEFILE to processed/invalid " >> $LOGFILE
                    #move file to processed invalid folder
            if [ ${#PARTS[3]} -gt 0 ]; then
                echo "${PARTS[3]} is not a valid filename format" 
                echo "echo ${PARTS[3]} is not a valid filename format " >> $LOGFILE
                        mv -f $VALIDATEFILE $FOLDER_FOR_INVALID >> $LOGFILE
            fi
        fi
    else
        echo $VALIDATEFILE is empty so will be moving to invalid folder >> $LOGFILE
        echo $VALIDATEFILE is empty so will be moving to invalid folder
        mv -f $VALIDATEFILE $FOLDER_FOR_INVALID >> $LOGFILE
    fi
done
#search processed valid folder and filter on DailyList,WarnedList,WebPage,PublicDisplay,FirmList

echo "Step 3 - ended" >> $LOGFILE

exit
