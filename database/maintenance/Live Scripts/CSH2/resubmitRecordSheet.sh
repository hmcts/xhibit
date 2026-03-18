#!/usr/bin/ksh

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. /opt/moj/home/wmbroker/bin/cron/setEnv.sh


EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com"
#EMAIL_RECIPIENTS="vishwanath.mallya@cgi.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk

todaysDate=`date +"%d%m%Y"`
yesterdaysDate=`TZ=GMT+24 date +"%d%m%Y"`


if [ -f $SCRIPT_HOME/missingASNs_$yesterdaysDate.txt ]
then

while read line
do
ASN=`echo $line|tr -s ' '|awk '{print $NF}'`
echo ASN=$ASN
CASE_TYPE=`echo $line|tr -s ' '|cut -f1 -d ' '`
echo CASE_TYPE=$CASE_TYPE
CASE_NUMBER=`echo $line|tr -s ' '|cut -f2 -d ' '`
echo CASE_NUMBER=$CASE_NUMBER
NO_OF_FIELDS=`echo $line|tr -s ' '|awk '{print NF-1}'`
echo NO_OF_FIELDS=$NO_OF_FIELDS
COURT_NAME=`echo $line|tr -s ' '|cut -f3-${NO_OF_FIELDS} -d ' '`
echo COURT_NAME=**$COURT_NAME**
#line=`echo $line|tr -s ' '|cut -f4 -d ' '`
echo $line
echo $line >> $SCRIPT_HOME/$yesterdaysDate.tmp
OUTPUT1=`sqlplus -s <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/checkDefendantOnCaseCount.sql $ASN $CASE_TYPE $CASE_NUMBER "$COURT_NAME"`
echo $OUTPUT1
OUTPUT1=`echo $OUTPUT1|tr -s ' '|cut -f1 -d ' '`
echo $OUTPUT1

if [ "$OUTPUT1" -gt "1" ]
then
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u Record Sheet resubmission failed -s $SMTP_SERVER -m Record Sheet resend failed for ASN $ASN . Please resend manually."
  `$emailCmd`

else

echo "Entering resend logic"
OUTPUT=`sqlplus -s <username>/<password>@${ORACLE_SID} @$SCRIPT_HOME/sql/updateDefendantOnCaseRV.sql $ASN $CASE_TYPE $CASE_NUMBER "$COURT_NAME"`
echo $OUTPUT
OUTPUT=`echo $OUTPUT|tr -s ' '|cut -f1 -d ' '`
echo $OUTPUT

if [ "$OUTPUT" != "1" ]
then
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u Record Sheet resubmission failed -s $SMTP_SERVER -m Record Sheet resend failed for ASN $ASN . Please resend manually."
  `$emailCmd`
else
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u Record Sheet resubmission successful -s $SMTP_SERVER -m Record Sheet resent successfully for ASN $ASN "
  `$emailCmd`
fi
fi
sleep 200
done < $SCRIPT_HOME/missingASNs_$yesterdaysDate.txt

fi
rm $SCRIPT_HOME/$yesterdaysDate.tmp

