#!/bin/sh

NOTIFY_HOME=/export/home/xhibit/scripts/publicDisplayNotifier
LOGDIR=${NOTIFY_HOME}/logs
[ ! -d $LOGDIR ] && mkdir $LOGDIR 

JAVA_HOME=${NOTIFY_HOME}/jre-1.6.0_45
JAVA_CMD=${JAVA_HOME}/bin/java

#
# System Properties
#

WL_URL="t3://midcluster:7071"
JMS_CONNECTION_FACTORY=CSJMSConnectionFactory
JMS_TOPIC_NAME=PublicDisplay


#
# set  JAVA_OPTIONS
#

JAVA_OPTS="-Xms32m -Xmx64m"

#
# Set classpath to the correct values.
#

CLASSPATH=${NOTIFY_HOME}/lib/weblogic.jar
CLASSPATH=${CLASSPATH}:${NOTIFY_HOME}/lib/DailyListNotifier.jar
CLASSPATH=${CLASSPATH}:${NOTIFY_HOME}/lib/wlclient.jar
CLASSPATH=${CLASSPATH}:${NOTIFY_HOME}/lib/wljmsclient.jar
CLASSPATH=${CLASSPATH}:${NOTIFY_HOME}/lib/wlfullclient.jar


#
# Run the app !
#
echo `date`>>$NOTIFY_HOME/logs/notify.log
echo Midtier URL: $WL_URL JMS Connection Factor: $JMS_CONNECTION_FACTORY JMS Topic Name: $JMS_TOPIC_NAME   >> $NOTIFY_HOME/logs/notify.log

$JAVA_CMD $JAVA_OPTS -classpath $CLASSPATH uk.gov.courtservice.xhibit.integration.publicdisplay.jms.DailyListNotifier $WL_URL $JMS_CONNECTION_FACTORY $JMS_TOPIC_NAME $1 $2|tee -a $NOTIFY_HOME/logs/notify.log
echo "------------------------------------------------------------"|tee -a $NOTIFY_HOME/logs/notify.log


