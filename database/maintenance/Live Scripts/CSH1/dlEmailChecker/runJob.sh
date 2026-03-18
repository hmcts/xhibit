#!/bin/ksh
# File:         emailDataCheck.sh
# Created:      05-March-2010
# Creator:      IRSimmons
# Purpose:      To nightly checks
#
#
SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/dlEmailChecker
. $SCRIPT_HOME/../setEnv.sh

TODATE="`date '+%Y%m%d%s'`"
LOGFILE=$SCRIPT_HOME/logs/$TODATE.log

. $SCRIPT_HOME/dl.sh > $LOGFILE

echo $MESSAGEBODY

echo SUBJECT is $SUBJECT
	$SCRIPT_HOME/sendEmail.sh $LOGFILE
