# First some environment
ORACLE_BASE=/opt/moj/oracle/10.2.0
ORACLE_SID=csdbprd1
ORACLE_HOME=/opt/moj/oracle/10.2.0
NLS_LANG="AMERICAN_UNITED KINGDOM.WE8ISO8859P1"
TNS_ADMIN=/opt/moj/home/wmbroker
PATH=$PATH:$ORACLE_HOME/bin:$ORACLE_HOME/lib
LD_LIBRARY_PATH=/usr/ucblib:/opt/moj/oracle/10.2.0/lib:$LD_LIBRARY_PATH
MYPATH=/opt/moj/home/wmbroker/bin/cron/cjit
MYRESULTS=/opt/moj/home/wmbroker/bin/cron/cjit/results
MYTEMP=$MYPATH/tmp
export MYPATH MYTEMP ORACLE_HOME ORACLE_BASE ORACLE_SID PATH LD_LIBRARY_PATH MYRESULTS NLS_LANG TNS_ADMIN

cd $MYPATH
$MYPATH/runsqlplus_cjit.sh $MYPATH/sent_doc
mv $MYPATH/tmp9.txt $MYRESULTS/select_`date +%Y%m%d`.txt
cd -
