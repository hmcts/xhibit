#!/bin/ksh
# fIle:         autoPDataCheck.sh
# Created:      25-May-2010
# Creator:      Ian Simmons
# Purpose:      To assist evening checks
#
# Modification History
# Date          By      Change
# 25-May-2010   IS      Initial check in of this file
#                       General tidy up
#
SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/morningChecks
. $SCRIPT_HOME/setEnv.sh

TODATE="`date '+%Y%m%d'`"
DAY="`date '+%a'`"
ALLOK=true
password=xhibit
# firstly resync case
REFRESHCASE=`sqlplus -s xhibit/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/refreshCase`
# now check the public display
HTTPUP1=`$SCRIPT_HOME/presBox.sh x.y.199.81 7071 | telnet` > /dev/null 2>&1
HTTPUP2=`$SCRIPT_HOME/presBox.sh x.y.199.82 7071 | telnet`
echo $HTTPUP1 > $SCRIPT_HOME/archive/telnet_$TODATE.log
echo $HTTPUP2 >> $SCRIPT_HOME/archive/telnet_$TODATE.log

HTTPUP1GREP=`echo $HTTPUP1 | grep rhs`
HTTPUP2GREP=`echo $HTTPUP2 | grep rhs`
PUBLICDISPLAYSTATUS="Ok"

if [ `echo $HTTPUP1GREP | wc -m` -lt 3 ] && [ `echo $HTTPUP2GREP | wc -m` -lt 3 ]  
then
    PUBLICDISPLAYSTATUS="Not Ok"
    PUBLICDISPAYERROR="Pres Servers are down."
fi
# thin client
THINCLIENT=`$SCRIPT_HOME/probService.sh x.y.199.81 7071 | telnet`
THINCLIENT1=`$SCRIPT_HOME/probService.sh x.y.199.82 7071 | telnet`
echo $THINCLIENT > $SCRIPT_HOME/archive/telnet_thinclient_$TODATE.log
echo $THINCLIENT >> $SCRIPT_HOME/archive/telnet_thinclient_$TODATE.log
RESPONSETHIN=`echo $THINCLIENT | grep 4430`
RESPONSETHIN1=`echo $THINCLIENT1 | grep 4430`
THINCLIENTSTATUS="Ok"
if [ `echo $RESPONSETHIN | wc -c` -eq 1 ] && [ `echo $RESPONSETHIN1 | wc -c` -eq 1 ]
then
   THINCLIENTSTATUS="Not Ok"
fi

# Now count Cases
if [ "$DAY" == "Mon" ]
then
	COUNTCASE=`sqlplus -s <username>/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/countCases_mon`
else
	COUNTCASE=`sqlplus -s <username>/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/countCases`
fi
COUNTCASE=$(echo $COUNTCASE|sed 's/-U-/U/g')
COUNTCASE=`echo $COUNTCASE | sed -e 's/^ *//' -e 's/ *$//'`

# now GET RUNNING TIMES
RUNNINGTIME=`sqlplus -s <username>/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/runningTime`
RUNNINGTIME=$(echo $RUNNINGTIME |sed 's/-U-/U/g')
RUNNINGTIME=`echo $RUNNINGTIME | sed -e 's/^ *//' -e 's/ *$//'`

# BROKER UP
BROKERUP=`/opt/moj/home/wmbroker/bin/ha/hamqsi_monitor_broker_as BRK_CSH_1 QM_CSH_1`
BROKERUPSTATIC=`cat $SCRIPT_HOME/static/brokerup.txt`
BROKERSTATUS="Ok"
if [ "$BROKERUP" != "$BROKERUPSTATIC" ]
then
    BROKERSTATUS="Not Ok"
fi
# broker abends

ABEND_DIR="/var/mqsi/common/errors"
BROKERABENDSTAUS="Ok"
ABEND_FILE_LIST=`ls $ABEND_DIR/*.abend`
if [[ -n $ABEND_FILE_LIST ]]
then
        BROKERABENDSTAUS="Not Ok"
fi
# now check case has resync'd
sleep 10
RESYNDCASE=`sqlplus -s <username>/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/checkRefreshCase`
RESYNDCASE=$(echo $RESYNDCASE|sed 's/-U-/U/g')
RESYNDCASE=`echo $RESYNDCASE | sed -e 's/^ *//' -e 's/ *$//'`
RESYNDCASESTATUS="Ok"
RESYNDCASEOK="O"
if [ "$RESYNDCASE" != "$RESYNDCASEOK" ];
then
        RESYNDCASESTATUS="Not Ok"
fi

# now check if there is any court down
COURTSDOWN=`cat /opt/moj/home/wmbroker/bin/cron/court_list_down.txt`
COURTSDOWNSTATUS="Ok"
COURTSDOWN_LEN=`expr $COURTSDOWN | awk ' { print length } '`
if [ `echo $COURTSDOWN | wc -c` -gt 1 ]
then
        COURTSDOWNSTATUS="Not Ok"
fi

# ensure that all daily lists are in
DAILYLISTS=`sqlplus -s <username>/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/dailyList`
DAILYLISTS=$(echo $DAILYLISTS|sed 's/-U-/U/g')
DAILYLISTS=`echo $DAILYLISTS |sed -e 's/^ *//' -e 's/ *$//'`
DAILYLISTNOTSITTING=`sqlplus -s <username>/$password@${ORACLE_SID} @$SCRIPT_HOME/sql/courtsNotSitting`
echo $DAILYLISTNOTSITTING

DAILYLISTNOTSITTING=$(echo $DAILYLISTNOTSITTING|sed 's/-U-/U/g')
DAILYLISTNOTSITTING=`echo $DAILYLISTNOTSITTING |sed -e 's/^ *//' -e 's/ *$//'`
DAILYLISTSTATUS="Ok"
echo $DAILYLISTNOTSITTING
echo $DAILYLISTS

if [ `echo $DAILYLISTS | wc -c` -gt 1 ]
then
     if [ "$DAILYLISTS" != "$DAILYLISTNOTSITTING" ] 
     then
	 DAILYLISTSTATUS="Not Ok"
     fi
fi

#check exiss
#EXISSRESULTS=`sqlplus -s <username>/<password>@csdbprd2 @$SCRIPT_HOME/sql/exiss`
EXISSRESULTS=$(echo $EXISSRESULTS|sed 's/-U-/U/g')
EXISSRESULTS=`echo $EXISSRESULTS | sed -e 's/^ *//' -e 's/ *$//'`
echo $EXISSRESULTS

EXISSRESULTSSTATUS="Ok"
if [ `echo $EXISSRESULTS | wc -c` -gt 1 ]
then
        EXISSRESULTSSTATUS="Not Ok"
fi

#Check CJIT SMS Messages
SMS_COUNT=/opt/moj/home/wmbroker/bin/cron/morningChecks/Failure_Count.txt
SMS_FAIL_COUNT=/opt/moj/home/wmbroker/bin/cron/morningChecks/SMS_Fail_Count.txt
SMSLIMIT=0
SMSSTATUS="Ok"
sqlplus <username>/<password>@csdbprd2 @/opt/moj/home/wmbroker/bin/cron/morningChecks/SMS.sql > $SMS_COUNT
sed 's/^[ \t]*//;s/[ \t]*$//' $SMS_COUNT > $SMS_FAIL_COUNT
TRIM_COUNT=$(sed -n '15,15p' $SMS_FAIL_COUNT)
if [ "$TRIM_COUNT" -gt "$SMSLIMIT" ];
then
SMSSTATUS="Not Ok"
rm Failure_Count.txt
else
SMSSTATUS="Ok"
rm Failure_Count.txt
fi

# check CJIT
CJIPCHECK=`sqlplus -s <username>/<password>@csdbprd2 @$SCRIPT_HOME/sql/cjit`
CJIPCHECK=$(echo $CJIPCHECK|sed 's/-U-/U/g')
CJIPCHECK=`echo $CJIPCHECK | sed -e 's/^ *//' -e 's/ *$//'`
CJIPSTATUS="Ok"
if [ `echo $CJIPCHECK | wc -c` -eq 1 ]
then
        CJIPSTATUS="Not Ok"
fi

#check for the broker archive directory
MQSIARCHIVESTATUS="Not Ok"
if [ -d /var/mqsi/logs/wtxaudit/mqsiarchive ]
then
    MQSIARCHIVESTATUS="Ok"
fi

# msqsarchive updating frequently

# Check if there are any messages in the DAR_NEW_MESSAGES table
DARTSRESULTS=`sqlplus -s <username>/<password>@csdbprd2 @$SCRIPT_HOME/sql/darts`
DARTSRESULTSSTATUS="Ok"
DARTSRESULTS=$(echo $DARTSRESULTS|sed 's/-U-/U/g')
DARTSRESULTS=`echo $DARTSRESULTS | sed -e 's/^ *//' -e 's/ *$//'`
if [ `echo $DARTSRESULTS` -gt 1 ] 
then
	DARTSRESULTSSTATUS="Not Ok"
fi
echo $DARTSRESULTSSTATUS
2
#############################################################################################################
MQ_OVERALL_STATUS="Ok"
MQ_APPEND=""
THICK_CLIENT_SECURE_OVERALL_STATUS="Ok"
THICK_CLIENT_SECURE_APPEND=""
THICK_CLIENT_NON_SECURE_OVERALL_STATUS="Ok"
THICK_CLIENT_NON_SECURE_APPEND=""
#SCJSE_OVERALL_STATUS="Ok"
#SCJSE_APPEND=""
WMB_MESSAGE_QUEUE_TABLE_STATUS="Ok"
WMB_MESSAGE_QUEUE_TABLE_COUNT=0
DB_OVERALL_SCHEMA_STATUS="Ok"
DB_SCHEMA_APPEND=""
DB_TNSPING_OVERALL_STATUS="Ok"
DB_TNSPING_APPEND=""
MQSIARCHIVE_FOLDER_STATUS="Ok"
MQSIARCHIVE_FOLDER_APPEND=""
HK_STATUS="Ok"
HK_APPEND=""
DARTS_STATUS="Ok"
DARTS_APPEND=""
LAA_STATUS="Ok"

DARTS_COUNT=`sqlplus -s <username>/<password>@csdbprd1 @$SCRIPT_HOME/sql/getDARTSStatus.sql`
echo $DARTS_COUNT

if [ "$DARTS_COUNT" -ne "0" ]
then
        DARTS_STATUS="Not Ok"
        DARTS_APPEND="$DARTS_APPEND DARTS"
fi

LAA_ERRORS=`ls /opt/moj/home/wmbroker/bin/cron/LAA/workspace/|wc -l`
echo $LAA_ERRORS
if [ "$LAA_ERRORS" -gt "0" ]
then
       LAA_STATUS="Not Ok" 
fi

LAA_ERRORS=`ssh wmbroker@x.y.66.72 "ls /opt/moj/home/wmbroker/bin/cron/LAA/workspace/|wc -l"`
echo $LAA_ERRORS
if [ "$LAA_ERRORS" -gt "0" ]
then
       LAA_STATUS="Not Ok"
fi



HK_CASE_COUNT=`sqlplus -s <username>/<password>@csdbprd1 @$SCRIPT_HOME/sql/getHKCaseCount.sql`
echo $HK_CASE_COUNT
CASE_DELETION_COUNT=`sqlplus -s <username>/<password>@csdbprd1 @$SCRIPT_HOME/sql/getCaseDeletionCount.sql`
echo $CASE_DELETION_COUNT
CASE_DELETION_COUNT=`echo $CASE_DELETION_COUNT|tr -d ' '`
echo $CASE_DELETION_COUNT

LIST_DELETION_COUNT=`sqlplus -s <username>/<password>@csdbprd1 @$SCRIPT_HOME/sql/getListDeletionCount.sql`
echo $LIST_DELETION_COUNT
INCOMPLETE_HK_RUN_COUNT=`sqlplus -s <username>/<password>@csdbprd1 @$SCRIPT_HOME/sql/getIncompleteHKRuns.sql`
echo $INCOMPLETE_HK_RUN_COUNT

if [ "$INCOMPLETE_HK_RUN_COUNT" -ne "0" ]
then
	HK_STATUS="Not Ok"
	HK_APPEND="$HK_APPEND HK"
fi

CHECK_CMD=`ssh wmbroker@x.y.66.72 "cd /var/mqsi/logs/wtxaudit;ls -p|grep mqsiarchive/"`
echo $CHECK_CMD
if [ $CHECK_CMD != "mqsiarchive/" ]
then 
    MQSIARCHIVE_FOLDER_APPEND="$MQSIARCHIVE_FOLDER_APPEND CSH2"
    MQSIARCHIVE_FOLDER_STATUS="Not Ok"
fi
if [ ! -d /var/mqsi/logs/wtxaudit/mqsiarchive ]
then
    MQSIARCHIVE_FOLDER_APPEND="$MQSIARCHIVE_FOLDER_APPEND CSH1"
    MQSIARCHIVE_FOLDER_STATUS="Not Ok"
fi


FUNCTION_RETURN_VALUE=""
telnet_function() {
	telnetResult=`echo "open $1 $2"|telnet`
	grepResult=`echo "$telnetResult"|tr -d '\n'`
	searchResult=`echo "$grepResult"|grep Escape`
	FUNCTION_RETURN_VALUE=$?
}

sqlplus_function() {
        grepResult=`echo 'exit'|sqlplus $1/$1@$2|tr -d '\n'`
        searchResult=`echo "$grepResult"|grep 'SQL>'`
        FUNCTION_RETURN_VALUE=$?
}

tnsping_function() {
        grepResult=`tnsping $1|tr -d '\n'`
        searchResult=`echo "$grepResult"|grep OK`
        FUNCTION_RETURN_VALUE=$?
}

tnsping_function WMBCSH
        if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                DB_TNSPING_OVERALL_STATUS="Not Ok"
                DB_TNSPING_APPEND="$DB_TNSPING_APPEND WMBCSH"
        fi

tnsping_function CSDBPRD1
        if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                DB_TNSPING_OVERALL_STATUS="Not Ok"
                DB_TNSPING_APPEND="$DB_TNSPING_APPEND CSDBPRD1"
        fi

tnsping_function CSDBPRD2
        if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                DB_TNSPING_OVERALL_STATUS="Not Ok"
                DB_TNSPING_APPEND="$DB_TNSPING_APPEND CSDBPRD2"
        fi

tnsping_function CSDBPRD3
        if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                DB_TNSPING_OVERALL_STATUS="Not Ok"
                DB_TNSPING_APPEND="$DB_TNSPING_APPEND CSDBPRD3"
        fi

sqlplus_function xhibit CSDBPRD1
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
		DB_OVERALL_SCHEMA_STATUS="Not Ok"
		DB_SCHEMA_APPEND="$DB_SCHEMA_APPEND XHIBIT"
	fi

sqlplus_function cjit CSDBPRD2
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
		DB_OVERALL_SCHEMA_STATUS="Not Ok"
		DB_SCHEMA_APPEND="$DB_SCHEMA_APPEND CJIT"
	fi

sqlplus_function exiss CSDBPRD2
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
		DB_OVERALL_SCHEMA_STATUS="Not Ok"
		DB_SCHEMA_APPEND="$DB_SCHEMA_APPEND EXISS"
	fi

sqlplus_function darts CSDBPRD2
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
		DB_OVERALL_SCHEMA_STATUS="Not Ok"
		DB_SCHEMA_APPEND="$DB_SCHEMA_APPEND DARTS"
	fi

sqlplus_function gdgate CSDBPRD3
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
		DB_OVERALL_SCHEMA_STATUS="Not Ok"
		DB_SCHEMA_APPEND="$DB_SCHEMA_APPEND GDGATE"
	fi

telnet_function x.y.199.97 4430
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_SECURE_OVERALL_STATUS="Not Ok"
                THICK_CLIENT_SECURE_APPEND="$THICK_CLIENT_SECURE_APPEND MIDM1"
        fi

telnet_function x.y.199.98 4430
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_SECURE_OVERALL_STATUS="Not Ok"
                THICK_CLIENT_SECURE_APPEND="$THICK_CLIENT_SECURE_APPEND MIDM2"
        fi

telnet_function x.y.199.99 4430
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_SECURE_OVERALL_STATUS="Not Ok"
                THICK_CLIENT_SECURE_APPEND="$THICK_CLIENT_SECURE_APPEND MIDM3"
        fi


telnet_function x.y.199.97 7071
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_NON_SECURE_OVERALL_STATUS="Not Ok"
                THICK_CLIENT_NON_SECURE_APPEND="$THICK_CLIENT_NON_SECURE_APPEND MIDM1"
        fi

telnet_function x.y.199.98 7071
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_NON_SECURE_OVERALL_STATUS="Not Ok"
                THICK_CLIENT_NON_SECURE_APPEND="$THICK_CLIENT_NON_SECURE_APPEND MIDM2"
        fi

telnet_function x.y.199.99 7071
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_NON_SECURE_OVERALL_STATUS="Not Ok"
                THICK_CLIENT_NON_SECURE_APPEND="$THICK_CLIENT_NON_SECURE_APPEND MIDM3"
        fi

#telnet_function x.y.199.113 7071
#	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
#        then
#                SCJSE_OVERALL_STATUS="Not Ok"
#                SCJSE_APPEND="$SCJSE_APPEND SCJSE(7071)"
#        fi

#telnet_function x.y.197.161 80
#	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
#        then
#                SCJSE_OVERALL_STATUS="Not Ok"
#                SCJSE_APPEND="$SCJSE_APPEND SCJSE(80)"
#        fi

telnet_function x.y.66.70 1411
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                MQ_OVERALL_STATUS="Not Ok"
                MQ_APPEND="$MQ_APPEND CSH1"
        fi

telnet_function x.y.66.72 1412
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                MQ_OVERALL_STATUS="Not Ok"
                MQ_APPEND="$MQ_APPEND CSH2"
        fi

telnet_function x.y.250.138 1413
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                MQ_OVERALL_STATUS="Not Ok"
                MQ_APPEND="$MQ_APPEND MIDM1"
        fi

telnet_function x.y.250.139 1414
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                MQ_OVERALL_STATUS="Not Ok"
                MQ_APPEND="$MQ_APPEND MIDM2"
        fi

telnet_function x.y.250.140 1415
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                MQ_OVERALL_STATUS="Not Ok"
                MQ_APPEND="$MQ_APPEND MIDM3"
        fi

WMB_MESSAGE_QUEUE_TABLE_COUNT=`sqlplus -s <username>/<password>@csdbprd1 <<END
  set heading off feedback off
select count(*) from wmb_message_queue_table;
`
if [ $WMB_MESSAGE_QUEUE_TABLE_COUNT -gt 250 ]
then
WMB_MESSAGE_QUEUE_TABLE_STATUS="Not Ok"
fi







#############################################################################################################
# aud files

echo "PUBLIC DISPLAY $PUBLICDISPLAYSTATUS"
echo "THIN CLIENT APPS $THINCLIENTSTATUS"
echo "BROKER $BROKERSTATUS"
echo "REFRESH/RESYNC $RESYNDCASESTATUS"
echo "DAILY LIST $DAILYLISTSTATUS"
echo "EXISS MESSAGES $EXISSRESULTSSTATUS"
echo "SMS STATUS $SMSSTATUS"
echo "BROKER ABENDS $BROKERABENDSTAUS"
echo "CJIP PROXY CONNECTIVITY $CJIPSTATUS"
echo "MERCATOR SERVICES $COURTSDOWNSTATUS"
echo "DAILY LIST RUNNING TIME $RUNNINGTIME"
echo "COUNT CASE $COUNTCASE"
echo "MQ $MQ_OVERALL_STATUS"
echo "THICK CLIENT SECURE $THICK_CLIENT_SECURE_OVERALL_STATUS"
echo "THICK CLIENT NON SECURE $THICK_CLIENT_NON_SECURE_OVERALL_STATUS"
#echo "SCJSE $SCJSE_OVERALL_STATUS"
echo "WMB MESSAGE QUEUE TABLE $WMB_MESSAGE_QUEUE_TABLE_STATUS"
echo "DB SCHEMA $DB_OVERALL_SCHEMA_STATUS"
echo "DB TNS PING $DB_TNSPING_OVERALL_STATUS"
echo "MQSIARCHIVE FOLDER STATUS $MQSIARCHIVE_FOLDER_STATUS"
echo "HK STATUS $HK_STATUS"
echo "DARTS STATUS $DARTS_STATUS"
echo "LAA STATUS $LAA_STATUS"
echo "DAR_NEW_MESSAGES $DARTSRESULTSSTATUS"

# now generate email

STARTPAGE="<html></style><table border=0 cellpadding=2 width='70%' cellspacing=0 width=50% style='{border: 1px solid #000000;}'>"
STATUSOK="Ok"
STATUSNOTOK="Not Ok"
APENDNOTES=""
COLOUR="92d050"

HEADER="<tr><td width='20%' bgcolor='Silver' style='{border-right: 1px solid #000000 ;}'>&nbsp</td><td  bgcolor='Silver'style='{border-right: 1px solid #00000
0;}'><b><font size=2 face='Calibri'>Daily Check Details</b></td><td width='10%' bgcolor='Silver'><b><font size=2 face='Calibri'>Result</b></td></tr>"

THICKCLIENT="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Thick Client</td><td style='{border-right: 1px solid #000000;}'><font size=2 f
ace='Calibri'>Ensure that you can login to Thick Client Application and View Todays Schedule</td><td width='10%' align='center' bgcolor='92d050'>??</b></td></tr>"

if [ "$PUBLICDISPLAYSTATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>Public Display error $PUBLICDISPLAYERROR<BR>";
fi
PUBLICDISPLAY="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Public Display</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Ensure that you can view the public display pages with an upto date timestamp at the top left of page</td><td width='10%' align='center'  bgcolor='$COLOUR'>$PUBLICDISPLAYSTATUS</b></td></tr>"

COLOUR="92d050"

MESSAGING="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Messaging</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Ensure that you can send and receive messages using the thick client</td><td width='10%' align='center'  bgcolor='$COLOUR'>??</b></td></tr>"

if [ "$THINCLIENTSTATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P> <BR>";
fi

THINCLIENTAPPS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Thin Client Apps</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Ensure that thin client apps are working as expected</td><td width='10%' align='center'  bgcolor='$COLOUR'>$THINCLIENTSTATUS</b></td></tr>"

COLOUR="92d050"
FREEMEMORY="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Free Memory</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Ensure that there is enough free memory in webLogic</td><td width='10%' align='center'  bgcolor='$COLOUR'>??</b></td></tr>"

if [ "$BROKERSTATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>Broker Down.<BR>";
fi

COLOUR="92d050"
BROKER="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Broker</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Ensure that the broker services are running</td><td width='10%' bgcolor='$COLOUR' align='center'>$BROKERSTATUS</b></td></tr>"

if [ "$RESYNDCASESTATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>Refresh/Resync, unable to refresh a case.<BR>";
fi

COLOUR="92d050"
REFRESH="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Refresh/Resync</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Ensure that count script show no synchronisation of case</td><td width='10%' align='center' bgcolor='$COLOUR'>$RESYNDCASESTATUS</b></td></tr>"

if [ "$DAILYLISTSTATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>Courts with no daily list:<BR>$DAILYLISTS";
fi

DAILYLISTS1="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Daily List</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Ensure that all daily list have been received by XHIBIT</td><td width='10%' align='center'bgcolor='$COLOUR'>$DAILYLISTSTATUS</b></td></tr>"

COLOUR="92d050"
if [ "$EXISSRESULTSSTATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>Exiss: There are currently $ failed messages.<BR>";
fi

EXISS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>EXISS messages</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Ensure there are no messages in a failed status</td><td width='10%' align='center' bgcolor='$COLOUR'>$EXISSRESULTSSTATUS</b></td></tr>"

COLOUR="92d050"
if [ "$SMSSTATUS" != "$STATUSOK" ]
then
	COLOUR="Red"
	APPENDNOTE="$APPENDNOTE<P>Un-processed SMS message(s) found<BR>";
fi

SMS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>SMS Messages</td><td style='{border-right:1px solid #000000;}'><font size=2 
face='Calibri'>Ensure there are no SMS messages in an un-processed state</td><td width='10%' align='center' bgcolor='$COLOUR'>$SMSSTATUS</b></td></tr>"

COLOUR="92d050"
if [ "$BROKERABENDSTAUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>There has been a broker abend<BR>";
fi

BROKERABENDS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Broker Abends</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if a Broker abend has occurred overnight</td><td width='10%' align='center'bgcolor='$COLOUR'>$BROKERABENDSTAUS</b></td></tr>"

COLOUR="92d050"
if [ "$CJIPSTATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>There has been a broker abend<BR>";
fi

CJIPPROXY="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>CJIP Proxy Connectivity</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if the are alerts on the CJIP Proxy server</td><td width='10%' align='center'bgcolor='$COLOUR'>$CJIPSTATUS</b></td></tr>"

COLOUR="92d050"
if [ "$COURTSDOWNSTATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>Courts Down: $COURTSDOWN<BR>";
fi

COURTSDOWN="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Mercator Services</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if any Spokes have generated an alert regarding the Mercator Services</td><td width='10%' align='center' bgcolor='$COLOUR'>$COURTSDOWNSTATUS</b></td></tr>"


#####################################################################################################
COLOUR="92d050"
if [ "$MQ_OVERALL_STATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>MQ Down: $MQ_APPEND<BR>";
fi

MQSTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>MQ</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if we can telnet to MQ  </td><td width='10%' align='center' bgcolor='$COLOUR'>$MQ_OVERALL_STATUS</b></td></tr>"


COLOUR="92d050"
if [ "$THICK_CLIENT_SECURE_OVERALL_STATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>Thick client secure port 4430 is not responding at : $THICK_CLIENT_SECURE_APPEND<BR>";
fi

THICKCLIENTSECURESTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Thick Client Secure</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if we can telnet to Thick client secure port  </td><td width='10%' align='center' bgcolor='$COLOUR'>$THICK_CLIENT_SECURE_OVERALL_STATUS</b></td></tr>"

COLOUR="92d050"
if [ "$THICK_CLIENT_NON_SECURE_OVERALL_STATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>Thick client non secure port 7071 is not responding at : $THICK_CLIENT_NON_SECURE_APPEND<BR>";
fi

THICKCLIENTNONSECURESTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Thick Client Non Secure</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if we can telnet to Thick client non secure port  </td><td width='10%' align='center' bgcolor='$COLOUR'>$THICK_CLIENT_NON_SECURE_OVERALL_STATUS</b></td></tr>"

#COLOUR="92d050"
#if [ "$SCJSE_OVERALL_STATUS" != "$STATUSOK" ]
#then
#        COLOUR="Red"
#        APPENDNOTE="$APPENDNOTE<P>SCJSE Down: $SCJSE_APPEND<BR>";
#fi

#SCJSESTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>SCJSE</td><td style='{border-right: 1px solid #000000;}'><font size=2
#face='Calibri'>Check if we can telnet to SCJSE  </td><td width='10%' align='center' bgcolor='$COLOUR'>$SCJSE_OVERALL_STATUS</b></td></tr>"

COLOUR="92d050"
if [ "$WMB_MESSAGE_QUEUE_TABLE_STATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>WMB MESSAGE QUEUE TABLE Record count is: $WMB_MESSAGE_QUEUE_TABLE_COUNT<BR>";
fi

WMBMESSAGEQUEUETABLESTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Queue Table</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Count of records in WMB_MESSAGE_QUEUE_TABLE </td><td width='10%' align='center' bgcolor='$COLOUR'>$WMB_MESSAGE_QUEUE_TABLE_STATUS</b></td></tr>"

COLOUR="92d050"
if [ "$DB_OVERALL_SCHEMA_STATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>DB Schema is not responding for : $DB_SCHEMA_APPEND<BR>";
fi

DBSCHEMASTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>DB Schema</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if we can sqlplus to each DB schema  </td><td width='10%' align='center' bgcolor='$COLOUR'>$DB_OVERALL_SCHEMA_STATUS</b></td></tr>"

COLOUR="92d050"
if [ "$DB_TNSPING_OVERALL_STATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>DB TNSPING not OK for : $DB_TNSPING_APPEND<BR>";
fi

DBTNSPINGSTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>DB TNSping</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if we can tnsping to DB  </td><td width='10%' align='center' bgcolor='$COLOUR'>$DB_TNSPING_OVERALL_STATUS</b></td></tr>"


COLOUR="92d050"
if [ "$MQSIARCHIVE_FOLDER_STATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>MQSIArchive folder is not present in : $MQSIARCHIVE_FOLDER_APPEND<BR>";
fi

MQSIARCHIVEFOLDERSTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>MQSIArchive Folder</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if Mqsiarchive folder is present </td><td width='10%' align='center' bgcolor='$COLOUR'>$MQSIARCHIVE_FOLDER_STATUS</b></td></tr>"



COLOUR="92d050"
if [ "$HK_STATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>Housekeeping job has not completed <BR>";
fi

HOUSEKEEPINGSTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Housekeeping</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if HK job has completed successfully </td><td width='10%' align='center' bgcolor='$COLOUR'>$HK_STATUS</b></td></tr>"

#####################################################################################################
COLOUR="92d050"
if [ "$DARTS_STATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>DARTS connectivity is not working <BR>";
fi

DARTSSTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>DARTS</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check if XHIBIT is able to connect to DARTS </td><td width='10%' align='center' bgcolor='$COLOUR'>$DARTS_STATUS</b></td></tr>"
#####################################################################################################

#####################################################################################################
COLOUR="92d050"
if [ "$LAA_STATUS" != "$STATUSOK" ]
then
        COLOUR="Red"
        APPENDNOTE="$APPENDNOTE<P>There are LAA record transformation failures <BR>";
fi

LAASTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>LAA</td><td style='{border-right: 1px solid #000000
;}'><font size=2
face='Calibri'>Check if there are any LAA document transformation failures </td><td width='10%' align='center' bgcolor='$COLOUR'>$LAA_STATUS</b></td></tr>"
#####################################################################################################

COLOUR="92d050"
if [ "$DARTSRESULTSSTATUS" != "$STATUSOK" ]
then
	COLOUR="Red"
	APPENDNOTE="$APPENDNOTE<P>There are DAR_NEW_MESSAGES in the table : Count = $DARTSRESULTS<BR>";
fi

DARNEWMESSAGESSTATUS="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>DAR_NEW_MESSAGES</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Check the DAR_NEW_MESSAGES volume </td><td width='10%' align='center' bgcolor='$COLOUR'>$DARTSRESULTS</b></td></tr>"



COLOUR="92d050"

RUNNINGTIMEMSG="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Daily List running Time</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Last night's running time for daily lists</td><td width='10%' align='center'bgcolor='$COLOUR'>$RUNNINGTIME</b></td></tr>"

COUNTCASEMSG="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>Count Case</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>The number of cases processed overnight</td><td width='10%' align='center'bgcolor='$COLOUR'>$COUNTCASE</b></td></tr>"

if [ "$HK_CASE_COUNT" -gt "500" ]
then
	COLOUR="Red"
	APPENDNOTE="$APPENDNOTE<P>Number of cases to be deleted is greater than 500 <BR>";
fi

HKREMAININGCASECOUNTMSG="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>HK Remaining Case Count</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>The number of HK cases remaining to be processed </td><td width='10%' align='center'bgcolor='$COLOUR'>$HK_CASE_COUNT</b></td></tr>"

COLOUR="92d050"

COUNTDELETEDMSG="<tr><td width='20%' style='{border-right: 1px solid #000000 ;}'><font size=2 face='Calibri'>HK Case/Lists deleted</td><td style='{border-right: 1px solid #000000;}'><font size=2
face='Calibri'>Cases Deleted, Case Errors, Lists deleted</td><td width='10%' align='center'bgcolor='$COLOUR'>$CASE_DELETION_COUNT, $LIST_DELETION_COUNT</b></td></tr>"

ENDPAGE="</table>$APPENDNOTE";

SUBJECT="DAILY CHECKS: OK"
if [ `echo $APPENDNOTE | wc -c` -gt 1 ]
then
	SUBJECT="DAILY CHECKS: NOT OK"
fi

MESSAGE="$STARTPAGE$HEADER$THICKCLIENT$PUBLICDISPLAY$MESSAGING$THINCLIENTAPPS$FREEMEMORY$BROKER"
MESSAGE="$MESSAGE$REFRESH$DAILYLISTS1$EXISS$SMS$BROKERABENDS$CJIPPROXY$COURTSDOWN$MQSTATUS$THICKCLIENTSECURESTATUS$THICKCLIENTNONSECURESTATUS$WMBMESSAGEQUEUETABLESTATUS$DBSCHEMASTATUS$DBTNSPINGSTATUS$MQSIARCHIVEFOLDERSTATUS$DARTSSTATUS$LAASTATUS$HOUSEKEEPINGSTATUS$DARNEWMESSAGESSTATUS$RUNNINGTIMEMSG$COUNTCASEMSG$HKREMAININGCASECOUNTMSG$COUNTDELETEDMSG$ENDPAGE"

echo $MESSAGE > $SCRIPT_HOME/archive/morningchecks_$TODATE.txt

#now sendmail
  EMAIL_RECIPIENTS="xhibit_support@logica.com"
  #EMAIL_RECIPIENTS="vishwanath.mallya@cgi.com"
  EMAILCMD="$SCRIPT_HOME/sendemail.pl -f xhibitMorningChecks@justice.gov.uk -t $EMAIL_RECIPIENTS -u $SUBJECT -s $SMTP_SERVER -m $MESSAGE "
  $EMAILCMD
