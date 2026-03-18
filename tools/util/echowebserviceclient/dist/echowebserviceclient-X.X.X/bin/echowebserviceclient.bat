SETLOCAL

@ECHO off

@rem
@rem The following script sets the environment variables for use
@rem when running the Echo Web Service Client for the XHIBIT project. 
@rem This script should be run before any tasks are run to execute the Echo Web Service Client. 
@rem It requires Weblogic 9.2, Java JDK 1.5.04 the echowebserviceclient.jar and
@rem the properties file EchoWebServiceClientProperties.txt
@rem This script attempts to locate the Weblogic 9.2 specified in the C:\bea\beahomelist
@rem and will use the JDK deployed as part of weblogic (..\bea\jdk150_04) to set the Java
@rem A single parameter representing the password can be passed in as a comand line argument
@rem which represents the Proxy Password of the Proxy between XHIBIT and CJSE
@rem This script sets the following environment variables: 
@rem BEA_HOME, JAVA_HOME, WL_HOME, and CLASSPATH. 
@rem It modifies the PATH envrionment variable to include the java and weblogic bin.
@rem Finally it runs the echowebserviceclient.Main class, passing the location of the
@rem EchoWebServiceClientProperties.txt and the optional command line parameter representing the
@rem Proxy Password
@rem On a successful execution the echo message request will be logged to the outbox
@rem and the echo message response from the remote Echo Web Service will be logged to the inbox
@rem All errors will be logged to the errorbox and debug logging to the echoDebug.log
@rem

IF DEFINED BEA_HOME GOTO BEA_HOME_DEFINED

@rem 
@rem Check the contents of C:\bea\beahomelist and determine weblogic 9's home.
@rem    - Note checks a max of 5 entries per line!
@rem 

IF EXIST "C:\bea\beahomelist" (
    FOR /F "tokens=1-5 delims=;" %%i IN (C:\bea\beahomelist) DO (    
        IF EXIST %%i\weblogic92\server\lib\weblogic.jar (  
            SET BEA_HOME=%%i
            GOTO BEA_HOME_DEFINED
        )
        IF EXIST %%j\weblogic92\server\lib\weblogic.jar (
            SET BEA_HOME=%%j
            GOTO BEA_HOME_DEFINED
        )
        IF EXIST %%k\weblogic92\server\lib\weblogic.jar (
            SET BEA_HOME=%%k
            GOTO BEA_HOME_DEFINED
        )
        IF EXIST %%l\weblogic92\server\lib\weblogic.jar (
            SET BEA_HOME=%%l
            GOTO BEA_HOME_DEFINED
        )
        IF EXIST %%m\weblogic92\server\lib\weblogic.jar (
            SET BEA_HOME=%%m
            GOTO BEA_HOME_DEFINED
        )
    )
)
    
ECHO Could not determine BEA_HOME, please set BEA_HOME environment variable and rerun.
EXIT /B 1

:BEA_HOME_DEFINED

@rem
@rem Check JAVA is correct 
@rem Note Java Vendor is required by weblogic's commEnv.cmd to stop JAVA_HOME
@rem being reset.
@rem

%BEA_HOME%\jdk150_04\bin\java.exe -version 1>NUL 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO An error occured executing java under BEA_HOME "%BEA_HOME%", please correct and rerun.    
    EXIT /B 2
)

ECHO;
SET JAVA_HOME=%BEA_HOME%\jdk150_04
SET JAVA_HOME
SET JAVA_VENDOR=Sun
SET JAVA_VENDOR
%JAVA_HOME%\bin\java.exe -version

@rem
@rem Check Weblogic is correct 
@rem

%BEA_HOME%\jdk150_04\bin\java.exe -classpath %BEA_HOME%\weblogic92\server\lib\weblogic.jar weblogic.version 1>NUL 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO An error occured executing weblogic under BEA_HOME "%BEA_HOME%", please correct and rerun.    
    EXIT /B 4
)

ECHO;
SET WL_HOME=%BEA_HOME%\weblogic92
SET WL_HOME
FOR /F "tokens=*" %%i IN ('%JAVA_HOME%\bin\java.exe -classpath %WL_HOME%\server\lib\weblogic.jar weblogic.version') DO (
    ECHO %%i
    GOTO WL_ECHO_END
)
:WL_ECHO_END

@rem
@rem Set the additional environment variables
@rem

SET PATH=%JAVA_HOME%\bin;%WL_HOME%\server\bin;%PATH%
ECHO;
SET PATH

SET CLASSPATH=
ECHO;
SET CLASSPATH


SET CLASSPATH=..\..\echowebserviceclient.jar;%JAVA_HOME%\jre\lib\rt.jar;%WL_HOME%\server\lib\weblogic.jar

ECHO;
ECHO XHIBIT Environment Set.
ECHO;

@ECHO OFF

@rem
@rem Execute ExISS Message Broker Stub
@rem Note - if %1 is not populated (it represents the proxy password) this is OK but make sure it's the last parameter!
@rem

@ECHO ON
%JAVA_HOME%\bin\java.exe %JAVA_OPTIONS% -classpath %CLASSPATH% echowebserviceclient.Main -p ../properties/EchoWebServiceClientProperties.txt -x %1 >echoDebug.log
