#!/bin/bash

#################################################################################
#
# Name: stopProcessFilesCPPX.sh
# Created: August 2020
# Revised: September 2020
#
# Author(s): Nia Walters
#
# Purpose: Ensure that the java class gets stopped 
#
# How to use: Run this script to stop the processing 
#
#################################################################################

echo "About to stop the processing of CPPX files" 
echo `basename $0`

#SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live
SCRIPT_HOME=/home/wmbroker/bin/cron/CPPX   # CTC
#. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live
. $SCRIPT_HOME/setEnv.sh   # CTC

FOLDER_FOR_VALID=$SCRIPT_HOME/processed/valid
echo "stop processing" > $FOLDER_FOR_VALID/stop.txt