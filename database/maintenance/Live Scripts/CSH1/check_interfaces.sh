#!/usr/bin/bash

##############################################################################
#
# Checks XHIBIT interfaces with EXISS and CJIT
#
# Sends an email if there have been any failed EXISS or CJIT messages since
# its last run.
#
# Stores last run time in 'check_interfaces_last_run.txt'.
#
# Created : 08/03/2010 - Dylan Thomas
#
##############################################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

# email settings
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk

lastRunFile=$SCRIPT_HOME/check_interfaces_last_run.txt

#
# Establish current and previous run times
######################################################################

# get previous runtime
lastRun=`cat $lastRunFile`
echo "lastRun >$lastRun<"

# get current runtime
currentRun=`date +"%d/%m/%Y %H:%M"`
echo "currentRun >$currentRun<"

# write current runtime to file
echo $currentRun > $lastRunFile


#
# Check XHB_Validation
######################################################################

# Number of schema failures
sqlResult=`sqlplus -s <username>/<password>@csdbprd1 <<END
  set heading off feedback off numformat 00000;
  select count(*) from xhb_validation where status = 'F';
`
echo "sqlResult=$sqlResult"
failedValidation=${sqlResult:4:5}
echo "failedValidation >$failedValidation<"

if [ "$failedValidation" -ne "00000" ]
then
  #We have failures so send an email
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u XHB_VALIDATION alert : $failedValidation failures -s $SMTP_SERVER -m There are $failedValidation validation failures which require attention"
 echo "emailCmd== $emailCmd"
 `$emailCmd`
fi

# CHeck for unprocessed records
sqlResult=`sqlplus -s <username>/<password>@csdbprd1 <<END
  set heading off feedback off numformat 00000;
  select count(*) from xhb_validation v, xhb_Validation_exiss ve where v.validation_id=VE.VALIDATION_ID and VE.ITEM_CREATED < (sysdate-(1/24)) and status not in ('S','F'); 
`
echo "sqlResult=$sqlResult"
notProcessed=${sqlResult:4:5}
echo "notProcessed >$notProcessed<"

if [ "$notProcessed" -ne "00000" ]
then
  #We have failures so send an email
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u XHB_VALIDATION alert : $notProcessed not processed -s $SMTP_SERVER -m There are $notProcessed validation records older than 1 hour which have not been processed"
 echo "emailCmd== $emailCmd"
 `$emailCmd`
fi


# Check EXISS
######################################################################

# retrieve total number of failed EXISS messages since last run
#sqlResult=`sqlplus -s <username>/<password>@csdbprd2 <<END
#  set heading off feedback off;
#  select count(*) From exi_item_outbound T where item_id in (
#    SELECT ITEM_ID FROM EXI_ITEM_OUTBOUND
#    WHERE ITEM_ID > (select min(item_id) from exi_item_outbound where ITEM_CREATED > (SYSDATE - 5))
#    MINUS
#    SELECT ITEM_ID FROM EXI_ITEM_OUTBOUND_TRACKING
#    WHERE ITEM_ID > (select min(item_id) from exi_item_outbound where ITEM_CREATED > (SYSDATE - 5)) 
#    AND STATUS_ID = 8) 
#    and item_created > to_date('$lastRun','dd/mm/yyyy hh24:mi')
#    and type_id in (1,2,10,12,15,20,414);
#`
#  select count(*) from exi_item_outbound_tracking
#  where tracking_date > to_date('$lastRun','dd/mm/yyyy hh24:mi') and status_id in (6,7,9,10,12);
#`

#echo "sqlResult=$sqlResult"
#failedExissMsg=`echo $sqlResult | sed -e 's/^ *//' -e 's/ *$//'`
#if [ $failedExissMsg -gt 100 ]
#then
#  # if failures then send email 
#  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u EXISS interface alert : $failedExissMsg failures -s $SMTP_SERVER -m There have been $failedExissMsg failed EXISS messages since $lastRun."
#  echo "emailCmd >$emailCmd<"
#  `$emailCmd`
#else
#  # if no failures then check total number of EXISS messages since last run
#  sqlResult=`sqlplus -s <username>/<password>@csdbprd2 <<END
#    set heading off feedback off numformat 00000;
#    select count(*) from exi_item_outbound
#    where item_created > to_date('$lastRun','dd/mm/yyyy hh24:mi');
#  `
#  totalExissMsg=${sqlResult:4:5}
#  echo "totalExissMsg >$totalExissMsg<"

#  # if no messages then send send email
#  if [ "$totalExissMsg" = "00000" ]
#  then 
#    emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u EXISS interface alert : no new messages -s $SMTP_SERVER -m There have no new EXISS messages generated since $lastRun."
#    echo "emailCmd >$emailCmd<"
#    `$emailCmd`
#  fi
#fi

#
# Check CJIT
######################################################################

# retrieve total number of failed CJIT messages since last run
sqlResult=`sqlplus -s <username>/<password>@csdbprd2 <<END
  set heading off feedback off numformat 00000
select count(*) From exi_item_outbound T where item_id in (
  select count(*) from cji_document
  where creation_date > to_date('$lastRun','dd/mm/yyyy hh24:mi') and status_id = 9
);
`

failedCjitMsg=${sqlResult:4:5}
echo "failedCjitMsg >$failedCjitMsg<"

if [ "$failedCjitMsg" -ne "00000" ]
  then
  # if failures then send email
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u CJIT interface alert : $failedCjitMsg failures -s $SMTP_SERVER -m There have been $failedCjitMsg failed CJIT messages since $lastRun."
  echo "emailCmd >$emailCmd<"
  `$emailCmd`
else
  # if no failures then check total number of CJIT messages since last run
  sqlResult=`sqlplus -s <username>/<password>@csdbprd2 <<END
    set heading off feedback off numformat 00000
    select count(*) from cji_document
    where creation_date > to_date('$lastRun','dd/mm/yyyy hh24:mi') and status_id = 3;
  `
  totalCjitMsg=${sqlResult:4:5}
  echo "totalCjitMsg >$totalCjitMsg<"

  # if no messages then send email
  if [ "$totalCjitMsg" = "00000" ]
  then
    emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u CJIT interface alert : no new messages -s $SMTP_SERVER -m There have been no new CJIT messages since $lastRun."
    echo "emailCmd >$emailCmd<"
    `$emailCmd`
  fi
fi



# Check CJIP
######################################################################

# retrieve total number of failed CJIP messages 
#CJIPResult=`sqlplus -s <username>/<password>@csdbprd2 <<END
#  set heading off feedback off
#select count(*) From CJI_EVENT T where status_id = 1 ;
#`

#echo "totalCjipMsg >$CJIPResult<"
#if [ $CJIPResult -gt 100 ]
#then
  # if failures then send email

#CJIPLastMessageTime=`sqlplus -s <username>/<password>@csdbprd2 <<END
#  set heading off feedback off
#select PARAMETER_VALUE FROM CJI_PARAMETER_VALUE where PARAMETER_NAME='CJIPLastMessageTime';
#`

#CJIPStatus=`sqlplus -s <username>/<password>@csdbprd2 <<END
#  set heading off feedback off
#select PARAMETER_VALUE FROM CJI_PARAMETER_VALUE where PARAMETER_NAME='CJIP_STATUS';
#`


#  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u CJI EVENT interface alert : $CJIPResult failures -s $SMTP_SERVER -m There have been $CJIPResult failed CJI Events. The current status is $CJIPStatus and the last message time is $CJIPLastMessageTime"
#  `$emailCmd`
#fi

