#!/bin/sh
#
## Xhibit Admin Startup Script
#
ADMIN_URL=t3://xhibitadmin:7000
export ADMIN_URL

#### System Properties ####
WL_HOME="/software/bea_92/weblogic92/server"
SERVER_NAME="admin"

#### Java Options ####
JAVA_HOME="/software/bea_92/jdk150_04"

#### Paths ####
#PATH=${WL_HOME}/bin:${WL_HOME}/lib/solaris:${JAVA_HOME}/bin:${JAVA_HOME}/jre/bin:${PATH}

CLASSPATH=${CLASSPATH}:${WL_HOME}/lib/weblogic.jar

#### Start Server ####
echo CLASSPATH=${CLASSPATH}
echo
echo PATH=${PATH}
echo


echo "Admin server password:"
stty -echo
read adminPassword
stty echo

echo Stopping server: ${SERVER_NAME} ...

${JAVA_HOME}/bin/java -classpath ${CLASSPATH} weblogic.Admin -username system -password ${adminPassword} -url ${ADMIN_URL} SHUTDOWN

