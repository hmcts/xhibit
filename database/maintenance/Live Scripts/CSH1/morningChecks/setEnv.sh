# setup Oracle
ORACLE_HOME=/opt/moj/oracle/10.2.0
TNS_ADMIN=$HOME

ORACLE_SIDCJSE=csdbprd2
ORACLE_HOME=/opt/moj/oracle/10.2.0
ORACLE_SID=csdbprd1
ORACLE_SID2=csdbprd2
ORACLE_SID3=csdbprd3
ORACLE_BASE=/opt/moj/oracle/10.2.0
JAVAHOME=/usr/jdk/jdk1.5.0_17
JAVA_HOME=/usr/jdk/jdk1.5.0_17
SMTP_SERVER="SMTPRelayN.dom1.infra.int"

PATH=/usr/bin:/usr/sbin:/usr/sbin:$ORACLE_HOME/bin:$JAVA_HOME/bin:/etc:/usr/ccs/bin:/usr/openwin/bin:/usr/local/bin:$ORACLE_HOME/lib:/src:/usr/local/bin:/opt/EDSWssh/bin:/usr/lib/lwp:/opt/csw/bin:$PATH
LD_LIBRARY_PATH=/usr/lib/lwp:$ORACLE_HOME/lib

export ORACLE_BASE ORACLE_SID ORACLE_SID2 ORACLE_SID3 ORACLE_SIDCJSE
export ORACLE_HOME TNS_ADMIN LD_LIBRARY_PATH
export JAVA_HOME JAVAHOME
export PATH
