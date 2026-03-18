#!/bin/ksh
# file:         xhibitDashboardScript.sh
# Created:      20-September-2016
# Creator:      Scott Atwell
# Purpose:      To support the Application Management Dashboard
#
# Modification History
# Date          	By      Change
# 20-September-2016   	SA      Initial check in of this file
#
#
AMS_SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/amdashboard
SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron/morningChecks
DL_EMAIL_CHECKER_HOME=/opt/moj/home/wmbroker/bin/cron/dlEmailChecker/sql
. $SCRIPT_HOME/../setEnv.sh

SOURCE_FOLDER=$AMS_SCRIPT_HOME
DESTINATION_FOLDER=""
ARCHIVE_FOLDER=$AMS_SCRIPT_HOME/archive
LOG_FILE=$ARCHIVE_FOLDER/sftp.log
BOARDING_SERVER=x.y.198.9
BOARDING_USER_ID=amdash_xhibit

PRESM1_IP=x.y.199.81
PRESM2_IP=x.y.199.82

CSH1_IP=x.y.66.70
CSH2_IP=x.y.66.72

MIDM1_IP=x.y.250.138
MIDM1_WL_IP=x.y.199.97

MIDM2_IP=x.y.250.139
MIDM2_WL_IP=x.y.199.98

MIDM3_IP=x.y.250.140
MIDM3_WL_IP=x.y.199.99

#SCJSE_80_IP=x.y.197.161
#SCJSE_7071_IP=x.y.199.113


LOGDATETIME="`date '+%d%m%Y_%H%M%S'`"
DAY="`date '+%a'`"
password=xhibit

find $ARCHIVE_FOLDER/ -name "XHIBIT*.csv" -type f -mtime +5 |xargs rm
mv $AMS_SCRIPT_HOME/XHIBIT.csv $ARCHIVE_FOLDER/XHIBIT_$LOGDATETIME.csv

# check the public display
HTTPUP1=`$SCRIPT_HOME/presBox.sh $PRESM1_IP 7071 | telnet` > /dev/null 2>&1
HTTPUP2=`$SCRIPT_HOME/presBox.sh $PRESM2_IP 7071 | telnet`

HTTPUP1GREP=`echo $HTTPUP1 | grep rhs`
HTTPUP2GREP=`echo $HTTPUP2 | grep rhs`
PUBLICDISPLAYSTATUS="Ok"
PUBLIC_DISPAY_ERROR=""

if [ `echo $HTTPUP1GREP | wc -m` -lt 3 ] && [ `echo $HTTPUP2GREP | wc -m` -lt 3 ]  
then
    PUBLICDISPLAYSTATUS="Not Ok"
    PUBLIC_DISPAY_ERROR="DISPLAY,"
fi
# thin client
THINCLIENT=`$SCRIPT_HOME/probService.sh $PRESM1_IP 7071 | telnet`
THINCLIENT1=`$SCRIPT_HOME/probService.sh $PRESM2_IP 7071 | telnet`
RESPONSETHIN=`echo $THINCLIENT | grep 4430`
RESPONSETHIN1=`echo $THINCLIENT1 | grep 4430`
THINCLIENTSTATUS="Ok"
THIN_CLIENT_DOWN=""
if [ `echo $RESPONSETHIN | wc -c` -eq 1 ] && [ `echo $RESPONSETHIN1 | wc -c` -eq 1 ]
then
   THINCLIENTSTATUS="Not Ok"
   THIN_CLIENT_DOWN="PRES,"
fi

# BROKER UP
BROKERUP=`/opt/moj/home/wmbroker/bin/ha/hamqsi_monitor_broker_as BRK_CSH_1 QM_CSH_1`
BROKERUPSTATIC=`cat $SCRIPT_HOME/static/brokerup.txt`
BROKERSTATUS="Ok"
BROKER_DOWN=""
if [ "$BROKERUP" != "$BROKERUPSTATIC" ]
then
    BROKERSTATUS="Not Ok"
    BROKER_DOWN="CSH1,"
fi


BROKER2UP=`ssh wmbroker@x.y.66.72 "/opt/moj/home/wmbroker/bin/ha/hamqsi_monitor_broker_as BRK_CSH_2 QM_CSH_2"`
BROKER2UP=`echo $BROKER2UP|grep hamqsi|tr -d '\r'`
BROKER2UPSTATIC=`cat $SCRIPT_HOME/static/broker2up.txt`
BROKER2_DOWN=""
if [ "$BROKER2UP" != "$BROKER2UPSTATIC" ]
then
    BROKERSTATUS="Not Ok"
    BROKER_DOWN="$BROKER_DOWN CSH2,"
fi

# broker abends

ABEND_DIR="/var/mqsi/common/errors"
ABEND_FILE_LIST=`ls $ABEND_DIR/*.abend`
if [[ -n $ABEND_FILE_LIST ]]
then
	SERVICESDOWN="BROKER_ABEND,"
fi

#Check CJIT SMS Messages
SMSLIMIT=3000
SMSCOUNT=`sqlplus -s <username>/<password>@csdbprd2 << END
  set heading off feedback off
SELECT count(*) FROM cji_ahm WHERE status_id in (1) AND trunc(sent_agg_date) = trunc(sysdate );
`
SMSCOUNT=`echo $SMSCOUNT|tr -d '\r'`
SMS=""
if [ "$SMSCOUNT" -gt "$SMSLIMIT" ];
then
	SMS="SMS,"
fi

# check CJIT
CJIPLIMIT=100
CJIPCOUNT=`sqlplus -s <username>/<password>@csdbprd2 << END
  set heading off feedback off
select count(*) From CJI_EVENT T where status_id = 1 ;
`
CJIPCOUNT=`echo $CJIPCOUNT|tr -d '\r'`
CJIP=""
if [ "$CJIPCOUNT" -gt "$CJIPLIMIT" ];
then
	CJIP="CJIP,"
fi


# check LAA
#LAALIMIT=500000
#LAACOUNT=`sqlplus -s <username>/<password>@csdbprd3 << END
#  set heading off feedback off
#select count(*) From GDG_OUTBOUND_MESSAGES where trunc(request_timestamp)>=trunc(sysdate) and OUTBOUND_STATUS_ID <> 1002 ;
#`
#LAACOUNT=`echo $LAACOUNT|tr -d '\r'`
#LAA=""
#if [ "$LAACOUNT" -gt "$LAALIMIT" ];
#then
#        LAA="LAA,"
#fi


DARTS_STATUS="0"
# Check if there are any messages in the DAR_NEW_MESSAGES table
#DARTSRESULTS=`sqlplus -s <username>/<password>@csdbprd2 << END
#  set heading off feedback off
#select count(*) from dar_new_messages;
#`
#DARTSRESULTS=`echo $DARTSRESULTS|tr -d '\r'`
DARTS=""
#if [ "$DARTSRESULTS" -gt "500" ];
#then
#        DARTS="DARTS_INTERFACE,"
#	DARTS_STATUS="1"
#fi


DARTSCOUNT=`sqlplus -s <username>/<password>@csdbprd2 << END
  set heading off feedback off
SELECT count(*) FROM DAR_PRIORITY_NEW_MESSAGES ;
`
DARTSCOUNT=`echo $DARTSCOUNT|tr -d '\r'`

if [ "$DARTSCOUNT" -gt "100" ];
then
	DARTS="DARTS,"
#	SERVICESDOWN="$SERVICESDOWN DARTS_SLOW,"
#	DARTS_STATUS="2"
fi

#if [ "$DARTS_STATUS" = "2" ];
#then
#	SERVICESDOWN="$SERVICESDOWN DARTS_DOWN,"
#elif [ "$DARTS_STATUS" = "1" ];
#then
#	SERVICESDOWN="$SERVICESDOWN DARTS_SLOW,"
#fi
#############################################################################################################

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
                DB_TNSPING_APPEND="$DB_TNSPING_APPEND WMBCSH,"
        fi

tnsping_function CSDBPRD1
        if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                DB_TNSPING_APPEND="$DB_TNSPING_APPEND CSDBPRD1,"
        fi

tnsping_function CSDBPRD2
        if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                DB_TNSPING_APPEND="$DB_TNSPING_APPEND CSDBPRD2,"
        fi

#tnsping_function CSDBPRD3
#        if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
#        then
#                DB_TNSPING_APPEND="$DB_TNSPING_APPEND CSDBPRD3,"
#        fi

sqlplus_function xhibit CSDBPRD1
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
		DB_SCHEMA_APPEND="$DB_SCHEMA_APPEND XHIBIT,"
	fi

sqlplus_function cjit CSDBPRD2
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
		DB_SCHEMA_APPEND="$DB_SCHEMA_APPEND CJIT,"
	fi

#sqlplus_function exiss CSDBPRD2
#	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
#        then
#		DB_SCHEMA_APPEND="$DB_SCHEMA_APPEND EXISS,"
#	fi

sqlplus_function darts CSDBPRD2
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
		DB_SCHEMA_APPEND="$DB_SCHEMA_APPEND DARTS,"
	fi

#sqlplus_function gdgate CSDBPRD3
#	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
#        then
#		DB_SCHEMA_APPEND="$DB_SCHEMA_APPEND GDGATE,"
#	fi

telnet_function $MIDM1_WL_IP 4430
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_SECURE_APPEND="$THICK_CLIENT_SECURE_APPEND MIDM1,"
        fi

telnet_function $MIDM2_WL_IP 4430
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_SECURE_APPEND="$THICK_CLIENT_SECURE_APPEND MIDM2,"
        fi

telnet_function $MIDM3_WL_IP 4430
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_SECURE_APPEND="$THICK_CLIENT_SECURE_APPEND MIDM3,"
        fi


telnet_function $MIDM1_WL_IP 7071
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_NON_SECURE_APPEND="$THICK_CLIENT_NON_SECURE_APPEND MIDM1,"
        fi

telnet_function $MIDM2_WL_IP 7071
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_NON_SECURE_APPEND="$THICK_CLIENT_NON_SECURE_APPEND MIDM2,"
        fi

telnet_function $MIDM3_WL_IP 7071
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                THICK_CLIENT_NON_SECURE_APPEND="$THICK_CLIENT_NON_SECURE_APPEND MIDM3,"
        fi

#telnet_function $SCJSE_7071_IP 7071
#	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
#        then
#                SCJSE_APPEND="$SCJSE_APPEND SCJSE(7071),"
#        fi

#telnet_function $SCJSE_80_IP 80
#	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
#        then
#                SCJSE_APPEND="$SCJSE_APPEND SCJSE(80),"
#        fi

telnet_function $CSH1_IP 1411
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                MQ_APPEND="$MQ_APPEND CSH1,"
        fi

telnet_function $CSH2_IP 1412
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                MQ_APPEND="$MQ_APPEND CSH2,"
        fi

telnet_function $MIDM1_IP 1413
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                MQ_APPEND="$MQ_APPEND MIDM1,"
        fi

telnet_function $MIDM2_IP 1414
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                MQ_APPEND="$MQ_APPEND MIDM2,"
        fi

telnet_function $MIDM3_IP 1415
	if [ "$FUNCTION_RETURN_VALUE" -ne "0" ]
        then
                MQ_APPEND="$MQ_APPEND MIDM3,"
        fi

WMB_MESSAGE_QUEUE_TABLE_COUNT=`sqlplus -s xhibit/xhibit@csdbprd1 <<END
  set heading off feedback off
select count(*) from wmb_message_queue_table;
`
if [ $WMB_MESSAGE_QUEUE_TABLE_COUNT -gt 250 ]
then
SERVICESDOWN="$SERVICESDOWN BROKER_MESSAGE_QUEUE_TABLE_STATUS,"
fi




#############################################################################################################

##### Ok now put it all together
FILEFORDASHBOARD=$AMS_SCRIPT_HOME/XHIBIT.csv

SPOKEFILE="$SCRIPT_HOME/../court_list_down.txt"
SPOKESDOWN=`cat $SPOKEFILE|sort|uniq -d|tr -s '\n' ','|awk '{print substr($0,1,length($0)-1) }'`

# Servers down is a combo of THICK_CLIENT_SECURE and PUBLICDISPLAYSTATUS and BROKERSTATUS 
SERVERSDOWN="$THICK_CLIENT_SECURE_APPEND $THICK_CLIENT_NON_SECURE_APPEND $BROKER_DOWN $THIN_CLIENT_DOWN $PUBLIC_DISPAY_ERROR"
SERVERSDOWN=`echo $SERVERSDOWN|awk '{print substr($0,1,length($0)-1) }'`

# DBs down is DB_OVERALL_SCHEMA_STATUS 
DBDOWN="$DB_TNSPING_APPEND $DB_SCHEMA_APPEND"
DBDOWN=`echo $DBDOWN|awk '{print substr($0,1,length($0)-1) }'`

# MQs down is MQ_APPEND
MQDOWN="$MQ_APPEND"
MQDOWN=`echo $MQDOWN|awk '{print substr($0,1,length($0)-1) }'`

# Interfaces down is a combo of CJIPSTATUS and SMSSTATUS and DARTS_STATUS
INTERFACESDOWN="$CJIP $SMS $DARTS"
INTERFACESDOWN=`echo $INTERFACESDOWN|awk '{print substr($0,1,length($0)-1) }'`

# Refresh resync is reading the output file from the existing job that runs and looking for issues
RR_VALUE_S_CHECK_FILENAME=$SCRIPT_HOME/../output/refresh_resync_checker_S.txt
RR=`sed -n '6,6p' $RR_VALUE_S_CHECK_FILENAME`
RR=`echo $RR|tr -s ' '`

# Daily lists is reading the output file from the existing job that runs and looking for issues
# Note if the time is before 345 pm then this will always be ok
TODATE="`date '+%u'`"

echo $TODATE

if [ $TODATE = 5 ]
then
NOCOURTS=`sqlplus -s xhibit/$password@${ORACLE_SID} @$DL_EMAIL_CHECKER_HOME/dl_courtCount_fri.sql`
else
NOCOURTS=`sqlplus -s xhibit/$password@${ORACLE_SID} @$DL_EMAIL_CHECKER_HOME/dl_courtCount.sql`
fi

NOCOURTS=`echo $NOCOURTS|tr -d '\r'`

# Services is a combo of WMB_MESSAGE_QUEUE_TABLE_STATUS and BROKERABENDSTATUS and DARTSRESULTSSTATUS 
SERVICESDOWN=`echo $SERVICESDOWN|awk '{print substr($0,1,length($0)-1) }'`



# User count is reading the output from the user count job that is now being kicked off every 5 mins too
USERCOUNT=`sqlplus -s xhibit/$password@${ORACLE_SID} << END
  set heading off feedback off
select count(*) from aud_user_logins where logged_in='Y';
`
USERCOUNT=`echo $USERCOUNT|tr -d '\r'`


# Validation count added after recent P2
VALIDATIONCOUNT=`sqlplus -s xhibit/$password@${ORACLE_SID} << END
  set heading off feedback off
select count(*) from xhb_validation where status !='S';
`
VALIDATIONCOUNT=`echo $VALIDATIONCOUNT|tr -d '\r'`


# Unprocessed email count added after recent P2
EMAILCOUNT=`sqlplus -s xhibit/$password@${ORACLE_SID} << END
  set heading off feedback off
select count(*) from xhb_email2 where mail_id> (select max(mail_id)-2000 from xhb_email2) and STATUS != 'S';
`
EMAILCOUNT=`echo $EMAILCOUNT|tr -d '\r'`

if [ "`expr $EMAILCOUNT '<' 1750`" = "1" ]
then
	EMAILCOUNT=0
fi


DASHBOARDDATETIME="`date '+%d/%m/%Y %H:%M:%S'`"
echo $DASHBOARDDATETIME > $FILEFORDASHBOARD
echo "Spokes: $SPOKESDOWN" >> $FILEFORDASHBOARD
echo "Servers: $SERVERSDOWN" >> $FILEFORDASHBOARD
echo "DBs: $DBDOWN" >> $FILEFORDASHBOARD
echo "MQ: $MQDOWN" >> $FILEFORDASHBOARD
echo "Interfaces: $INTERFACESDOWN" >> $FILEFORDASHBOARD
echo "RefreshResync: $RR" >> $FILEFORDASHBOARD
echo "DailyLists: $NOCOURTS" >> $FILEFORDASHBOARD
echo "Services: $SERVICESDOWN" >> $FILEFORDASHBOARD
echo "UserCount: $USERCOUNT" >> $FILEFORDASHBOARD
echo "Unprocessed Document Count: $VALIDATIONCOUNT" >> $FILEFORDASHBOARD
echo "Unprocessed Email Count: $EMAILCOUNT" >> $FILEFORDASHBOARD


echo "sftping file.." >> $LOG_FILE
sftp $BOARDING_USER_ID@$BOARDING_SERVER <<EOF
lcd $SOURCE_FOLDER
put $DESTINATION_FOLDER/$FILEFORDASHBOARD
EOF

echo "sftp complete.." >> $LOG_FILE

exit 0
