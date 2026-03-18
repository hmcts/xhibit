#!/bin/ksh
# File:         autoPDataCheck.sh
# Created:      25-May-2010
# Creator:      Ian Simmons
# Purpose:      To assist evening checks
#
# Modification History
# Date          By      Change
# 25-May-2010	IS	Initial check in of this file
#			General tidy up
#
SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/nightlyCheck
. $SCRIPT_HOME/../setEnv.sh

TODATE="`date '+%Y%m%d'`"
LOGFILE=$SCRIPT_HOME/logs/$TODATE.log
CNSFILE=$SCRIPT_HOME/logs/$TODATE.cns

ALLOK=true
password=xhibit
# Declare and initialise variables
echo "\n***  Reference Data  ***"  > $LOGFILE

# Display reference data status
$SCRIPT_HOME/../../runsqlplus.sh $SCRIPT_HOME/../../refstatus >> $LOGFILE

# Display creation date and time for last finalised Daily List
sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/../../dlstatus_part1 >> $LOGFILE

sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/../../dlstatus_part2 >> $LOGFILE

MESSAGEBODY1='Courts without a list in tomorrows schedule:\n'
MESSAGEBODY2='Courts not sitting tomorrow:\n'

MESSAGEBODY3=`sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/courtsWithoutlist`
MESSAGEBODY3=$(echo $MESSAGEBODY3|sed 's/-U-/U/g')
MESSAGEBODY4=`sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/courtsNotSitting`

## If courts without a list are the same as courts not sitting then this is ok
## Trim leading and trailing spaces to ensure that we are comparing accurately
MESSAGEBODY3=`echo $MESSAGEBODY3 | sed -e 's/^ *//' -e 's/ *$//'`
MESSAGEBODY4=`echo $MESSAGEBODY4 | sed -e 's/^ *//' -e 's/ *$//'`

if [ "$MESSAGEBODY3" != "$MESSAGEBODY4" ]
then

   ## If the length of MESSAGEBODY3 > MESSAGEBODY4 then investigate
   if [ ${#MESSAGEBODY3} -gt ${#MESSAGEBODY4} ]
   then
     ALLOK=false
   fi

fi


# Check to see that messagebody4 is not empty, i.e. more than 0 chars long
if [ `echo $MESSAGEBODY4 | wc -c` -eq 1 ]
then
   MESSAGEBODY5='All Courts Sitting\n'
fi

MESSAGEBODY="$MESSAGEBODY1$MESSAGEBODY3\n\n$MESSAGEBODY2$MESSAGEBODY4\n\n$MESSAGEBODY5"

if [[ $ALLOK == false ]]
then
	SUBJECT="Overnight Daily Lists: UNSUCCESSFUL"
else
	SUBJECT="Overnight Daily Lists: SUCCESSFUL"
fi 
export SUBJECT
export MESSAGEBODY

