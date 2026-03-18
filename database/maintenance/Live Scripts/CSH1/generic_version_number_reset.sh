#!/usr/bin/ksh

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com "
#EMAIL_RECIPIENTS="vishwanath.mallya@logica.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk

for TABLE in XHB_CREST_IMPORT XHB_CR_LIVE_DISPLAY XHB_CR_LIVE_INTERNET XHB_ORDER_TYPE XHB_CR_LIVE_STATUS XHB_INTERNET_HTML
do
echo $TABLE
TRIGGER_NAME="$TABLE"_BUR_TR
echo $TRIGGER_NAME
OUTPUT=`sqlplus -s <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/reset_version_number_generic.sql $TRIGGER_NAME $TABLE`
echo $OUTPUT

checkDBIssue=`echo $OUTPUT|grep ORA|wc -l`
if [ $checkDBIssue -gt "0" ]
then
exit
fi

OUTPUT=`echo $OUTPUT|cut -f1 -d ' '`
echo $OUTPUT


if [ $OUTPUT != "0" ]
then
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $TABLE version has been reset -s $SMTP_SERVER -m Please monitor this table for any issues"
  `$emailCmd`

fi
done

