#!/usr/bin/ksh

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com "
#EMAIL_RECIPIENTS="vishwanath.mallya@logica.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk


OUTPUT=`sqlplus -s <username>/<password>@${ORACLE_SID2} @$SCRIPT_HOME/sql/insertDARTSMessage.sql`
echo $OUTPUT
OUTPUT=`echo $OUTPUT|cut -f1 -d ' '`
echo $OUTPUT

if [ $OUTPUT != "1" ]
then
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u DARTS error -s $SMTP_SERVER -m Could not insert test record into DARTS database. Please check DARTS database for any issues."
  `$emailCmd`

fi

