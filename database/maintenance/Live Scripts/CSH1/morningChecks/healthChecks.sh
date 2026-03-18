#!/bin/ksh
#This script checks the count of WMB_MESSAGE_QUEUE_TABLE and alerts if it goes beyond 200

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/morningChecks
. $SCRIPT_HOME/setEnv.sh

WMB_MESSAGE_QUEUE_TABLE_COUNT=`sqlplus -s xhibit/xhibit@csdbprd1 <<END
  set heading off feedback off
select count(*) from wmb_message_queue_table;
`
if [ $WMB_MESSAGE_QUEUE_TABLE_COUNT -gt 200 ]
then
  EMAIL_RECIPIENTS="xhibit_support@logica.com"
  SUBJECT="XHIBIT Health Check Not Ok"
  MESSAGE="The current count of WMB_MESSAGE_QUEUE_TABLE table is $WMB_MESSAGE_QUEUE_TABLE_COUNT"
  EMAILCMD="$SCRIPT_HOME/sendemail.pl -f xhibitHealthChecks@justice.gov.uk -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $MESSAGE "
  $EMAILCMD
fi

