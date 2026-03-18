#!/bin/bash
#####################################################################################
# Module Name: XHIBIT HOUSEKEEPING - Housekeep Courtel and Running Lists            #
# Module ID: xhb_hk_courtel_running_lists                                               #
# File Name: xhb_hk_courtel_running_lists.sh                                                #
# Author: Sujatha Sethuraman                                                        #
# Version: 0.1                                                                      #
# Date: 02-Oct-19                                                                   #
#                                                                                   #
# Purpose                                                                           #
# ------------                                                                      #
# Identifies Courtel and Running Lists for Housekeeping from the XHIBIT database    #
# marking them as Obsolete first and deleting them                                  #
# Also accepts upper-limit of cases to be identified ,                              #
# if NOT supplied default is set as 10000 in the procedure                          #
#                                                                                   #
# Change Control                                                                    #
# ----------------------                                                            #
# Version    Date       Author          Description        Jira ticket              #
# -------  ---------  ----------        ----------------   ------                   #
#    0.1   02-Oct-19    S.Sethuraman     HK Lists procedure  ctx_4500,ctx_4501      #
#                                                                                   #
# ###################################################################################


# These declarations need to be changed so that they reflect the database setup within
# the environment.
SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/HK/CTX
. $SCRIPT_HOME/../../setEnv.sh

declare -x SCRIPT_NAME=${0##*/};
declare -x AUDIT_TYPE=housekeeping;
declare -x SQLPLUS=$ORACLE_HOME/bin/sqlplus
declare -x ORA=xhibit/xhibit@CSDBPRD1
declare -x ARCHIVE_LOGFILE=xhb_hk_courtel_running_lists`date +"_%d_%m_%Y_%H_%M_%S"`.log
declare -x P_COURT_ID=0
declare -x P_UPPER_LIMIT=0

# Redirect standard output and error to a log file, give other Users write permission
# on the file and announce the start of the run in the log file

umask 000
exec 1>>${ARCHIVE_LOGFILE}
exec 2>&1

AUDIT_MESSAGE="`date +"%d/%m/%Y-%H:%M:%S:> "` EVENT_STATUS=Start | DETAILS= Housekeep Courtel and Running lists in XHIBIT Database";
echo $AUDIT_MESSAGE

# Execute SQLPLUS and call the stored procedure with the following arguments
# $1 - P_UPPER_LIMIT - upper limit -- Limit on number of cases to identify so as load is balanced , supply 0 if NO limit
# $2 - P_COURTEL_PERIOD - No of Days Courtel lists retained - supply 0 to use default parameter set in XHB_CONFIG_PROP 
# $3 - P_RUNNING_OBS_PERIOD - No of Days after which the running list is marked as OBSOLETE - supply 0 to use default parameter set in XHB_CONFIG_PROP
# $4 - P_RUNNING_DEL_PERIOD - No of Days after which the running list is DELETED - supply 0 to use default parameter set in XHB_CONFIG_PROP
# Either Supply all 4 arguments eg: xhb_hk_courtel_running_lists.sh 10000 180 180 730 
#                                or xhb_hk_courtel_running_lists.sh 0 180 180 0 or 
#                                   xhb_hk_courtel_running_lists.sh 0 0 0 0
# OR dont supply ANY and just call xhb_hk_courtel_running_lists.sh without any arguments

if [ "$#" -ne 4 ]; then
   P_UPPER_LIMIT=0;
   P_COURTEL_PERIOD=30;
   P_RUNNING_OBS_PERIOD=180;
   P_RUNNING_DEL_PERIOD=730;
else
   P_UPPER_LIMIT=$1;
   P_COURTEL_PERIOD=$2;
   P_RUNNING_OBS_PERIOD=$3;
   P_RUNNING_DEL_PERIOD=$4;
fi

AUDIT_MESSAGE="`date +"%d/%m/%Y-%H:%M:%S:> "` EVENT_STATUS=Running PROCEDURE | DETAILS= Housekeep Courtel and Running lists in XHIBIT Database with parameters $P_UPPER_LIMIT, $P_COURTEL_PERIOD, $P_RUNNING_OBS_PERIOD, $P_RUNNING_DEL_PERIOD";
echo $AUDIT_MESSAGE
echo ""

${SQLPLUS} -s $ORA <<endsql
WHENEVER SQLERROR EXIT SQL.SQLCODE
set serveroutput on
set pagesize 1000
set linesize 200
set termout on
set echo on
set feedback on
exec  xhb_housekeeping_pkg.del_courtel_list_data($P_UPPER_LIMIT,$P_COURTEL_PERIOD);
exec  xhb_housekeeping_pkg.housekeep_running_list($P_UPPER_LIMIT,$P_RUNNING_OBS_PERIOD,$P_RUNNING_DEL_PERIOD);
exit
endsql

err_code=$?
if [ $err_code != 0 ]; then

     AUDIT_MESSAGE="`date +"%d/%m/%Y-%H:%M:%S:> "` EVENT_STATUS=Failure | DETAILS= Housekeep Courtel and Running lists in XHIBIT Database - SQL error while calling stored procedure";
     echo $AUDIT_MESSAGE
     exit 1
fi
AUDIT_MESSAGE="`date +"%d/%m/%Y-%H:%M:%S:> "` EVENT_STATUS=Success | DETAILS= Housekeep Courtel and Running lists in XHIBIT Database";
echo $AUDIT_MESSAGE
echo ""
exit 0

