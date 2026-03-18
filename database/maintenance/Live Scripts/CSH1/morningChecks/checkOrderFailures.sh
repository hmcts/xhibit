#!/bin/ksh
SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/morningChecks
. $SCRIPT_HOME/setEnv.sh

ORDER_FAILURE_COUNT=`sqlplus -s <username>/<password>@csdbprd1 <<END
  set heading off feedback off
select count(*) from XHB_ORDER where ORDER_DELIVERY_STATUS_ID in (2,4) AND trunc(LAST_UPDATE_DATE) = trunc(sysdate-1) and order_template_id != 31;
`
if [ $ORDER_FAILURE_COUNT -gt 0 ]
then
#now sendmail
  SUBJECT="There are failed Orders in DB"
  MESSAGE="Order failure count: $ORDER_FAILURE_COUNT"
  EMAIL_RECIPIENTS="xhibit_support@logica.com"
  EMAILCMD="$SCRIPT_HOME/sendemail.pl -f xhibitOrderFailureCheck@justice.gov.uk -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $MESSAGE "
  $EMAILCMD
fi

