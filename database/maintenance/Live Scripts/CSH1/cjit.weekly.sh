#!/usr/bin/ksh

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
#SCRIPT_HOME=/opt/moj/home/wmbroker/scratchpad/vish
#. $SCRIPT_HOME/setEnv.sh
. /opt/moj/home/wmbroker/bin/cron/setEnv.sh
FILEPATH=/opt/moj/home/wmbroker/bin/cron/cjit/results
#FILEPATH=/opt/moj/home/wmbroker/scratchpad/vish
todaysDate=`date +"%Y%m%d"`
#todaysDate="20120619"
#FILENAME=$FILEPATH/select_$todaysDate.txt
WEEKDAYS=6
EMAIL_SCRIPT=/opt/moj/home/wmbroker/bin/sendemail.pl
SMTP_SERVER=SMTPRelayN.dom1.infra.int
# Added recipients to investigate email issues
EMAIL_RECIPIENTS="janine.hughes@cgi.cjsm.net;peter.appleton@atos.cjsm.net;admin1.cjse@cjit.cjsm.net;vishwanath.mallya@cgi.cjsm.net;mike.piper@cgi.cjsm.net"
EMAIL_SENDER=xhibitcsh@justice.gov.uk

rm $SCRIPT_HOME/CJITReports.zip

while [ "$WEEKDAYS" -gt "1" ]
do
echo WEEKDAYS=$WEEKDAYS
HOURS=`expr $WEEKDAYS '*' 24`
echo HOURS=$HOURS
todaysDate=`TZ=GMT+$HOURS date +%Y%m%d`
echo TodaysDate=$todaysDate
HOURS=`expr $HOURS '+' 23`
echo HOURS=$HOURS

FILENAME=$FILEPATH/select_$todaysDate.txt

maxTime=0
minTime=100
avgTime=0
totalCount=0
timeGreaterThan10Minutes=0

totalLines=`cat $FILENAME|wc -l`

echo $totalLines
totalLines=`expr $totalLines - 2`
echo $totalLines

lineNumber=0
while read line
do
lineNumber=`expr $lineNumber + 1`
if [ "$lineNumber" -gt "3" ] && [ "$lineNumber" -lt "$totalLines" ]
then
echo $line >> $SCRIPT_HOME/select_$todaysDate.tmp
fi
done < $FILENAME

if [ -f "$SCRIPT_HOME/select_$todaysDate.tmp" ]
then


while read line
do
totalCount=`expr $totalCount + 1`
startTime=`echo "$line"|tr -s ' '|cut -f3 -d ' '`
endTime=`echo "$line"|tr -s ' '|cut -f5 -d ' '`
#echo $startTime $endTime
sth=`echo $startTime|tr -s ' '|cut -f1 -d ":"`
stm=`echo $startTime|tr -s ' '|cut -f2 -d ":"`
sts=`echo $startTime|tr -s ' '|cut -f3 -d ":"`

eth=`echo $endTime|tr -s ' '|cut -f1 -d ":"`
etm=`echo $endTime|tr -s ' '|cut -f2 -d ":"`
ets=`echo $endTime|tr -s ' '|cut -f3 -d ":"`

startSeconds=`expr $sth \* 3600 + $stm \* 60 + $sts`
#echo $startSeconds
endSeconds=`expr $eth \* 3600 + $etm \* 60 + $ets`
#echo $endSeconds
totalTimeInSeconds=`expr $endSeconds - $startSeconds`
if [ "$totalTimeInSeconds" -lt "0" ]
then
#totalTimeInSeconds="5"
totalTimeInSeconds=`expr 86400 - $startSeconds`
totalTimeInSeconds=`expr $totalTimeInSeconds + $endSeconds`
fi
#echo $totalTimeInSeconds
if [ "$totalTimeInSeconds" -gt "$maxTime" ] 
then
maxTime=$totalTimeInSeconds
fi
if [ "$totalTimeInSeconds" -lt "$minTime" ] 
then
minTime=$totalTimeInSeconds
fi
if [ "$totalTimeInSeconds" -gt "600" ] 
then
timeGreaterThan10Minutes=`expr $timeGreaterThan10Minutes + 1`
fi
avgTime=`expr $avgTime + $totalTimeInSeconds`
done < $SCRIPT_HOME/select_$todaysDate.tmp

#avgTime=`expr $avgTime \/ $totalCount`
echo -----------
echo $avgTime
echo $totalCount
echo $timeGreaterThan10Minutes
echo $totalCount
echo ---------------
avgTime=`echo "$avgTime / $totalCount"|	bc -l`
meanTime=`echo "scale=2;$timeGreaterThan10Minutes * 100 / $totalCount"|bc -l`
#meanTime=`expr $timeGreaterThan10Minutes \/ $totalCount`
yesterdaysDate=`TZ=GMT+$HOURS date +%d/%m/%Y`
echo $yesterdaysDate
echo totalCount=$totalCount
echo requests taking more than 10 minutes=$timeGreaterThan10Minutes
echo Percentage=$meanTime
echo minTime=$minTime
echo maxTime=$maxTime
echo meanTime=$avgTime

maxTime=`echo $maxTime | awk -v  '{printf "%d:%d:%d\n",$maxTime/(60*60),$maxTime%(60*60)/60,$maxTime%60}'`
minTime=`echo $minTime | awk -v  '{printf "%d:%d:%d\n",$minTime/(60*60),$minTime%(60*60)/60,$minTime%60}'`
avgTime=`echo $avgTime | awk -v  '{printf "%d:%d:%d\n",$avgTime/(60*60),$avgTime%(60*60)/60,$avgTime%60}'`

echo "SUMMARY REPORT					$yesterdaysDate" >> $SCRIPT_HOME/cjit_report.txt
echo "								" >> $SCRIPT_HOME/cjit_report.txt
echo "Total Number of Documents			$totalCount" >> $SCRIPT_HOME/cjit_report.txt
echo "Number taking over 10 minutes to send	$timeGreaterThan10Minutes" >> $SCRIPT_HOME/cjit_report.txt
echo "Percentage						$meanTime%" >> $SCRIPT_HOME/cjit_report.txt
echo "								" >> $SCRIPT_HOME/cjit_report.txt
echo "Minimum Time to Send (HH:MM:SS)		$minTime" >> $SCRIPT_HOME/cjit_report.txt
echo "Maximum Time to Send (HH:MM:SS)		$maxTime" >> $SCRIPT_HOME/cjit_report.txt
echo "Mean Time to Send (HH:MM:SS)			$avgTime" >> $SCRIPT_HOME/cjit_report.txt
echo "							">> $SCRIPT_HOME/cjit_report.txt
echo "							">> $SCRIPT_HOME/cjit_report.txt
echo "							">> $SCRIPT_HOME/cjit_report.txt

cp $FILEPATH/cjit_report_$todaysDate.txt $SCRIPT_HOME/cjit_reports/
rm $SCRIPT_HOME/select_$todaysDate.tmp

fi
WEEKDAYS=`expr $WEEKDAYS '-' 1`
done

tar cvf $SCRIPT_HOME/CJITReports.tar $SCRIPT_HOME/cjit_reports/*
gzip $SCRIPT_HOME/CJITReports.tar
cp $SCRIPT_HOME/CJITReports.tar.gz $SCRIPT_HOME/CJITReports.zip
emailCmd="$EMAIL_SCRIPT -f $EMAIL_SENDER -t $EMAIL_RECIPIENTS -u Status of events from XHIBIT for last week -s $SMTP_SERVER -m Please find attached the daily stats. Files are now being sent in .zip extension, as CJSM now blocks .gz files. Please rename the .zip file to .gz before extracting. -a $SCRIPT_HOME/cjit_report.txt -a $SCRIPT_HOME/CJITReports.zip"

`$emailCmd`
rm $SCRIPT_HOME/cjit_report.txt
rm $SCRIPT_HOME/CJITReports.tar.gz
rm $SCRIPT_HOME/cjit_reports/*
