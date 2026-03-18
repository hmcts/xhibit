
###################################################
#
# Name: ClearDARTSMessages.sh
#
# Created: 10th September 2009
#
# Purpose: Clears all DARTS messages from the DARTS schema where the messages are
#	   successful as these messages are no longer required
#
#
#
# Return Codes: 11 = Exit due to validation failure
#               12 = Exit due to invalid mail_id being passed in
#               TODO 13 = Exit due to sql error or database access error
#               14 = Success
#
###################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh
OUTPUT_FILENAME=$SCRIPT_HOME/output/clearDARTSMessages.txt


####
# Run the sql
#
sqlplus <username>/<password>@${ORACLE_SID2} @$SCRIPT_HOME/sql/clearDARTSMessages.sql $OUTPUT_FILENAME

echo Finished ......
exit 14

