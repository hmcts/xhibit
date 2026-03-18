#!/bin/bash
#################################################################################
#   xhb_houskeeping2.sh                                                         #
# Automated job for housekeeping in the XHIBIT schema only			#
#                                                                               #
#                                                                               #
#                                                                               #
#                                                                               #
#                                                                               #
#                                                                               #
#                                                                               #
#                                                                               #
#################################################################################

. $HOME/.bash_profile
. ${COMMON_VAR}/.common-variables

# framework file
ARG1=$1
ARG2=$2
ARG3=$3

# get script name and remove the extension, will be used to determine the sql file 
scriptname=`basename "$0" .sh`

umask 000

exec 1>>${LOG_DIR}/${scriptname}.${LOGFILE}
exec 2>&1

AUDIT_MESSAGE="EVENT_STATUS=Start | DETAILS= Starting housekeeping2 procedure";
echo -e ${AUDIT_MESSAGE}


#function to check that $3 is a number
num_check(){
	if [[ ${ARG3} =~ $NUMBERCHECK ]] 
	then
       		sleep 0	
	else
		
		AUDIT_MESSAGE="EVENT_STATUS=Failure | DETAILS= Invalid number entered ${ARG3}: must be an integer for the third parameter."
		echo -e ${AUDIT_MESSAGE}
		exit
	fi
	}

# function to check for E V or PD
valid_char(){
	if [[ ${ARG1} == "E" || ${ARG1} == "V" || ${ARG1} == "P" ]]
	then 
		sleep 0	
	else
		AUDIT_MESSAGE="EVENT_STATUS=Failure | DETAILS= Incorrect value for first parameter: ${ARG3} use only the following: E, V or P"
		echo -e ${AUDIT_MESSAGE} 
		exit
	fi
}

# function to check if sql file exists and create log file if requested
# $1,$2 in this function relates to the basename/parameter passed through from the 'case'

check_type(){
	if [[ ${ARG2} == "DAYS" || ${ARG2} == "ROWS" ]]
        then
                sleep 0
        else
                AUDIT_MESSAGE="EVENT_STATUS=Failure |  DETAILLS= Incorrect value for first parameter: use only DAYS or ROWS"
                echo -e ${AUDIT_MESSAGE}
                exit
        fi
}

# check for the right sql file using the second parameter word
echo -e "Values entered: ${ARG1} ${ARG2} ${ARG3}"

        num_check
	valid_char 
	check_type	
echo -e "exec XHIBIT.XHB_HOUSEKEEPING2_PKG.initiate_run('${ARG1}',${ARG2},'${ARG3}');"	
	
${SQLPLUS} -s ${ORA} >>${LOG_DIR}/${scriptname}.${LOGFILE}<<endsql
set serveroutput on
WHENEVER SQLERROR EXIT SQL.SQLCODE
exec XHIBIT.XHB_HOUSEKEEPING2_PKG.initiate_run('${ARG1}','${ARG2}',${ARG3});
endsql

        AUDIT_MESSAGE="EVENT_STATUS=Success | DETAILS= Run Completed. Please view file in: ${LOG_DIR}/${scriptname}.${LOGFILE}"
	echo -e ${AUDIT_MESSAGE}

err_code=$?
if [ $err_code != 0 ]; then

     AUDIT_MESSAGE=="EVENT_STATUS=Failure | DETAILS= SQL error while calling stored procedure";
     echo -e ${AUDIT_MESSAGE}
     exit 1
fi



exit

