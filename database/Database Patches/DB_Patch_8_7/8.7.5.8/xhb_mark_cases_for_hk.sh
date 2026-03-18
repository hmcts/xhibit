#!/bin/bash
#####################################################################################
# Module Name: XHIBIT HOUSEKEEPING - Marks cases for Housekeeping                   #
# Module ID: xhb_mark_for_case_for_hk                                               #
# File Name: xhb_mark_case_for_hk.sh                                                #
# Author: Sujatha Sethuraman                                                        #
# Version: 0.1                                                                      #
# Date: 05-Sep-19                                                                   #
#                                                                                   #
# Purpose                                                                           #
# ------------                                                                      #
# Identifies Cases for Housekeeping from the XHIBIT database                        #
# either Courtwise if Court ID is supplied OR runs for all Courts                   #
# Also accepts upper-limit of cases to be identified ,                              #
# if NOT supplied default is set as 10000 in the procedure                          #
#                                                                                   #
# Change Control                                                                    #
# ----------------------                                                            #
# Version    Date       Author          Description        Jira ticket              #
# -------  ---------  ----------        ----------------   ------                   #
#    0.1   05-Sep-19    S.Sethuraman     NEW HK procedure  ctx_4493                 #
#                                                                                   #
# ###################################################################################


# These declarations need to be changed so that they reflect the database setup within
# the environment.

declare -x SCRIPT_NAME=${0##*/};
declare -x AUDIT_TYPE=housekeeping;
declare -x SQLPLUS=$ORACLE_HOME/bin/sqlplus
declare -x ORA=xhibit/xhibit@O10TST2
declare -x ARCHIVE_LOGFILE=xhb_mark_case_for_hk`date +"_%d_%m_%Y_%H_%M_%S"`.log
declare -x P_COURT_ID=0
declare -x P_UPPER_LIMIT=0

# Redirect standard output and error to a log file, give other Users write permission
# on the file and announce the start of the run in the log file

umask 000
exec 1>>${ARCHIVE_LOGFILE}
exec 2>&1

AUDIT_MESSAGE="`date +"%d/%m/%Y-%H:%M:%S:> "` EVENT_STATUS=Start | DETAILS= find all Cases eligible for Housekeeping in XHIBIT Database";
echo $AUDIT_MESSAGE

# Execute SQLPLUS and call the stored procedure with the following arguments
# $1 - P_COURT_ID - xhibit court id number - supply 1 court id only if supplying, otherwise supply 0 to run for all courts
# $2 - P_UPPER_LIMIT - upper limit -- Limit on number of cases to identify so as load is balanced , supply 0 if NO limit
# Either Supply both arguments eg: xhb_mark_case_for_hk.sh 81 1000   or xhb_mark_case_for_hk.sh 81 0 or xhb_mark_CASE_for_hk.sh 0 0
# OR dont supply ANY and just call xhb_mark_case_for_hk.sh without any arguments

if [ "$#" -ne 2 ]; then
   P_COURT_ID=0;
   P_UPPER_LIMIT=0;
else
   P_COURT_ID=$1;
   P_UPPER_LIMIT=$2;
fi

AUDIT_MESSAGE="`date +"%d/%m/%Y-%H:%M:%S:> "` EVENT_STATUS=Running PROCEDURE | DETAILS= find all Cases eligible for Housekeeping in XHIBIT Database with parameters $P_COURT_ID, $P_UPPER_LIMIT";
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
exec  xhb_housekeeping_pkg_ss.find_all_cases_eligible_for_hk($P_COURT_ID,$P_UPPER_LIMIT);
exit
endsql

err_code=$?
if [ $err_code != 0 ]; then

     AUDIT_MESSAGE="`date +"%d/%m/%Y-%H:%M:%S:> "` EVENT_STATUS=Failure | DETAILS= find all Cases eligible for Housekeeping in XHIBIT Database - SQL error while calling stored procedure";
     echo $AUDIT_MESSAGE
     exit 1
fi
AUDIT_MESSAGE="`date +"%d/%m/%Y-%H:%M:%S:> "` EVENT_STATUS=Success | DETAILS= find all Cases eligible for Housekeeping in XHIBIT Database";
echo $AUDIT_MESSAGE
echo ""
exit 0

