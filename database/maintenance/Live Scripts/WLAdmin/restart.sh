#!/bin/ksh
WL_HOME=/opt/customer/bea/user_projects/domains/XHIBIT_LIVE
SCRIPT_DIR=/home/oracle/bin/cron
SCRIPT_NAME=restart.py
TEMP_SCRIPT_NAME=template.py

. ${WL_HOME}/bin/setDomainEnv.sh
rm $SCRIPT_DIR/$SCRIPT_NAME
echo $URL

URL=`perl -MMIME::Base64 -ne 'print decode_base64($_)' < $SCRIPT_DIR/url.txt`
START_TIME=`date`
echo Script started at $START_TIME >> $SCRIPT_DIR/restart.out

echo "connect(username='weblogic', password='$URL', url='t3://x.y.250.143:7001',adminServerName='AdminServer')" > $SCRIPT_DIR/$SCRIPT_NAME
echo " " >> $SCRIPT_DIR/$SCRIPT_NAME
cat $SCRIPT_DIR/$TEMP_SCRIPT_NAME >> $SCRIPT_DIR/$SCRIPT_NAME 
 ${JAVA_HOME}/bin/java ${JAVA_OPTIONS} weblogic.WLST  $SCRIPT_DIR/$SCRIPT_NAME >> $SCRIPT_DIR/restart.out 2>&1
END_TIME=`date`
echo Script finished at $END_TIME >> $SCRIPT_DIR/restart.out

rm $SCRIPT_DIR/$SCRIPT_NAME

