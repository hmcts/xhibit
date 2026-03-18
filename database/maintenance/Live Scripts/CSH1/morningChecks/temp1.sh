#!/bin/ksh

LAA_ERRORS=`ls /opt/moj/home/wmbroker/bin/cron/LAA/workspace/|wc -l`
echo $LAA_ERRORS
if [ "$LAA_ERRORS" -gt "0" ]
then
	echo "ERRORS"
fi
