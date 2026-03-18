#!/usr/bin/ksh

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
#SCRIPT_HOME=/opt/moj/home/wmbroker/scratchpad/vish
#. $SCRIPT_HOME/setEnv.sh
. /opt/moj/home/wmbroker/bin/cron/setEnv.sh


EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com xhibitsupport@hmcts.gsi.gov.uk "
#EMAIL_RECIPIENTS="vishwanath.mallya@logica.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk

rm $SCRIPT_HOME/validationData.txt
rm $SCRIPT_HOME/ASNs.txt
rm $SCRIPT_HOME/validationIDs.txt
rm $SCRIPT_HOME/missingCases.txt

todaysDate=`date +"%d%m%Y"`
OUTPUT=`sqlplus -s <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/selectRSEntries.sql $SCRIPT_HOME/ASNs.txt`
OUTPUT=`sqlplus -s <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/selectValidationEntries.sql $SCRIPT_HOME/validationIDs.txt`

while read line
do
if [ $line != "" ]
then  
OUTPUT1=`sqlplus -s <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/selectValidationData.sql $SCRIPT_HOME/validationData.txt $line`
fi 
done < $SCRIPT_HOME/validationIDs.txt

while read line
do
if [ $line != "" ]
then 
grepOutput=`grep ">$line<" $SCRIPT_HOME/validationData.txt` 
count=`echo $grepOutput|wc -c`
if [ "$count" -lt "2" ]
then

sqlOutput=`sqlplus -s <username>/<password>@csdbprd1 <<END
  set heading off feedback off
select case_type, case_number, c.court_name from xhb_case a, xhb_court c where case_id in (select case_id from aud_defendant_on_case where trunc(last_update_date)=trunc(sysdate) and results_verified='R' and ASN='$line') and a.court_id = c.court_id;
`

`echo $sqlOutput " " $line >> $SCRIPT_HOME/missingASNs_$todaysDate.txt`
`echo $sqlOutput >> $SCRIPT_HOME/missingCases.txt`
fi
fi 
done < $SCRIPT_HOME/ASNs.txt

OUTPUT=`cat $SCRIPT_HOME/missingCases.txt|wc -c`
if [ $OUTPUT > "1" ] 
then
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u Record Sheets are missing -s $SMTP_SERVER -m Verify the missing record sheets and resend. Details are in CSH2 ~/bin/cron/missingASNs_$todaysDate.txt. -a $SCRIPT_HOME/missingCases.txt"
  `$emailCmd`

fi

