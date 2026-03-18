#!/bin/bash
#####################################################################################
# Module Name: Archive for XHIBIT - Deletes data based on the arguments             #
# Module ID: xhb_archive                                                            #
# File Name: xhb_archive.sh                                                         #
# Author: Patrick Dunne                                                             #
# Version: 0.1                                                                      #
# Date: 16-Mar-09                                                                   #
#                                                                                   #
# Purpose                                                                           #
# ------------                                                                      #
# Removes Case and listing data from the XHIBIT database where the criteria         #
# for which records to remove are passed in as arguments.                           #
#                                                                                   #
# Change Control                                                                    #
# ----------------------                                                            #
# Version    Date       Author     Description       CCN                            #
# -------  ---------  ---------- ----------------   ------                          #
#    0.1   16-Mar-09    P.Dunne     New Module.      365                            #
#                                                                                   #
# ###################################################################################


# These declarations need to be changed so that they reflect the database setup within
# the environment.

declare -x SCRIPT_NAME=${0##*/};
declare -x AUDIT_TYPE=archive;
declare -x SQLPLUS=$ORACLE_HOME/bin/sqlplus
declare -x ORA=xhibit/xhibit@csdbprd1
declare -x ARCHIVE_LOGFILE=xhb_archive.log

# Redirect standard output and error to a log file, give other Users write permission
# on the file and announce the start of the run in the log file

umask 000
exec 1>>${ARCHIVE_LOGFILE}
exec 2>&1

AUDIT_MESSAGE="EVENT_STATUS=Start | DETAILS= Remove Case and listing data from XHIBIT Database";
echo $AUDIT_MESSAGE

# Execute SQLPLUS and call the stored procedure with the following arguments
# $1 - run type argument -- (A)ll,(C)ases,(L)istings
# $2 - case limit argument -- Limit on number of cases to delete
# $3 - running list argument -- Months to keep running lists for
# $4 - warned list argument -- Months to keep warning lists for
# $5 - firm list argument -- Months to keep firm lists for
# $6 - daily list argument -- Months to keep daily lists for
# $7 - success log argument -- Produced success log or not

${SQLPLUS} -s $ORA <<endsql
WHENEVER SQLERROR EXIT SQL.SQLCODE
exec  xhb_housekeeping_pkg.initiate_run('$1',$2,$3,$4,$5,$6,$7);
exit
endsql

err_code=$?
if [ $err_code != 0 ]; then

     AUDIT_MESSAGE=="EVENT_STATUS=Failure | DETAILS= Remove Case and listing data from the XHIBIT Database - SQL error while calling stored procedure";
     echo $AUDIT_MESSAGE
     exit 1
fi
AUDIT_MESSAGE=="EVENT_STATUS=Success | DETAILS= Remove Case and listing data from the XHIBIT Database";
echo $AUDIT_MESSAGE
exit 0

