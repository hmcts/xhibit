#!/bin/ksh
# File:         emailDataCheck.sh
# Created:      05-March-2010
# Creator:      IRSimmons
# Purpose:      To nightly checks
#
#
SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/nightlyCheck
. $SCRIPT_HOME/../setEnv.sh

TODATE="`date '+%Y%m%d'`"
LOGFILE=$SCRIPT_HOME/logs/$TODATE.log

. $SCRIPT_HOME/autoPDataCheck.sh > $LOGFILE
echo SUBJECT is $SUBJECT
if [ -f $SCRIPT_HOME/logs/$TODATE.log ]
then
        chmod 777 $SCRIPT_HOME/logs/*
	$SCRIPT_HOME/sendEmail.sh $LOGFILE

	gzip $LOGFILE
	mv $SCRIPT_HOME/logs/*.log.gz $SCRIPT_HOME/Archive/
else
	echo failed
fi
