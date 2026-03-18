#!/usr/bin/bash

###################################################
#
# Name: run_list_archive.sh
#
# Created: 14th August 2012
#
# Amendments: 
#
# Purpose: Runs the xhb_archive.sh script to archive lists in XHIBIT. On day 1 of this script a seperate task has been undertaken
#	   to clear down all lists to only 3 months remaining.
#	   This job will be run daily and will ensure that 3 months worth of lists will be retained.
#
#	   No emails will be sent but the success status will be stored for potential use in service reporting jobs
#
#
# How to use: This script will be run automatically via a crontab job
#
# Return Codes: 11 = Exit due to validation failure
#               TODO 13 = Exit due to sql error or database access error
#               14 = Success
#
###################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/HK
. $SCRIPT_HOME/../setEnv.sh
SUCCESS=true
cd $SCRIPT_HOME

# This variable stores the name of the audit file to append to
# It assumes the file exists
CASE_HK_FILENAME=$SCRIPT_HOME/output/case_hk.txt


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


# Now kick off the housekeeping job
echo ./xhb_archive.sh C 2000 3 3 3 3 FALSE
./xhb_archive.sh C 2000 3 3 3 3 FALSE > $CASE_HK_FILENAME

echo All ok for the moment
exit 14

