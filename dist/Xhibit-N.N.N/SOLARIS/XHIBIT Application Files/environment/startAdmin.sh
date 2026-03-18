#!/bin/sh

#
# Start the xhibit AdminServer....
#

JAVA_HOME=/software/bea_92/jdk150_04
WL_HOME=/software/bea_92/weblogic92
DOMAIN_HOME=/software/apps/xhibit_2/xhibit
APP_HOME=/software/apps/xhibit_2/application


#
# Set Java Options
#

# JAVA JAVA_OPTIONS
JAVA_OPTIONS="-client"
JAVA_OPTIONS="${JAVA_OPTIONS} -Xms256m"
JAVA_OPTIONS="${JAVA_OPTIONS} -Xmx256m"
JAVA_OPTIONS="${JAVA_OPTIONS} -XX:PermSize=32m"
JAVA_OPTIONS="${JAVA_OPTIONS} -XX:MaxPermSize=256m"
JAVA_OPTIONS="${JAVA_OPTIONS} -XX:CompileThreshold=8000"
JAVA_OPTIONS="${JAVA_OPTIONS} -da"
JAVA_OPTIONS="${JAVA_OPTIONS} -Xverify:none"

# WEBLOGIC JAVA_OPTIONS"
JAVA_OPTIONS="${JAVA_OPTIONS} -Dplatform.home=${WL_HOME}"
JAVA_OPTIONS="${JAVA_OPTIONS} -Dwls.home=${WL_HOME}/server"
JAVA_OPTIONS="${JAVA_OPTIONS} -Dwli.home=${WL_HOME}/integration"
JAVA_OPTIONS="${JAVA_OPTIONS} -Dweblogic.management.discover=true"
JAVA_OPTIONS="${JAVA_OPTIONS} -Dweblogic.ProductionModeEnabled=true"
JAVA_OPTIONS="${JAVA_OPTIONS} -Dwlw.iterativeDev="
JAVA_OPTIONS="${JAVA_OPTIONS} -Dwlw.testConsole="
JAVA_OPTIONS="${JAVA_OPTIONS} -Dwlw.logErrorsToConsole="
JAVA_OPTIONS="${JAVA_OPTIONS} -Dweblogic.Name=admin"
JAVA_OPTIONS="${JAVA_OPTIONS} -Djava.security.policy=${WL_HOME}/server/lib/weblogic.policy"
JAVA_OPTIONS="${JAVA_OPTIONS} -Dweblogic.alternateTypesDirectory=${APP_HOME}/mbeantypes"
JAVA_OPTIONS="${JAVA_OPTIONS} -Dweblogic.security.SSL.ignoreHostnameVerification=true" 

# XHIBIT JAVA_OPTIONS - the three comented out break the console it there is no lib setup...
#JAVA_OPTIONS="${JAVA_OPTIONS} -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl"
#JAVA_OPTIONS="${JAVA_OPTIONS} -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl"
#JAVA_OPTIONS="${JAVA_OPTIONS} -Djavax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl"
JAVA_OPTIONS="${JAVA_OPTIONS} -Dlog4j.log.dir=${DOMAIN_HOME}/logs"
JAVA_OPTIONS="${JAVA_OPTIONS} -Dweblogic.ext.dirs=/software/apps/xhibit_2/xhibit/lib:/software/apps/xhibit_2/application/lib"


#
# Set Classpath
#

# JAVA CLASSPATH
CLASSPATH=${JAVA_HOME}/lib/tools.jar

# WEBLOGIC CLASSPATH
CLASSPATH=${CLASSPATH}:${WL_HOME}/server/lib/weblogic_sp.jar
CLASSPATH=${CLASSPATH}:${WL_HOME}/server/lib/weblogic.jar
CLASSPATH=${CLASSPATH}:${WL_HOME}/server/lib/webservices.jar
CLASSPATH=${CLASSPATH}:${WL_HOME}/common/eval/pointbase/lib/pbclient51.jar
CLASSPATH=${CLASSPATH}:${WL_HOME}/server/lib/xqrl.jar
CLASSPATH=${CLASSPATH}:${WL_HOME}/integration/lib/util.jar

# XHIBIT CLASSPATH
CLASSPATH=${CLASSPATH}:${DOMAIN_HOME}/lib


#
# Set Path
#

# JAVA PATH
PATH=${JAVA_HOME}/jre/bin
PATH=${PATH}:${JAVA_HOME}/bin

# WEBLOGIC PATH
PATH=${PATH}:${WL_HOME}/server/bin
PATH=${PATH}:${WL_HOME}/server/native/solaris
PATH=${PATH}:${WL_HOME}/server/native/solaris/sparc
PATH=${PATH}:${WL_HOME}/server/native/solaris/sparc64
LD_LIBRARY_PATH=${PATH}

export PATH CLASSPATH LD_LIBRARY_PATH

echo using PATH
echo ${PATH}

echo using CLASSPATH
echo ${CLASSPATH}

echo "${JAVA_HOME}/bin/java ${JAVA_OPTIONS} weblogic.Server"
${JAVA_HOME}/bin/java ${JAVA_OPTIONS} weblogic.Server

