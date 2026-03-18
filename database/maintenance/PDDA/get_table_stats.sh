#!/bin/ksh
# First some environment
ORACLE_BASE=/opt/moj/oracle/10.2.0
ORACLE_SID=CSDBPRD1
ORACLE_HOME=/opt/moj/oracle/10.2.0
NLS_LANG="AMERICAN_UNITED KINGDOM.WE8ISO8859P1"
TNS_ADMIN=/opt/moj/home/wmbroker
PATH=$PATH:$ORACLE_HOME/bin:$ORACLE_HOME/lib
LD_LIBRARY_PATH=/usr/ucblib:/opt/moj/oracle/10.2.0/lib:$LD_LIBRARY_PATH
export ORACLE_HOME ORACLE_BASE ORACLE_SID PATH LD_LIBRARY_PATH NLS_LANG TNS_ADMIN

echo tnsping ${ORACLE_SID}
tnsping ${ORACLE_SID}
echo sqlplus xhibit/xhibit@${ORACLE_SID} @/opt/moj/home/wmbroker/bin/cron/pdda/get_table_stats.sql
sqlplus xhibit/xhibit@${ORACLE_SID} @/opt/moj/home/wmbroker/bin/cron/pdda/get_table_stats.sql