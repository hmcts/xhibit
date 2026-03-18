@ECHO off

SETLOCAL

@rem
@rem Check Parameters
@rem

SET SERVER_NUMBER=%1

IF DEFINED SERVER_NUMBER GOTO SERVER_NUMBER_DEFINED
ECHO please pass SERVER_NUMBER parameter.
EXIT /B 1
:SERVER_NUMBER_DEFINED

@rem
@rem Check/Set Environment
@rem

CALL %~pd0%startcommon.bat
IF %ERRORLEVEL% EQU 0 GOTO START_COMMON_OK
EXIT /B 1
:START_COMMON_OK

@rem
@rem Set Java Options
@rem

@rem JAVA JAVA_OPTIONS

SET DEBUG_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,address=6111,server=y,suspend=n 

@rem
@rem Set Java Options
@rem

@rem JAVA JAVA_OPTIONS
SET JAVA_OPTIONS=-server
SET JAVA_OPTIONS=%JAVA_OPTIONS% %DEBUG_OPTS%


SET JAVA_OPTIONS=%JAVA_OPTIONS% -Xms256m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Xmx512m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -XX:PermSize=128m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -XX:MaxPermSize=256m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -da
@rem WERBLOGIC JAVA_OPTIONS
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dplatform.home=%WL_HOME%
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwls.home=%WL_HOME%\server
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwli.home=%WL_HOME%\integration
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.management.discover=false
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.management.server=http://@computer.name@:@admin.port@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwlw.iterativeDev=false
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwlw.testConsole=false
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwlw.logErrorsToConsole=true
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.management.username=@admin.username@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.management.password=@admin.password@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.Name=midM%SERVER_NUMBER%
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Djava.security.policy=%WL_HOME%\server\lib\weblogic.policy
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.alternateTypesDirectory=%APPLICATION_HOME%\mbeantypes
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.ext.dirs=%DOMAIN_HOME%\lib;%APPLICATION_HOME%\lib
@rem XHIBIT JAVA_OPTIONS
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Ddefault.AUTHENTICATION_URL=t3s://@computer.name@:@midM1.sslport@,@computer.name@:@midM2.sslport@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Ddefault.PROVIDER_URL=t3://@computer.name@:@midM1.port@,@computer.name@:@midM2.port@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Djavax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DdisableMercatorUse=false
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dresults.activateVerifyResults=Y
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dlocator.eager.load
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dlog4j.log.dir=%DOMAIN_HOME%\logs
@rem XHIBIT MERCATOR INTEGRATION JAVA_OPTIONS (to use these settings disableMercatorUse must be false)
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DMERCATOR_WRAPPER_TYPE=HTTP
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DMERCATOR_AGENT_NAME=appcomm
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DMERCATOR_AGENT_IPADDRESS=10.63.127.28
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DMERCATOR_AGENT_PORT=8081
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DMERCATOR_CONNECTION_TIMEOUT=60000
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DIS_MERCATOR_XML_FILE_LOGGING=true
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DIS_MERCATOR_VOLUME_STATS=true
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DIS_MERCATOR_PERF_STATS=true
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DIS_MERCATOR_XML_TO_LOG=true
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DIS_MERCATOR_BUSINESS_ERRORS_FILE_LOGGING=true
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DIS_MERCATOR_EXCEPTION_TESTING=false
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DMERCATOR_EXCEPTION_TYPE=IO
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DMERCATOR_EXCEPTION_RETURN_CODE=37

SET JAVA_OPTIONS=%JAVA_OPTIONS% -DdisableSchedulerServlet=@disable.scheduler@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dscheduler.scheduledtasks=@scheduler.scheduledtasks@

@rem The Driving License Status, No TICS and Collect Magistrate Court ID can be updated individually on Defendant On Case. If this is false the Java layer does it, otherwise Mercator
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DIS_MERCATOR_DOC_UPDATE=false

@rem
@rem Set Classpath
@rem

@rem JAVA CLASSPATH
SET CLASSPATH=%JAVA_HOME%\lib\tools.jar
@rem WEBLOGIC CLASSPATH
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\server\lib\weblogic.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\server\lib\webservices.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\common\eval\pointbase\lib\pbclient51.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\server\lib\xqrl.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\integration\lib\util.jar
@rem XHIBIT CLASSPATH


@rem
@rem Set Path
@rem

@rem JAVA PATH
PATH=%JAVA_HOME%\jre\bin
PATH=%PATH%;%JAVA_HOME%\bin
@rem WEBLOGIC PATH
PATH=%PATH%;%WL_HOME%\server\native\win\32

@rem
@rem Set Command
@rem

SET COMMAND=%JAVA_HOME%\bin\java %JAVA_OPTIONS% weblogic.Server

@rem
@rem Execute Weblogic
@rem

ECHO;
SET PATH

ECHO;
SET CLASSPATH

ECHO;
SET COMMAND

@rem ECHO;
%COMMAND%

