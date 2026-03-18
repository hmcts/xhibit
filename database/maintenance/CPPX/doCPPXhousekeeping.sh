#!/usr/bin/ksh

#################################################################################
#
# Name: doCPPXhousekeeping.sh
# Created: January 2020
# Purpose: Runs the CPPX housekeeping jobs
#
#################################################################################

#SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live
SCRIPT_HOME=/home/wmbroker/bin/cron/CPPX   # CTC
#. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live
. $SCRIPT_HOME/setEnv.sh   # CTC

ORACLE_XHIBIT_DB_USER=xhibit
ORACLE_XHIBIT_DB_PASS=xhibit
LOGFILE=$SCRIPT_HOME/logs/HouseKeepingLogs_`date +"%d%m%y_%H%M%S"`.txt
FOLDER_FOR_VALID=$SCRIPT_HOME/processed/valid
FOLDER_FOR_STAGING=$SCRIPT_HOME/dssinbound
FOLDER_FOR_LOGS=$SCRIPT_HOME/logs
FOLDER_FOR_DEALTWITH=$SCRIPT_HOME/processed/dealtWith
FOLDER_FOR_INVALID=$SCRIPT_HOME/processed/invalid
FOLDER_FOR_ARCHIVED_INVALID=$SCRIPT_HOME/archived/processed/invalid/
FOLDER_FOR_ARCHIVED_VALID=$SCRIPT_HOME/archived/processed/valid
FOLDER_FOR_ARCHIVED_DEALTWITH=$SCRIPT_HOME/archived/processed/dealtWith
FOLDER_FOR_ACKNOWLEDGEMENT=$SCRIPT_HOME/archived/acknowledgements
FOLDER_FOR_ARCHIVED_ACKNOWLEDGEMENT=$SCRIPT_HOME/acknowledgements
FOLDER_FOR_ARCHIVED_LOGS=$SCRIPT_HOME/archived/logs/
TIME1__ARCHIVE=+6
TIME2__DELETE=+30

THISLOG=output/doCPPXhousekeeping_`date +"%d%m%y_%H%M%S"`.log
OUTPUT=`sqlplus -s ${ORACLE_XHIBIT_DB_USER}/${ORACLE_XHIBIT_DB_PASS}@${ORACLE_SID} << END
SET SERVEROUTPUT ON;
exec XHB_HOUSEKEEPING_PKG.PROCESS_CPP();
`

echo "$OUTPUT" > $THISLOG
echo Logs for doCPPXhousekeeping.sh `date` > $LOGFILE

#######
#LOGS - LOGS OVER 7 DAYS OLD MOVED TO ARCHIVED/logs
#######

echo "Moving logs over 7 days old to ARCHIVED/logs " >> $LOGFILE

find $FOLDER_FOR_LOGS -type f -mtime $TIME1__ARCHIVE -exec mv '{}' $FOLDER_FOR_ARCHIVED_LOGS \;

############
#STAGING - OUTSIDE OF WORKING HOURS SHOULD BE EMPTY
############

echo "Checking Staging folder....should be empty outside of working hours"

if [ "$(ls -A $FOLDER_FOR_STAGING)" ];then
		echo  $FOLDER_FOR_STAGING is not empty
		echo "$FOLDER_FOR_STAGING is not empty" >> $LOGFILE
else
		echo $FOLDER_FOR_STAGING is empty 
		echo "$FOLDER_FOR_STAGING is empty" >> $LOGFILE
fi	


##################
#PROCESS/VALID - MOVE FILES FROM processed/valid TO archived/processed/valid
##################

echo "Moving Files ..... from processed/valid TO archived/processed/valid "

echo mv $FOLDER_FOR_VALID/*.* $FOLDER_FOR_ARCHIVED_VALID/  >> $LOGFILE
mv $FOLDER_FOR_VALID/*.* $FOLDER_FOR_ARCHIVED_VALID/ 


##################
#PROCESS/DEALTWITH - MOVE FILES FROM processed/dealtWith TO archived/processed/dealtWith
##################

echo "Moving Files ..... from processed/dealtWith TO archived/processed/dealtWith "

echo mv $FOLDER_FOR_DEALTWITH/*.* $FOLDER_FOR_ARCHIVED_DEALTWITH/  >> $LOGFILE
mv $FOLDER_FOR_DEALTWITH/*.* $FOLDER_FOR_ARCHIVED_DEALTWITH/ 


#######################
#PROCESSED/INVALID - OUTSIDE OF WORKING HOURS SHOULD BE EMPTY
#######################

echo "Checking processed/invalid folder....should be empty outside of working hours"

if [ "$(ls -A $FOLDER_FOR_VALID)" ];then
		echo $FOLDER_FOR_INVALID is not empty
		echo "$FOLDER_FOR_INVALID is not empty" >> $LOGFILE
else
		echo $FOLDER_FOR_INVALID is empty 
		echo "$FOLDER_FOR_INVALID is empty" >> $LOGFILE
fi	


################################
#PROCESS/INVALID - Move files from processed/invalid to archived/processed/invalid
################################

echo "Moving Files ..... from processed/invalid TO archived/processed/invalid"

echo mv $FOLDER_FOR_INVALID/*.* $FOLDER_FOR_ARCHIVED_INVALID/ >> $LOGFILE
mv $FOLDER_FOR_INVALID/*.* $FOLDER_FOR_ARCHIVED_INVALID/


################################
#ARCHIVED/ACKNOWLEDGEMENTS - ZIP FILES OVER 7 DAYS OLD
################################

echo "Zip archived/processed/invalid files over 7 days old"  >> $LOGFILE
find $FOLDER_FOR_ARCHIVED_ACKNOWLEDGEMENT -type f -mtime $TIME1__ARCHIVE|xargs gzip -9

#####DELETE FILES OVER 30 DAYS OLD

echo "Deleting ......archived/processed/invalid files over 30 days old"  >> $LOGFILE
find $FOLDER_FOR_ARCHIVED_ACKNOWLEDGEMENT -type f -mtime $TIME2__DELETE -exec rm '{}' \;


################################
#ARCHIVED/PROCESSED/(IN)VALID - ZIP FILES OVER 7 DAYS OLD
################################

echo "Zip archived/processed/invalid files over 7 days old"  >> $LOGFILE

find $FOLDER_FOR_ARCHIVED_INVALID -type f -mtime $TIME1__ARCHIVE|xargs gzip -9
find $FOLDER_FOR_ARCHIVED_VALID -type f -mtime $TIME1__ARCHIVE|xargs gzip -9
find $FOLDER_FOR_ARCHIVED_DEALTWITH -type f -mtime $TIME1__ARCHIVE|xargs gzip -9

#####DELETE FILES OVER 30 DAYS OLD

echo "Deleting ......archived/processed/invalid files over 30 days old"  >> $LOGFILE

find $FOLDER_FOR_ARCHIVED_INVALID -type f -mtime $TIME2__DELETE -exec rm '{}' \;
find $FOLDER_FOR_ARCHIVED_VALID -type f -mtime $TIME2__DELETE -exec rm '{}' \;
find $FOLDER_FOR_ARCHIVED_DEALTWITH -type f -mtime $TIME2__DELETE -exec rm '{}' \;

###################
#ARCHIVED/LOGS - ZIP FILES OVER 7 DAYS OLD
###################

echo "Zip archived/logs files over 7 days old"  >> $LOGFILE
find $FOLDER_FOR_ARCHIVED_LOGS -type f -mtime $TIME1__ARCHIVE|xargs gzip -9
 
#####DELETE FILES OVER 30 DAYS OLD

echo "Deleting ......archived logs over 30 days old"  >> $LOGFILE
find $FOLDER_FOR_ARCHIVED_LOGS -type f -mtime $TIME2__DELETE -exec rm '{}' \;

exit
