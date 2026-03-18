#!/usr/bin/ksh

SCRIPT_HOME=/home/oracle/bin/cron/userCount

todaysDate=`date +"%d%m%Y"`
formattedDate=`date +"%d-%m-%Y"`

`find /opt/customer/bea/user_projects/domains/XHIBIT_LIVE/servers/LIVEpresM1/logs -name "access*" -type f -mtime 0|xargs cat |grep -v '\- \-'|cut -f 3 -d ' '|sort|uniq > $SCRIPT_HOME/userCount_$todaysDate.txt`

while read line
do
if [ $line ]
then

grepOutput=`grep "$line" $SCRIPT_HOME/userLastLogin.txt`
count=`echo $grepOutput|wc -c`
if [ "$count" -lt "2" ]
then
`echo $line $formattedDate >> $SCRIPT_HOME/userLastLogin.txt`
else

Output=`sed "s/$line.[0-9][0-9]-[0-9][0-9]-[0-9][0-9][0-9][0-9]/$line $formattedDate/g" $SCRIPT_HOME/userLastLogin.txt > $SCRIPT_HOME/temp.txt`
mv $SCRIPT_HOME/temp.txt $SCRIPT_HOME/userLastLogin.txt
fi

fi
done < $SCRIPT_HOME/userCount_$todaysDate.txt

