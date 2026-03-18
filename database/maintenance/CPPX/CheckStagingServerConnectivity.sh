
###################################################
#
# Name: CheckStagingServerConnectivity.sh
#
# Created: 3rd August 2020
#
# Purpose: Checks staging server connectivity
#	   It will run periodically and email if its unsuccessful
#
#
# How to use: Run this script on its own.
#
# Return Codes: 11 = Exit due to validation failure
#               14 = Success
#
###################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/CPPX   # for NLE/Live

. $SCRIPT_HOME/../setEnv.sh   # for NLE/Live

# DSS details to be supplied
DSSDIRECTORY=$SCRIPT_HOME/dssinbound
REMOTEDSSFOLDER=/
#USERNAME=XhibittoCPP # NLE
USERNAME=XhibittoCPP_StagingInternal
PASSWORD=somepass
SERVERNAME=x.y.z.a


SUCCESS=true

#Log file# 
LOGFILE=$SCRIPT_HOME/logs/CPPMergeFailes_logs_`date +"%d%m%y_%H%M%S"`.txt

# Email variables - need to be amended accordingly #
EMAIL_RECIPIENTS=scott.atwell@cgi.com
EMAIL_SENDER=xhibitcsh@justice.gov.uk
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
TODAY=`date '+DATE: %m/%d/%y TIME:%H:%M:%S'`

####
# Check that there are no arguments passed in
#
if [ $# -ne 0 ]
then
  echo "Error in $0 - Invalid Argument Count" >> $LOGFILE 
  SUCCESS=false
fi


####
# Now if we are still successful then continue, otherwise error
#
if [ $SUCCESS = "false" ]
then
  echo "Syntax: $0" >> $LOGFILE 
  echo >> $LOGFILE 
  exit 11
fi

####
# We can now check if we can connect to staging server
#
CANSENDEMAIL=false

echo "Before connecting to db to check Merge Fails" >> $LOGFILE 
## The output of this gets written to the files defined above
## It checks for last hour for any merge failures
STAGINGSERVEROUTPUT=`telnet $SERVERNAME 22 <<< 'exit'`
CONNECTSTRING=`echo $STAGINGSERVEROUTPUT | grep Connected | wc -l`
if [ $CONNECTSTRING -eq "0" ];
then
  echo Not connected to DSS >> $LOGFILE
  CANSENDEMAIL=true
else
  echo Connected ok! >> $LOGFILE
fi


## if staging servre conn is offline then send an email ##
if [ $CANSENDEMAIL = "true" ]
then
  echo Now going to send an email for conn check >> $LOGFILE 
  OUTPUT_FILE=$SCRIPT_HOME/logs/StagingServerConnCheck.log
  echo `date +"%d-%m-%y %T"`, Staging Server connectivity is offline >> $OUTPUT_FILE

  ####
  # Send the email as all checks have been done
  #
  #### Email variables

  # Initial Subject #
  SUBJECT="CPPX connectivity to staging server has been as showing as offline"
  
  # echo out the subject # 
  echo SUBJECT = $SUBJECT >> $LOGFILE 
  emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY "
  echo EmailCmd: $emailCmd >> $LOGFILE
  `$emailCmd`
  
  echo Finished ...... >> $LOGFILE 
  exit 14
else
  echo All ok for now >> $LOGFILE 
  exit 14
fi
