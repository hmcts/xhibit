#!/bin/bash
#################################################################################
#    deleteXHB_VALIDATION_EXISS.sh                                              #
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
ARG2=$2
ARG3=$3

# get script name and remove the extension, will be used to determine the sql file 
scriptname=`basename "$0" .sh`

#function to check that $2 is a number
num_check(){
	if [[ ${ARG2} =~ $NUMBERCHECK ]] 
	then
       		sleep 0	
	else
		echo "Invalid number entered: must be an integer. Exiting script."
		exit
	fi
	}

# function to check if sql file exists and create log file if requested
# $1,$2 in this function relates to the basename/parameter passed through from the 'case'

check_sql_file(){
if [[ -f $1 && ${ARG3} == "" ]]
then
        echo "Parameters used: Script:${scriptname} Param:$2 Value:${ARG2} LOG:N"
${SQLPLUS} -s $ORA >>${LOG_DIR}/${scriptname}.${LOGFILE}<<endsql
set serveroutput on
set feedback off
set verify off
@$1 ${ARG2}
endsql
        echo "Run Completed. Please view file in: ${LOG_DIR}/${scriptname}.${LOGFILE}"
        exit

elif [[ -f $1 && ${ARG3} == ${PARAM4} ]]
then
        echo "Engaging logging..."
        echo "Parameters used: Script:${scriptname} Param:$2 Value:${ARG2} LOG:Y" >>${LOG_DIR}/${scriptname}.${LOGFILE}
${SQLPLUS} -s $ORA >>${LOG_DIR}/${scriptname}.${LOGFILE}<<endsql
set serveroutput on
set feedback on
set verify on
@$1 ${ARG2}
endsql
        echo "Run Completed. Please view file in: ${LOG_DIR}/${scriptname}.${LOGFILE}"
else
        echo "Exiting run. The corresponding SQL file is missing from ${SQL_DIR}. Please investigate."
fi
}

# check for the right sql file using the second parameter word
for (( i=0; i< ${#PLIST[@]}; i++ ))
do
        if [[ $1 == ${PLIST[i]} ]]
then
        num_check
        case $1 in
        ${PARAM1})
		check_sql_file ${SQL_DIR}/${scriptname}_1.sql ${PARAM1}
		exit
		;;
        ${PARAM2})
                check_sql_file ${SQL_DIR}/${scriptname}_2.sql ${PARAM2}
		exit
		;;
        ${PARAM3})
                check_sql_file ${SQL_DIR}/${scriptname}_3.sql ${PARAM3}
		exit
		;;
        esac
else
	#checks if the array is complete else the invalid message appears all the time
	#only write a message at the end of the list after checking
	arraylength=${#PLIST[@]}-1
	if (( $i == $arraylength ))
	then 
		echo "Invalid parameter used. Use only one of the following: ${PLIST[@]}"
	fi
fi
done
