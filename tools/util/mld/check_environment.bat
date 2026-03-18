@ECHO off

@rem
@rem Title:         XHIBIT Environment Check Script
@rem Description:   This script is used to check the environment.
@rem Copyright:     Copyright (c) 2005
@rem Company:       Electronic Data Systems
@rem Author:        Will Fardell
@rem Version:       0.1
@rem 

@rem 
@rem Check Environment. 
@rem 

ECHO;
@rem ECHO ---------------------------------------------------------------------------- 

@rem Check JAVA_HOME 

ECHO;
 
IF NOT DEFINED JAVA_HOME (    
    ECHO JAVA_HOME: Undefined
    ECHO Could not find java. Check Failed.
    EXIT /B 1
)

SET JAVA_VERSION=%JAVA_HOME%\bin\java.exe -version 

%JAVA_VERSION% 1> NUL 2> NUL
IF %ERRORLEVEL% NEQ 0 (
    ECHO JAVA_HOME: %JAVA_HOME%
    ECHO Could not find java. Check Failed.
    EXIT /B 1
)

ECHO JAVA_HOME: %JAVA_HOME%
ECHO;
%JAVA_VERSION%
ECHO;
@rem ECHO ----------------------------------------------------------------------------

@rem Check ANT_HOME

ECHO;

IF NOT DEFINED ANT_HOME (    
    ECHO ANT_HOME: Undefined
    ECHO Could not find ant. Check Failed.
    EXIT /B 1
)

SET ANT_VERSION=CALL %ANT_HOME%\bin\ant.bat -version

%ANT_VERSION% 1> NUL 2> NUL
IF %ERRORLEVEL% NEQ 0 (
    ECHO ANT_HOME: %ANT_HOME%
    ECHO Could not find ant. Check Failed.
    EXIT /B 1
)

ECHO ANT_HOME: %ANT_HOME%
ECHO;
%ANT_VERSION%
ECHO;
@rem ECHO ----------------------------------------------------------------------------

@rem Check WL_HOME

ECHO;

IF NOT DEFINED WL_HOME (    
    ECHO WL_HOME: Undefined
    ECHO Could not find weblogic. Check Failed.
    EXIT /B 1
)

SET WEBLOGIC_VERSION=%JAVA_HOME%\bin\java.exe -cp %WL_HOME%\server\lib\weblogic.jar weblogic.version 

%WEBLOGIC_VERSION% 1> NUL 2> NUL
IF %ERRORLEVEL% NEQ 0 (
    ECHO WL_HOME: %WL_HOME%
    ECHO Could not find weblogic. Check Failed.
    EXIT /B 1
)

ECHO WL_HOME: %WL_HOME%   

%WEBLOGIC_VERSION%
ECHO;
@rem ECHO ----------------------------------------------------------------------------

@rem Check XHIBIT_HOME

ECHO;

IF NOT DEFINED XHIBIT_HOME (    
    ECHO XHIBIT_HOME: Undefined
    ECHO Could not find xhibit. Check Failed.
    EXIT /B 1
)

IF NOT EXIST %XHIBIT_HOME% (
    ECHO XHIBIT_HOME: %XHIBIT_HOME%
    ECHO Could not find xhibit. Check Failed.
    EXIT /B 1
)


ECHO XHIBIT_HOME: %XHIBIT_HOME%   
ECHO;
@rem ECHO ----------------------------------------------------------------------------
