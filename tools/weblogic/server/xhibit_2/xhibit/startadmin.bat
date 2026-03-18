@ECHO off

SETLOCAL

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
SET JAVA_OPTIONS=-client
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Xms64m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Xmx128m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -XX:PermSize=32m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -XX:MaxPermSize=128m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -XX:CompileThreshold=8000
SET JAVA_OPTIONS=%JAVA_OPTIONS% -da
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Xverify:none
@rem WERBLOGIC JAVA_OPTIONS
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dplatform.home=%WL_HOME%
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwls.home=%WL_HOME%\server
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwli.home=%WL_HOME%\integration
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.management.discover=true
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.ProductionModeEnabled=
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwlw.iterativeDev=
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwlw.testConsole=
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwlw.logErrorsToConsole=
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.Name=admin
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Djava.security.policy=%WL_HOME%\server\lib\weblogic.policy
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.alternateTypesDirectory=%APPLICATION_HOME%\mbeantypes
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.ext.dirs=%DOMAIN_HOME%\lib;%APPLICATION_HOME%\lib
@rem XHIBIT JAVA_OPTIONS
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dlog4j.log.dir=%DOMAIN_HOME%\logs

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
