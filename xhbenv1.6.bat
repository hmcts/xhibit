@ECHO off

@rem
@rem The following script sets the environment variables for use
@rem when developing the XHIBIT project. This script should
@rem be run before any tasks are run. This script sets the following
@rem environment variables BEA_HOME, JAVA_HOME, WL_HOME, ANT_HOME, 
@rem ANT_OPTS and CLASSPATH. It modifies the PATH envrionment 
@rem variable to include the java and weblogic bin.
@rem

IF DEFINED BEA_HOME GOTO BEA_HOME_DEFINED

@rem 
@rem Check the contents of C:\bea\beahomelist and determine weblogic 10's home.
@rem    - Note checks a max of 5 entries per line!
@rem 

IF EXIST "C:\bea\beahomelist" (
    FOR /F "tokens=1-5 delims=;" %%i IN (C:\bea\beahomelist) DO (    
        IF EXIST %%i\wlserver_10.3\server\lib\weblogic.jar (  
            SET BEA_HOME=%%i
            GOTO BEA_HOME_DEFINED
        )
        IF EXIST %%j\wlserver_10.3\server\lib\weblogic.jar (
            SET BEA_HOME=%%j
            GOTO BEA_HOME_DEFINED
        )
        IF EXIST %%k\wlserver_10.3\server\lib\weblogic.jar (
            SET BEA_HOME=%%k
            GOTO BEA_HOME_DEFINED
        )
        IF EXIST %%l\wlserver_10.3\server\lib\weblogic.jar (
            SET BEA_HOME=%%l
            GOTO BEA_HOME_DEFINED
        )
        IF EXIST %%m\wlserver_10.3\server\lib\weblogic.jar (
            SET BEA_HOME=%%m
            GOTO BEA_HOME_DEFINED
        )
    )
)
    
ECHO Could not determine BEA_HOME, please set BEA_HOME environment variable and rerun.
EXIT /B 1

:BEA_HOME_DEFINED

@rem
@rem Set XHIBIT_HOME to script dir (remove trailing slash)
@rem

IF DEFINED XHIBIT_HOME GOTO XHIBIT_HOME_SET

SET XHIBIT_HOME=%~pd0%
SET XHIBIT_HOME=%XHIBIT_HOME:~0,-1%

:XHIBIT_HOME_SET

ECHO;
SET XHIBIT_HOME

@rem
@rem Check JAVA is correct 
@rem Note Java Vendor is required by weblogic's commEnv.cmd to stop JAVA_HOME
@rem being reset.
@rem

%BEA_HOME%\jdk1.6.0_45\bin\java.exe -version 1>NUL 2>&1

IF %ERRORLEVEL% NEQ 0 (
    ECHO An error occured executing java under BEA_HOME "%BEA_HOME%", please correct and rerun.    
    EXIT /B 2
)

ECHO;
SET JAVA_HOME=%BEA_HOME%\jdk1.6.0_45
SET JAVA_HOME
SET JAVA_VENDOR=Sun
SET JAVA_VENDOR
%JAVA_HOME%\bin\java.exe -version

@rem
@rem Check Weblogic is correct 
@rem

%BEA_HOME%\jdk1.6.0_45\bin\java.exe -classpath %BEA_HOME%\wlserver_10.3\server\lib\weblogic.jar weblogic.version 1>NUL 2>&1

IF %ERRORLEVEL% NEQ 0 (
    ECHO An error occured executing weblogic under BEA_HOME "%BEA_HOME%", please correct and rerun.    
    EXIT /B 4
)


ECHO;
SET WL_HOME=%BEA_HOME%\wlserver_10.3
SET WL_HOME
FOR /F "tokens=*" %%i IN ('"%JAVA_HOME%\bin\java.exe" -classpath %WL_HOME%\server\lib\weblogic.jar weblogic.version') DO (
    ECHO %%i
    GOTO WL_ECHO_END
)
:WL_ECHO_END

@rem
@rem Check Ant is correct 
@rem

SET ANT_HOME=%BEA_HOME%\modules\org.apache.ant_1.7.1
CALL %BEA_HOME%\modules\org.apache.ant_1.7.1\bin\ant.bat -version 1>NUL 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO An error occured executing ant under BEA_HOME "%BEA_HOME%", please correct and rerun.    
    EXIT /B 8
)

ECHO;
SET ANT_HOME=%BEA_HOME%\modules\org.apache.ant_1.7.1
SET ANT_HOME
CALL %ANT_HOME%\bin\ant.bat -version

@rem
@rem Set the additional environment variables
@rem

SET PATH=%JAVA_HOME%\bin;%WL_HOME%\server\bin;%ANT_HOME%\bin;%PATH%
ECHO;
SET PATH

SET CLASSPATH=%WL_HOME%\server\lib\wlfullclient.jar
ECHO;

SET ANT_OPTS=-Xms448m -Xmx448m -XX:PermSize=448m -XX:MaxPermSize=1024m
@rem SET ANT_OPTS=
ECHO;
@rem SET ANT_OPTS

ECHO;
ECHO XHIBIT Environment Set.
ECHO;