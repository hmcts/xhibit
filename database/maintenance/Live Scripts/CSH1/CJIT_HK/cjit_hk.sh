#!/bin/sh

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh

EMAIL_RECIPIENTS="xhibit_support@logica.com"
TODAY=`date '+DATE: %d/%m/%y TIME:%H:%M:%S'`
echo $TODAY

NUMDAYSTOKEEP=303
OUTFILE=$SCRIPT_HOME/CJIT_HK/cjit_hk_$NUMDAYSTOKEEP.txt
STILL_LOOPING=true
while [ $STILL_LOOPING = "true" ]
do
  date > $OUTFILE
  sqlplus cjit/cjit@csdbprd2 @$SCRIPT_HOME/CJIT_HK/cjit_hk.sql $NUMDAYSTOKEEP >> $OUTFILE
  date >> $OUTFILE
#echo ORA >> $OUTFILE

  ###
  # Now send an email for this iteration
  SUBJECT='Please check attachment for errors: CJIT Housekeeping at '$NUMDAYSTOKEEP
  MESSAGE='Update of current status of CJIT HouseKeeping for '$NUMDAYSTOKEEP' days'
  ATTACHMENTS=$OUTFILE

COUNT=`grep ORA $OUTFILE|wc -l`
if [ "$COUNT" -gt "0" ]
then
  EMAILCMD="$SCRIPT_HOME/../sendemail.pl -f xhibitlists@justice.gov.uk -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY ; $MESSAGE -a $ATTACHMENTS "

  $EMAILCMD
fi

  NUMDAYSTOKEEP=`expr $NUMDAYSTOKEEP '-' 3`

  if [ "`expr $NUMDAYSTOKEEP '<' 300`" = "1" ]
  then
    STILL_LOOPING=false
  fi
done


###
# Now send final email to say we're done
SUBJECT='CJIT Housekeeping job complete at '$NUMDAYSTOKEEP
MESSAGE='Finished'
ATTACHMENTS=$OUTFILE
echo FINISHED >> $OUTFILE
EMAILCMD="$SCRIPT_HOME/../sendemail.pl -f xhibitlists@justice.gov.uk -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $TODAY ; $MESSAGE -a $ATTACHMENTS "

#$EMAILCMD

exit
