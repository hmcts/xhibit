#!/usr/bin/ksh

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
#SCRIPT_HOME=/opt/moj/home/wmbroker/scratchpad/vish
#. $SCRIPT_HOME/setEnv.sh
. /opt/moj/home/wmbroker/bin/cron/setEnv.sh


EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
#EMAIL_RECIPIENTS="xhibit_support@logica.com xhibitsupport@hmcts.gsi.gov.uk "
EMAIL_RECIPIENTS="vishwanath.mallya@logica.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk

todaysDate=`date +"%d%m%Y"`

sqlOutput=`sqlplus -s <username>/<password>@csdbprd1 <<END
  set heading off 
select user_id, max(to_char(date_logged_in,'DD-MM-YYYY')),'#' from aud_user_logins group by user_id order by user_id;
`
sqlOutput1=`echo $sqlOutput|tr -s '#' '\n' > $SCRIPT_HOME/userCount/userCount_tmp.txt`
usageByDate=`cat $SCRIPT_HOME/userCount/userCount_tmp.txt|grep "rows selected" `
usageByDate=`echo $usageByDate|tr -s '' |cut -f1 -d ' '`
echo $todaysDate $usageByDate >> $SCRIPT_HOME/userCount/usageByDate.txt
`cat $SCRIPT_HOME/userCount/userCount_tmp.txt|grep -v "rows selected" > $SCRIPT_HOME/userCount/userCount_$todaysDate.txt`
rm $SCRIPT_HOME/userCount/userCount_tmp.txt



while read line
do
userid=`echo $line|cut -f1 -d ' '`

grepOutput=`grep "$userid" $SCRIPT_HOME/userCount/userLastLogin.txt`
count=`echo $grepOutput|wc -c`
if [ "$count" -lt "2" ]
then
`echo $line >> $SCRIPT_HOME/userCount/userLastLogin.txt`
else

Output=`sed "s/$userid.[0-9][0-9]-[0-9][0-9]-[0-9][0-9][0-9][0-9]/$line/g" $SCRIPT_HOME/userCount/userLastLogin.txt > $SCRIPT_HOME/userCount/temp.txt`
mv $SCRIPT_HOME/userCount/temp.txt $SCRIPT_HOME/userCount/userLastLogin.txt
fi
done < $SCRIPT_HOME/userCount/userCount_$todaysDate.txt

