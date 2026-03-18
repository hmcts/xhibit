@ECHO OFF

SETLOCAL

PUSHD %~pd0%..
SET NOTIFY_HOME=%CD%
POPD

IF NOT DEFINED JAVA_HOME (
    ECHO JAVA_HOME has not been defined please set and retry.
    GOTO :eof
)
SET JAVA_CMD=%JAVA_HOME%/bin/java

echo Court ID: %1 Date: %2 IP Address: %3 Port: %4

@rem
@rem System Properties
@rem

set WL_URL=t3://%3:%4
set JMS_CONNECTION_FACTORY=CSJMSConnectionFactory
set JMS_TOPIC_NAME=PublicDisplay

echo Court ID: %1 Date: %2 IP Address: %3 Port: %4 Midtier URL: %WL_URL% JMS Connection Factor: %JMS_CONNECTION_FACTORY% JMS Topic Name: %JMS_TOPIC_NAME%

@rem
@rem Set classpath to the correct values.
@rem

SET NOTIFY_CLASSPATH=%NOTIFY_HOME%\lib\wlclient.jar;%NOTIFY_HOME%\lib\wljmsclient.jar;%NOTIFY_HOME%\lib\DailyListNotifier.jar

@rem
@rem Set classpath to the correct values.
@rem

SET CLASSPATH=%NOTIFY_HOME%/lib/wlclient.jar
SET CLASSPATH=%CLASSPATH%;%NOTIFY_HOME%/lib/wljmsclient.jar
SET CLASSPATH=%CLASSPATH%;%NOTIFY_HOME%/lib/DailyListNotifier.jar

@rem
@rem Set JAVA_OPTION
@rem

SET JAVA_OPTS=-Xms32m -Xmx64m

@rem
@rem Execute NOTIFY
@rem

echo CLASSPATH: %CLASSPATH%
echo JAVA_OPTS: %JAVA_OPTS%

@ECHO ON

%JAVA_CMD% %JAVA_OPTS% -classpath %CLASSPATH% uk.gov.courtservice.xhibit.integration.publicdisplay.jms.DailyListNotifier %WL_URL% %JMS_CONNECTION_FACTORY% %JMS_TOPIC_NAME% %1 %2
