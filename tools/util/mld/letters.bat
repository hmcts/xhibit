@ECHO off

@rem
@rem Title:         XHIBIT Letter Generation Script
@rem Description:   This script is used to test the generation of letters
@rem Copyright:     Copyright (c) 2005
@rem Company:       Electronic Data Systems
@rem Author:        Will Fardell
@rem Version:       1.0
@rem 

SETLOCAL

@rem 
@rem Check Parameters
@rem 

SET USAGE=usage letters.bat main-class source-xml

SET MAIN_CLASS=%1

SET XML_FILE=%2

IF NOT DEFINED XML_FILE (
    ECHO %USAGE%
    EXIT /B 1
)

IF NOT DEFINED MAIN_CLASS (
    ECHO %USAGE%
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
@rem Add Additional Classes For Letters
@rem 

IF DEFINED CLASSPATH (
    SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\support\framework\build\classes
) ELSE (
    SET CLASSPATH=%XHIBIT_HOME%\support\framework\build\classes
)

SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\support\config\build\classes
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\support\config\src
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\metadata\build\classes
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\common\listdistribution\build\classes
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\midtier\listdistribution2\build\classes

@rem 
@rem Execute Command
@rem 

ECHO PATH
ECHO;
            
ECHO CLASSPATH
ECHO;

SET LETTER_COMMAND=%JAVA_HOME%\bin\java %JVMARGS% -classpath %CLASSPATH% %MAIN_CLASS% %XML_FILE%

ECHO COMMAND: %LETTER_COMMAND%
ECHO;

%LETTER_COMMAND%
