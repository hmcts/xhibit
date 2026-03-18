#!/usr/bin/ksh

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
#EMAIL_RECIPIENTS="xhibit_support@logica.com "
EMAIL_RECIPIENTS="vishwanath.mallya@cgi.com leigh.jermin@cgi.com mike.piper@cgi.com richard.griffiths@cgi.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk
EMAIL_MESSAGE="There is a duplicate CREST_DEFENDANT_ID record "
RESTART_SUCCESSFUL="Duplicate Defendant ID detected"
RESTART_UNSUCCESSFUL="webLogic Restart Unsuccessful"
EMAIL_SUBJECT="Duplicate Defendant ID detected"

sqlOutput=`sqlplus -s <username>/<password>@csdbprd1 <<END
  set heading off feedback off
select count(*) from xhb_defendant where
CREST_DEFENDANT_ID in (select max(CREST_DEFENDANT_ID) from xhb_defendant);
`

echo _ $sqlOutput _
sqlOutput=`echo $sqlOutput|tr -s ' '|cut -f1 -d ' '`
echo _ $sqlOutput _

if [ "$sqlOutput" -gt "1" ]
then
sqlOutput1=`sqlplus -s xhibit/xhibit@csdbprd1 <<END
  set heading off feedback off
select max(CREST_DEFENDANT_ID) from xhb_defendant;
`
sqlOutput1=`echo $sqlOutput1|tr -s ' '|cut -f1 -d ' '`

  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $EMAIL_SUBJECT -s $SMTP_SERVER -m $EMAIL_MESSAGE $sqlOutput1"
  `$emailCmd`
fi

