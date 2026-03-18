#!/bin/bash

#################################################################################
#
# Name: processFilesCPPX.sh
# Created: August 2020
# Revised: September 2020
#
# Author(s): Nia Walters
#
# Purpose: Ensure that the java class gets run to pick up and process incoming CPP files 
#
# How to use: This script should be scheduled to run via crontab it will only every
# run one instance of the java file by checking the number of processFilesCPPX.sh script running at one time
#
#################################################################################

echo "About to check if processing of CPPX files is running and start if not" 
echo `basename $0`

#SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live
SCRIPT_HOME=/home/wmbroker/bin/cron/CPPX   # CTC
#. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live
. $SCRIPT_HOME/setEnv.sh   # CTC

#variables needed
PROCESS_NAME=`basename $0`
TEST=`pgrep -f $PROCESS_NAME`
NUMPROCESSES=`echo $TEST | wc -w`
#echo NUMPROCESSES = $NUMPROCESSES

CLASSPATH=$SCRIPT_HOME/properties:$SCRIPT_HOME/lib/CppFileProcessor.jar:$SCRIPT_HOME/lib/commons-pool2-2.4.3.jar:$SCRIPT_HOME/lib/commons-logging-1.0.3.jar:$SCRIPT_HOME/lib/commons-lang-2.6.jar:$SCRIPT_HOME/lib/commons-dbcp-1.4.jar:$SCRIPT_HOME/lib/commons-configuration-1.10.jar:$SCRIPT_HOME/lib/com.bea.core.apache.commons.pool_1.3.0.jar:$SCRIPT_HOME/lib/ojdbc6.jar:$SCRIPT_HOME/lib/log4j-1.2.13.jar::$SCRIPT_HOME/lib/ant.jar

LOGFILE=$SCRIPT_HOME/logs/logs_`date +"%d%m%y_%H%M%S"`.txt

if [ $NUMPROCESSES -gt 1 ];then
	echo Process already running >> $LOGFILE
else
	java -Xms64m -Xmx256m -classpath $CLASSPATH uk.gov.courtservice.xhibit.cpp.scripts.LighthouseCPP
	fileToDel=$SCRIPT_HOME/processed/valid/stop.txt
	if [ -f $fileToDel ] ; then
		rm $fileToDel
		echo `date +"%d%m%y_%H%M%S"` ': Deleted stop file'   >> $LOGFILE
	fi
fi

echo 'Stopped processFilesCPPX script'

exit 