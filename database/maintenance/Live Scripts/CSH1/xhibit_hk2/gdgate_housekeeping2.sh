#!/bin/bash
#################################################################################
#   gdg_houskeeping2.sh                                                         #
# Automated job for housekeeping in the GDGATE schema only			#
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
ARG2=$1
ARG3=$2

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
		
		AUDIT_MESSAGE="EVENT_STATUS=Failure | DETAILS= Invalid number entered ${ARG3}: must be an integer for the second parameter."
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
                AUDIT_MESSAGE="EVENT_STATUS=Failure | DETAILS= Incorrect value for first parameter: use only DAYS or ROWS"
                echo -e ${AUDIT_MESSAGE}
                exit
        fi
}

# check for the right sql file using the second parameter word
echo -e "Values entered: ${ARG2} ${ARG3}"

        num_check
	check_type	
echo -e "exec GDGATE.GDG_HOUSEKEEPING2_PKG.initiate_run('${ARG2}',${ARG3});"	
	
${SQLPLUS} -s ${ORA2} >>${LOG_DIR}/${scriptname}.${LOGFILE}<<endsql
set serveroutput on
WHENEVER SQLERROR EXIT SQL.SQLCODE
exec GDGATE.GDG_HOUSEKEEPING2_PKG.initiate_run('${ARG2}',${ARG3});
endsql

        AUDIT_MESSAGE=="EVENT_STATUS=Success | DETAILS=Run Completed. Please view file in: ${LOG_DIR}/${scriptname}.${LOGFILE}";
	echo -e ${AUDIT_MESSAGE}
err_code=$?
if [ $err_code != 0 ]; then

     AUDIT_MESSAGE=="EVENT_STATUS=Failure | DETAILS= SQL error while calling stored procedure";
     echo -e ${AUDIT_MESSAGE}
     exit 1
fi



exit

