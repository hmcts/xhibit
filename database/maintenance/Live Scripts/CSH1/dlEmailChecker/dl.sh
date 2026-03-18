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
SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/dlEmailChecker
#sCRIPT_HOME=/opt/moj/home/wmbroker/scratchpad/ian/dlEmailChecker
. $SCRIPT_HOME/../setEnv.sh


ALLOK=true
password=xhibit

# Display creation date and time for last finalised Daily List

MESSAGEBODY1='These are the courts currently without a daily list:\n'

MESSAGEBODY2='Court with a daily list already received:\n'

TODATE="`date '+%u'`"

echo $TODATE

if [ $TODATE = 5 ]
then
	MESSAGEBODY3=`sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/dl_courtsWithoutlist_fri`
	SUBJECT="DAILY LISTS $NOCOURTS WITH FINAL LIST"
	MESSAGEBODY4=`sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/dl_courtsWithlist_fri`
	MESSAGEBODY4=$(echo $MESSAGEBODY4|sed 's/-U-/U/g')
	NOCOURTS=`sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/dl_courtCount_fri`
else 
	MESSAGEBODY3=`sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/dl_courtsWithoutlist`
	SUBJECT="DAILY LISTS $NOCOURTS WITH FINAL LIST"
	MESSAGEBODY4=`sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/dl_courtsWithlist`
	MESSAGEBODY4=$(echo $MESSAGEBODY4|sed 's/-U-/U/g')
	NOCOURTS=`sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/dl_courtCount`
fi
## If courts without a list are the same as courts not sitting then this is ok
## Trim leading and trailing spaces to ensure that we are comparing accurately
MESSAGEBODY3=`echo $MESSAGEBODY3 | sed -e 's/^ *//' -e 's/ *$//'`
MESSAGEBODY3=$(echo $MESSAGEBODY3|sed 's/-U-/U/g')
MESSAGEBODY4=`echo $MESSAGEBODY4 | sed -e 's/^ *//' -e 's/ *$//'`
MESSAGEBODY="$MESSAGEBODY1$MESSAGEBODY3\n\n$MESSAGEBODY2$MESSAGEBODY4\n\n"
SUBJECT="DAILY LISTS:  $NOCOURTS with final lists"

if [ $NOCOURTS = 0 ]
then
  SUBJECT="DAILY LISTS: there are no courts with final lists" 
fi 

export SUBJECT
export MESSAGEBODY
