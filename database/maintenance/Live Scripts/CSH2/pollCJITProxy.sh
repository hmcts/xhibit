#!/usr/bin/bash

##############################################################################
#
# Checks XHIBIT interfaces CJIT Proxy
#
##############################################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

# email settings
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
EMAIL_RECIPIENTS="xhibit_support@logica.com richard.schuchardt@logica.com"
#EMAIL_RECIPIENTS="vishwanath.mallya@logica.com"
EMAIL_SENDER=xhibitcsh@justice.gov.uk
CJIT_PROXY_HOST=x.y.36.48
CJIT_PROXY_PORT=8080
#CJIT_PROXY_HOST=x.y.250.131
#CJIT_PROXY_PORT=1521
RESULT=""

lastRunFile=$SCRIPT_HOME/check_cjit_proxy_result.txt

lastResult=`cat $SCRIPT_HOME/check_cjit_proxy_result.txt`
#echo $lastResult

telnetResult=`telnet $CJIT_PROXY_HOST $CJIT_PROXY_PORT`

grepResult=`echo "$telnetResult"|tr -d '\n'`

searchResult=`echo "$grepResult"|grep Escape`
CJIT_PROXY_STATUS=$?

if [ "$CJIT_PROXY_STATUS" -ne "0" ]
then
 if [ "$lastResult" != "DOWN" ]
 then
  echo DOWN > $SCRIPT_HOME/check_cjit_proxy_result.txt
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u CJIT PROXY IS DOWN -s $SMTP_SERVER -m CJIT PROXY IS DOWN"
  `$emailCmd`
 fi
else
 if [ "$lastResult" != "UP" ]
 then
  echo UP > $SCRIPT_HOME/check_cjit_proxy_result.txt
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u CJIT PROXY IS BACK UP -s $SMTP_SERVER -m CJIT PROXY IS BACK UP "
  `$emailCmd`
 fi
fi

