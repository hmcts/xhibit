SETLOCAL

@ECHO off

@rem
@rem The following script sets the environment variables for use
@rem when running the JMS Util Service the XHIBIT project. This script should
@rem be run before any tasks are run. This script sets the following
@rem environment JAVA_HOME and CLASSPATH. 
@rem It modifies the PATH envrionment variable to include the java bin.
@rem Finally it runs the jmsutil.Main class
@rem

ECHO;

@rem
@rem Set this variable
@rem
SET JAVA_HOME=C:\bea\jdk150_04


SET JAVA_VENDOR=Sun
%JAVA_HOME%\bin\java.exe -version

@rem
@rem Set the additional environment variables
@rem

SET PATH=%JAVA_HOME%\bin;%PATH%
ECHO;
SET PATH

@ECHO;
SET CLASSPATH=

SET CLASSPATH=%JAVA_HOME%\lib\rt.jar;..\lib\log4j-1.2.13.jar;..\lib\commons-logging-1.0.3.jar;..\lib\castor-1.0.jar;..\lib\xalan-2.7.0.jar;..\lib\xalan-2.7.0-xercesImpl.jar;..\lib\xalan-2.7.0-xml-apis.jar;..\lib\xalan.serializer-2.7.0.jar;..\ojdbc14.jar;..\lib\Config.jar;..\xhibit.ant.custom-1.0.jar;..\lib\Framework.jar;..\lib\XhibitXmlBinding.jar;..\lib\datamigration.jar
@ECHO OFF

@rem
@rem Execute Data Migration
@rem

@ECHO ON

@rem %1 is the database password, which must be passed in as the first arg in the command line
%JAVA_HOME%\bin\java.exe %JAVA_OPTIONS% -classpath %CLASSPATH% datamigration1745.DataMigration -p%1 %2 %3 %4 %5 %6 >dataMigrationDebug.log


