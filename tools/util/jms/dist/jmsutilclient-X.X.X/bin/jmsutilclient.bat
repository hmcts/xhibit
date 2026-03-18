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
IF EXIST "C:\Progra~1\Xhibit_8.0.4\jre-1.5.0_04" (
    SET JAVA_HOME=C:\Progra~1\Xhibit_8.0.4\jre-1.5.0_04
    GOTO JAVA_HOME_DEFINED
)
IF EXIST "D:\bea\jdk150_04" (
    SET JAVA_HOME=D:\bea\jdk150_04
    GOTO JAVA_HOME_DEFINED
)

ECHO Could not determine JAVA_HOME, please set JAVA_HOME environment variable as per your local environment and rerun.
EXIT /B 1

:JAVA_HOME_DEFINED

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

SET CLASSPATH=%JAVA_HOME%\lib\rt.jar;..\..\jmsutil.jar;..\..\weblogic.jar

@ECHO OFF

@rem
@rem Execute JMS Util
@rem

@ECHO ON

@rem Parameterised URL for simple inbound test which gets the inbound messages from the default outbox (dist\jmsutilclient-X.X.X\outbox)
@rem q for first destination weblogic instance (IP Address:Port), z for second (optional). Must have the JMS Queue specified in -d deployed on them
@rem d is the JMS Queue Name (scjsegateway/jms/InboundTestQueue) to which the JMS messages are sent. 
@rem m relies on the default outbox
@rem s indicates a JMS send will occur
@rem %JAVA_HOME%\bin\java.exe %JAVA_OPTIONS% -classpath %CLASSPATH% jmsutil.Main -q %1 -z %2 -d %3 -m -s

@rem Parameterised URL for inbound test where outbound messages in -b are automatically converted to normal inbound messages or inbound exception messages depending on -g: 
@rem q for first destination weblogic instance (IP Address:Port), z for second (optional). Must have the JMS Queue specified in -d deployed on them
@rem d is the JMS Queue Name (scjsegateway/jms/InboundTestQueue) to which the JMS messages are sent. 
@rem a is the inbuilt delay in ms between sending JMS messages to -d. Optional - defaults to 300ms
@rem i is the startID when converting messages (this will become the INBOUND_MESSAGE_ID on the GDG_INBOUND_MESSAGES table on the GDGATE database)
@rem b is the conversion directory and will ensure conversion is attempted if any relevant files are found
@rem j is the examples directory (optional - defaults dist\jmsutilclient-X.X.X\examples, add -j %7 to specify somewhere different)
@rem g is a boolean (optional: if present then conversion creates normal Inbound Messages else Exception inbound messages are generated)
@rem y is a boolean (optional: if present then conversion creates exception Inbound Messages with no RelatesTo field. Ignored if -g is true)
@rem m relies on the default outbox
@rem s indicates a JMS send will occur

%JAVA_HOME%\bin\java.exe %JAVA_OPTIONS% -classpath %CLASSPATH% jmsutil.Main -q %1 -z %2 -d %3 -a %4 -i %5 -b %6 -m -s >inboundAppDebug.log


