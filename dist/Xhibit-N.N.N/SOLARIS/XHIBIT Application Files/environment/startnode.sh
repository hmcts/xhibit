#!/bin/sh

JAVA_HOME=/software/bea_92/jdk150_04
LISTEN_ADDRESS=midtier2
LISTEN_PORT=4444
LC_CTYPE=en_GB.ISO8859-15
NLS_LANG="ENGLISH_UNITED KINGDOM.WE8ISO8859P1"


WL_HOME="/software/bea_92/weblogic92"
. "${WL_HOME}/common/bin/commEnv.sh"
NODEMGR_HOME="/software/apps/xhibit_2/managed/node"
SECURITY_PATH=/software/apps/xhibit_2/common/security

if [ "${MEM_ARGS}" = "" ]
then
MEM_ARGS="-Xms32m -Xmx200m"
fi


CLASSPATH=${WEBLOGIC_CLASSPATH}${CLASSPATH}
export CLASSPATH PATH NLS_LANG LC_CTYPE SECURITY_PATH

echo CLASSPATH IS SET TO:
echo ${CLASSPATH}
echo END CLASSPATH


cd "${NODEMGR_HOME}"


     "${JAVA_HOME}/bin/java" ${JAVA_VM} ${MEM_ARGS} ${JAVA_OPTIONS} \
     -Djava.security.policy="${WL_HOME}/server/lib/weblogic.policy" \
     -Dweblogic.nodemanager.javaHome="${JAVA_HOME}" \
     -DListenAddress="${LISTEN_ADDRESS}" \
     -DListenPort="${LISTEN_PORT}" \
     -Dweblogic.nodemanager.debugEnabled=true \
     -Dweblogic.security.SSL.ignoreHostnameVerification=true \
     -Dweblogic.nodemanager.SecureListener=true \
     weblogic.NodeManager -v  &

