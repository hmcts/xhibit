#!/bin/ksh
SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/morningChecks
. $SCRIPT_HOME/setEnv.sh

HK_CASE_COUNT=`sqlplus -s <username>/<password>@csdbprd1 @$SCRIPT_HOME/sql/getHKCaseCount.sql`
echo $HK_CASE_COUNT
CASE_DELETION_COUNT=`sqlplus -s <username>/<password>@csdbprd1 @$SCRIPT_HOME/sql/getCaseDeletionCount.sql`
echo $CASE_DELETION_COUNT
LIST_DELETION_COUNT=`sqlplus -s <username>/<password>@csdbprd1 @$SCRIPT_HOME/sql/getListDeletionCount.sql`
echo $LIST_DELETION_COUNT
INCOMPLETE_HK_RUN_COUNT=`sqlplus -s <username>/<password>@csdbprd1 @$SCRIPT_HOME/sql/getIncompleteHKRuns.sql`
echo $INCOMPLETE_HK_RUN_COUNT
