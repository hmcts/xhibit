#!/usr/bin/ksh

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com xhibitsupport@hmcts.gsi.gov.uk "
#EMAIL_RECIPIENTS="vishwanath.mallya@logica.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk


OUTPUT=`sqlplus -s <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/query_formatting_document_failures.sql`
echo $OUTPUT
OUTPUT=`echo $OUTPUT|cut -f3 -d ' '`
echo $OUTPUT

if [ $OUTPUT != "0" ]
then

sqlOutput=`sqlplus -s <username>/<password>@csdbprd1 <<END
  set heading off feedback off
select count(*), c.court_name,'||' from XHB_DOCUMENT_CONTROL a, XHB_COURT c where a.status='FD' and a.creation_date>=sysdate-4/24 and a.creation_date<=sysdate-1/24 and a.court_id=c.court_id group by c.court_name;
`
wc=`echo $sqlOutput|wc -w|tr -s ' ' ''`
wc=`expr $wc - 1`
sqlOutput=`echo $sqlOutput|sed -e 's/||/,/g'|cut -f1-$wc -d ' '`
echo $sqlOutput





OUTPUT1=`sqlplus -s <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/update_formatting_document_failures.sql`
echo $OUTPUT1
OUTPUT1=`echo $OUTPUT1|cut -f1 -d ' '`
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $OUTPUT1 list/letter distribution record(s) resent -s $SMTP_SERVER -m $OUTPUT1 document(s) have been resent. Details are : \n  $sqlOutput"
  `$emailCmd`

fi

