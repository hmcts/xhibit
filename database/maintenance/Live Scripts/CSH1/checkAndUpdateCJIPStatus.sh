#!/usr/bin/bash

##############################################################################
#
# Checks XHIBIT interfaces with CJIP
#
# Sends an email if there have been any failed CJIP messages since
# its last run and tries to rectify the issue.
#
# Created : 11/04/2012 - Vishwanath Mallya
#
##############################################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

# email settings
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk

# Check CJIP
######################################################################

# retrieve total number of failed CJIP messages 
CJIPResult=`sqlplus -s <username>/<password>@csdbprd2 <<END
  set heading off feedback off
select count(*) From CJI_EVENT T where status_id = 1 ;
`

echo "totalCjipMsg >$CJIPResult<"
if [ $CJIPResult -gt 70 ]
then
  # if failures then send email

CJIPLastMessageTime=`sqlplus -s <username>/<password>@csdbprd2 <<END
  set heading off feedback off
select PARAMETER_VALUE FROM CJI_PARAMETER_VALUE where PARAMETER_NAME='CJIPLastMessageTime';
`

CJIPStatus=`sqlplus -s <username>/<password>@csdbprd2 <<END
  set heading off feedback off
select PARAMETER_VALUE FROM CJI_PARAMETER_VALUE where PARAMETER_NAME='CJIP_STATUS';
`
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u CJI EVENT interface alert : $CJIPResult failures -s $SMTP_SERVER -m There have been $CJIPResult failed CJI Events. The current status is $CJIPStatus and the last message time is $CJIPLastMessageTime"
  `$emailCmd`

OUTPUT=`sqlplus -s <username>/<password>@csdbprd2 @$SCRIPT_HOME/sql/updateCJIPStatus.sql`
echo $OUTPUT
OUTPUT=`echo $OUTPUT|tr -s ' '|cut -f1,4 -d ' '|tr -s ' ' '2'`
echo $OUTPUT

if [ $OUTPUT != "121" ]
then

  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u CJI EVENT interface alert : STATUS change unsuccessful -s $SMTP_SERVER -m The CJIP Status and last message time has NOT been updated. Please check database for current status"
  `$emailCmd`
else
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u CJI EVENT interface alert : STATUS changed -s $SMTP_SERVER -m The CJIP Status and last message time has been updated. Please check database for current status"
  `$emailCmd`
fi

fi

