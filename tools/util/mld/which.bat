@ECHO off

@rem
@rem Title:         XHIBIT Format Script
@rem Description:   This script is used to test the formating of documents
@rem Copyright:     Copyright (c) 2005
@rem Company:       Electronic Data Systems
@rem Author:        Will Fardell
@rem Version:       1.0
@rem 

SETLOCAL

@rem 
@rem Check Parameters
@rem 

SET USAGE_HELP=which.bat RESOURCE

SET RESOURCE=%1

IF NOT DEFINED RESOURCE (
    ECHO %USAGE_HELP%
    EXIT /B 1
)

@rem 
@rem Configure & Check Configured Environment
@rem 
       
CALL %~pd0%check_environment.bat
IF %ERRORLEVEL% NEQ 0 (
    EXIT /B %ERRORLEVEL%
)

@rem 
@rem Configure Derived Environment
@rem 

CALL %~pd0%config_classpath.bat
IF %ERRORLEVEL% NEQ 0 (
    EXIT /B %ERRORLEVEL%
)

CALL %~pd0%config_jvmargs.bat
IF %ERRORLEVEL% NEQ 0 (
    EXIT /B %ERRORLEVEL%
)

CALL %~pd0%config_path.bat
IF %ERRORLEVEL% NEQ 0 (
    EXIT /B %ERRORLEVEL%
)

@rem 
@rem Add Additional Classes For Which
@rem 

IF DEFINED CLASSPATH (
    SET CLASSPATH=%CLASSPATH%;%~pd0%lib\which.jar
) ELSE (
    SET CLASSPATH=%~pd0%lib\which.jar
)

@rem 
@rem Execute Command
@rem 

SET PATH
ECHO;

SET CLASSPATH
ECHO;
            
SET FORMAT_COMMAND=%JAVA_HOME%\bin\java %JVMARGS% uk.co.xdevelopment.which.Main %RESOURCE%

SET FORMAT_COMMAND
ECHO;

%FORMAT_COMMAND%