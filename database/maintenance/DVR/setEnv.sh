ORACLE_HOME=/opt/moj/oracle
ORACLE_BASE=/opt/moj/oracle
TNS_HOME=$HOME
ORACLE_SID=o10tst2
JAVA_HOME=/home/wmbroker/bin/jdk1.6.0/
ORACLE_XHIBIT_DB_USER=xhibit
ORACLE_XHIBIT_DB_PASS=xhibit
ORACLE_DARTS_DB_USER=darts
ORACLE_DARTS_DB_PASS=darts

PATH=/home/wmbroker/bin/jdk1.6.0/jre/bin:/usr/bin:/usr/sbin:$ORACLE_HOME/bin:$JAVA_HOME/bin:/etc:/usr/ccs/bin:/usr/openwin/bin:/usr/local/bin:$ORACLE_HOME/lib:/usr/local/bin:/usr/lib/lwp:/opt/csw/bin:$PATH
CLASSPATH=/home/wmbroker/bin/cron/CPPX/jar/CPPX_Add_CLOB_1.6.jar:/home/wmbroker/bin/jdk1.6.0/jre/lib/rt.jar:/opt/IBM/mqsi/6.1/jre15/lib/rt.jar:/opt/IBM/mqsi/6.1/classes/derby.jar:/opt/IBM/mqsi/6.1/classes/ConfigManagerProxy.jar:/opt/IBM/mqsi/6.1/classes/configutil.jar:/opt/mqm/java/lib/com.ibm.mq.jar:/opt/mqm/java/lib/connector.jar:/opt/IBM/mqsi/6.1/messages:/var/mqsi/common/wsrr::/opt/ibm/wsdtx/wmqi/dtxwmqi.jar:/opt/ibm/wsdtx/libs/dtxpi.jar

LD_LIBRARY_PATH=/usr/lib/lwp:$ORACLE_HOME/lib
export ORACLE_HOME TNS_HOME LD_LIBRARY_PATH ORACLE_HOME ORACLE_SID ORACLE_XHIBIT_DB_USER ORACLE_XHIBIT_DB_PASS CLASSPATH
export PATH

