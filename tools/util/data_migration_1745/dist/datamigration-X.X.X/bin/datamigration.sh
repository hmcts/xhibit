#!/bin/sh


#
# Set the following 
#
JAVA_HOME=/software/bea_92/jdk150_04
export JAVA_HOME


##############################

if [ $# -lt 1 ]; then
	echo "please supply a password, e.g."
	echo ""
	echo "datamigration.sh the_xhibit_db_password"
	exit 1
fi


JAVA_VENDOR=Sun
export JAVA_VENDOR

${JAVA_HOME}/bin/java -version


PATH=${JAVA_HOME}/bin:$PATH
export PATH


CLASSPATH=${JAVA_HOME}/lib/rt.jar:../lib/log4j-1.2.13.jar:../lib/commons-logging-1.0.3.jar:../lib/castor-1.0.jar:../lib/xalan-2.7.0.jar:../lib/xalan-2.7.0-xercesImpl.jar:../lib/xalan-2.7.0-xml-apis.jar:../lib/xalan.serializer-2.7.0.jar:../lib/ojdbc14.jar:../lib/Config.jar:../lib/xhibit.ant.custom-1.0.jar:../lib/Framework.jar:../lib/XhibitXmlBinding.jar:../lib/datamigration.jar
export CLASSPATH


LOGFILE=dataMigrationDebug.log
echo "Output to $LOGFILE"
${JAVA_HOME}/bin/java -classpath ${CLASSPATH} datamigration1745.DataMigration -p$1 $2 $3 $4 $5 $6 >$LOGFILE


