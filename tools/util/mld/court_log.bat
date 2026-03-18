@ECHO off

@rem
@rem Title:         XHIBIT Format Court Log Event Script
@rem Description:   This script is used to test the formating of court log events
@rem Copyright:     Copyright (c) 2005
@rem Company:       Electronic Data Systems
@rem Author:        Will Fardell
@rem Version:       1.0
@rem 

SETLOCAL

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
@rem Add Additional Classes For Formatting
@rem 

IF DEFINED CLASSPATH (
    SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\common\court_log\build\classes
) ELSE (
    SET CLASSPATH=%XHIBIT_HOME%\common\court_log\build\classes
)
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\common\court_log\build\testclasses
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\support\config\build\classes
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\support\config\src
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\support\framework\build\classes

@rem 
@rem Execute Command
@rem 

SET PATH
ECHO;

SET CLASSPATH
ECHO;
            
SET CL_COMMAND=%JAVA_HOME%\bin\java %JVMARGS% uk.gov.courtservice.xhibit.courtlog.helpers.xsl.RunCourtLogXslHelper %*

SET CL_COMMAND
ECHO;

%CL_COMMAND%

