#!/usr/bin/bash

###################################################
#
# Name: SendWeeklyCjitEmail.sh
#
# Created: 24th October 2011
#
# Amendments: 
#
# Purpose: Sends email with the weeks reports to XHIBIT Support team
#
#
# How to use: This script will be run automatically via a crontab job
#
# Return Codes: 11 = Exit due to validation failure
#               TODO 13 = Exit due to sql error or database access error
#               14 = Success
#
###################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/cjit/bin
. $SCRIPT_HOME/../../setEnv.sh
SUCCESS=true

cd $SCRIPT_HOME
rm CJITFiles.*
rm thisWeeksFiles/*

####
# Check that there are no arguments passed in
#
if [ $# -ne 0 ]
then
  echo "Error in $0 - Invalid Argument Count"
  SUCCESS=false
fi

CJIT_HOME=/opt/moj/home/wmbroker/bin/cron/cjit/results
CURR_DATETIME=`date +"%d-%m-%y %T"`
find $CJIT_HOME -mtime -8 -print -exec cp {} thisWeeksFiles \;
#find $CJIT_HOME -mtime -8 -print  > thisWeeksFileList.txt

tar cvf CJITFiles.tar thisWeeksFiles/*

gzip CJITFiles.tar

EMAIL_RECIPIENTS="scott.atwell@logicacmg.cjsm.net vishwanath.mallya@logicacmg.cjsm.net richard.williams@logicacmg.cjsm.net ryan.thomas@logicacmg.cjsm.net"
TODAY=`date '+DATE: %d/%m/%y TIME:%H:%M:%S'`
echo $TODAY

SUBJECT="CJIT Reports"
MESSAGE=''
ATTACHMENTS="CJITFiles.tar.gz"

EMAILCMD="$SCRIPT_HOME/../../../sendemail.pl -f xhibitlists@hmcourts-service.gsi.gov.uk -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY ; $MESSAGE -a $ATTACHMENTS "

$EMAILCMD

