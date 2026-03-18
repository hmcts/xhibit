ORACLE_HOME=/opt/moj/oracle
ORACLE_BASE=/opt/moj/oracle
TNS_HOME=$HOME
ORACLE_SID=o10tst2
JAVA_HOME=/usr/jdk/jdk1.5.0_85
JAVAHOME=/usr/jdk/jdk1.5.0_85
ORACLE_XHIBIT_DB_USER=xhibit
ORACLE_XHIBIT_DB_PASS=xhibit

PATH=/usr/bin:/usr/sbin:$ORACLE_HOME/bin:$JAVA_HOME/bin:/etc:/usr/ccs/bin:/usr/openwin/bin:/usr/local/bin:$ORACLE_HOME/lib:/usr/local/bin:/usr/lib/lwp:/opt/csw/bin:$PATH
LD_LIBRARY_PATH=/usr/lib/lwp:$ORACLE_HOME/lib
export ORACLE_HOME TNS_HOME LD_LIBRARY_PATH ORACLE_HOME ORACLE_SID ORACLE_XHIBIT_DB_USER ORACLE_XHIBIT_DB_PASS
export PATH
