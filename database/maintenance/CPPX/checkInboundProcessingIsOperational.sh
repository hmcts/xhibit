#!/bin/bash

#################################################################################
#
# Name: checkInboundProcessingIsOperational.sh
# Created: September 2022
# Revised:
#
# Author(s): Scott Atwell
#
# Purpose: Checks if the processInboundLists_CPPX.sh script is running and if not it starts it
#
# How to use: Run this script to start the processing
#
#################################################################################

echo "Checking if the processing of lists script is running"
echo `basename $0`

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live
#SCRIPT_HOME=/opt/moj/home/wmbroker/scratchpad/scott/CP_CFRA1151 # for NLE/Live
#SCRIPT_HOME=/home/wmbroker/bin/cron/CPPX   # CTC
. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live
#. $SCRIPT_HOME/setEnv.sh   # CTC

LOGFILE=$SCRIPT_HOME/logs/CheckInboundProcessingIsOperational_logs_`date +"%d%m%y_%H%M%S"`.txt

# The filename that can be created so that the batch job will stop running
FILE_TO_STOP_PROCESS=stopProcessingCPLists.file

## Check if the file to signal that this process should end is in place
if [ -f $SCRIPT_HOME/$FILE_TO_STOP_PROCESS  ]; then
        echo "Processing of lists job will not be checked as file is present to stop it" >> $LOGFILE
        echo "Processing of lists job will not be checked as file is present to stop it"
        exit
fi


#########
#### Do the stuff!
#########
echo "Started" >> $LOGFILE
echo "Started"
PROCESS_NAME=processInboundLists_CPPX.sh
TEST=`pgrep -f $PROCESS_NAME`
NUMPROCESSES=`echo $TEST | wc -w`

## Note: Need to check for less than 1 existing processes
if [ $NUMPROCESSES -lt 1 ];then
        echo Process is not started, so starting process: Date:`date +"%d%m%y_%H%M%S"` >> $LOGFILE
        echo Process is not started, so starting process: Date:`date +"%d%m%y_%H%M%S"`
        nohup $SCRIPT_HOME/$PROCESS_NAME useSelectedCourts > $SCRIPT_HOME/logs/restartListProcess_`date +"%d%m%y_%H%M%S"`.txt 2>&1 &

else
        echo Confirmed that job to pick up CP lists is running: Date:`date +"%d%m%y_%H%M%S"` >> $LOGFILE
        echo Confirmed that job to pick up CP lists is running: Date:`date +"%d%m%y_%H%M%S"`
fi

echo "Finished" >> $LOGFILE
echo "Finished"

exit

