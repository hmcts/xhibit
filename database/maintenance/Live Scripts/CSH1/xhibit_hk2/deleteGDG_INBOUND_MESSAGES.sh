#!/bin/bash
#################################################################################
#    deleteGDG_INBOUND_MESSAGES.sh                                              #
#                                                                               #
#                                                                               #
#                                                                               #
#                                                                               #
#                                                                               #
#                                                                               #
#                                                                               #
#                                                                               #
#################################################################################

. ${COMMON_VAR}/.common-variables

# framework file
ARG2=$1

# get script name and remove the extension, will be used to determine the sql file 
scriptname=`basename "$0" .sh`

# function to check if sql file exists and create log file if requested
# $1 in this function relates to the basename/parameter passed through as the scriptname.sql 
check_sql_file(){

if [[ -f $1 && ${ARG2} == "" ]]
then

${SQLPLUS} -s ${ORA2} >>${LOG_DIR}/${scriptname}.${LOGFILE}<<endsql
set serveroutput on
@$1
endsql
	echo "Run Completed. Please view file in: ${LOG_DIR}/${scriptname}.${LOGFILE}"
	exit

elif [[ -f $1 && ${ARG2} == ${PARAM4} ]]
then
	echo $1 ${ARG2}	
	echo "Engaging logging..."
	echo "Parameters used: Script:${scriptname} LOG:Y" >>${LOG_DIR}/${scriptname}.${LOGFILE}

${SQLPLUS} ${ORA2} 2>&1 >>${LOG_DIR}/${scriptname}.${LOGFILE}<<endsql
set serveroutput on
@$1
endsql

	echo "Run Completed. Please view file in: ${LOG_DIR}/${scriptname}.${LOGFILE}"
	exit
else
		echo "Exiting run. The corresponding SQL file is missing from ${SQL_DIR}. Please investigate."
		exit
	fi	
}

check_sql_file ${SQL_DIR}/${scriptname}.sql

exit
