#!/bin/ksh
LOGS_DIRECTORY="/var/mqsi/logs/wtxaudit/"
ARCHIVE_DIRECTORY="/var/mqsi/logs/wtxaudit/mqsiarchive"
#LOGS_DIRECTORY="/opt/moj/home/wmbroker/scratchpad/vish"
#ARCHIVE_DIRECTORY="/opt/moj/home/wmbroker/scratchpad/vish/testing"

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com"
#EMAIL_RECIPIENTS="vishwanath.mallya@logica.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk

if [ ! -d "$ARCHIVE_DIRECTORY" ]
then
echo "DIRECTORY DOES NOT EXIST"
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u MQSIARCHIVE FOLDER IS NOT PRESENT IN CSH2 -s $SMTP_SERVER -m MQSIARCHIVE FOLDER IS NOT PRESENT IN CSH2. CREATING FOLDER "
  `$emailCmd`
mkdir $ARCHIVE_DIRECTORY
chmod 777 $ARCHIVE_DIRECTORY
mv $LOGS_DIRECTORY/*.aud $ARCHIVE_DIRECTORY

CHECK_CMD=`ssh wmbroker@x.y.70.202 "/opt/moj/home/wmbroker/bin/cron/StopPublishWTXEventFlow_CSH2.sh;"`
echo $CHECK_CMD
count=`echo $CHECK_CMD|grep "Successful command completion"|wc -l`
if [ $count != "1" ]
then
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u WTXEvent flow might not be running -s $SMTP_SERVER -m A restart of BROKER on CSH2 might be required. Please investigate "
  `$emailCmd`

fi

CHECK_CMD1=`ssh wmbroker@x.y.70.202 "/opt/moj/home/wmbroker/bin/cron/StartPublishWTXEventFlow_CSH2.sh;"`
echo $CHECK_CMD1
count=`echo $CHECK_CMD1|grep "Successful command completion"|wc -l`
if [ $count != "1" ]
then
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u WTXEvent flow might not be running -s $SMTP_SERVER -m A restart of BROKER on CSH2 might be required. Please investigate "
  `$emailCmd`

fi



fi


