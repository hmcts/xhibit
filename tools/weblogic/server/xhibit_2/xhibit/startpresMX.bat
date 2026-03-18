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
SET DEBUG_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,address=6112,server=y,suspend=n 

@rem
@rem Set Java Options
@rem

@rem JAVA JAVA_OPTIONS
SET JAVA_OPTIONS=-server
SET JAVA_OPTIONS=%JAVA_OPTIONS% %DEBUG_OPTS%
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Xms128m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Xmx256m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -XX:PermSize=64m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -XX:MaxPermSize=128m
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
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.Name=presM%SERVER_NUMBER%
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Djava.security.policy=%WL_HOME%\server\lib\weblogic.policy
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.alternateTypesDirectory=%APPLICATION_HOME%\mbeantypes
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.ext.dirs=%DOMAIN_HOME%\lib;%APPLICATION_HOME%\lib
@rem XHIBIT JAVA_OPTIONS
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Ddefault.AUTHENTICATION_URL=t3s://@computer.name@:@midM1.sslport@,@computer.name@:@midM2.sslport@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Ddefault.PROVIDER_URL=t3://@computer.name@:@midM1.port@,@computer.name@:@midM2.port@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Djavax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dresults.activateVerifyResults=Y
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dlocator.eager.load
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dlog4j.log.dir=%DOMAIN_HOME%\logs
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dpublicdisplay.web.store_base=%DOMAIN_HOME%\cache\presM%SERVER_NUMBER%

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

