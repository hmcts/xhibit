#!/usr/bin/ksh

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com "
#EMAIL_RECIPIENTS="vishwanath.mallya@logica.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk

DAY=`date|cut -f1 -d ','`
NEXT_DAY=1

if [ "$DAY" = "Friday" ]
then
NEXT_DAY=3
fi

sqlOutput=`sqlplus -s <username>/<password>@csdbprd1 <<END
  set heading off feedback off
select a.court_name from mtbl_merc_dl_storage b, xhb_court a where
b.ADDITIONAL3 = to_char(sysdate+$NEXT_DAY,'yyyy-mm-dd')||' 00:00:00'
AND b.DL_TRIGGER <> 'C' 
and b.MESSAGE like '%DRAFT%'
and b.MESSAGE not like '%FINAL%'
and b.fromid=A.CREST_COURT_ID
order by a.court_name;
`

echo $sqlOutput
wc=`echo $sqlOutput|wc -w`
echo $wc
sqlOutput=`echo $sqlOutput|sed -e 's/-//g'`

if [ $wc -gt "0" ]
then
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u Missing Final List -s $SMTP_SERVER -m Court(s) with a draft list and without a final list: $sqlOutput."
  `$emailCmd`

fi

