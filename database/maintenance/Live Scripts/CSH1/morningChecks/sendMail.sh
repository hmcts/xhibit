SCRIPT_HOME=/opt/moj/home/wmbroker/scratchpad/scott
. $SCRIPT_HOME/setEnv.sh

## Decide which route to send these
# 1. Via sendmail and SMTP
# 2. Via XHB_EMAIL2 table in database

ROUTE_TO_TAKE=1

if [ $ROUTE_TO_TAKE -eq 1 ]
then
 echo $2
  EMAIL_RECIPIENTS="ian.simmons@logica.com"
  TODAY=`date '+DATE: %d/%m/%y TIME:%H:%M:%S'`
  echo $TODAY
  SUBJECT="echo $2"
  MESSAGE=`cat $1`

  EMAILCMD="$SCRIPT_HOME/sendemail.pl -f xhibitMorningChecks@justice.gov.uk -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $MESSAGE "

  $EMAILCMD

fi
