@rem
@rem Script for generating client and server keystores
@rem
@rem Author: Will Fardell

@ECHO OFF

SETLOCAL

@rem 
@rem Check Environment
@rem 

IF DEFINED JAVA_HOME GOTO JAVA_HOME_DEFINED
ECHO Please set JAVA_HOME environment variable.
EXIT /B 1
:JAVA_HOME_DEFINED

@rem 
@rem Set Parameters
@rem 

SET SCRIPT_DIR=%~pd0%
SET SCRIPT_DIR=%SCRIPT_DIR:~0,-1%

SET BUILD_DIR=%SCRIPT_DIR%\build
IF NOT EXIST %BUILD_DIR% mkdir %BUILD_DIR%

SET GEN_DIR=%BUILD_DIR%\xhbseccon
IF NOT EXIST %GEN_DIR% mkdir %GEN_DIR%

SET SRC_FILE=%SCRIPT_DIR%\xhibitkeystore

SET SERVER_FILE=%GEN_DIR%\xhibit.server.jks
SET SERVER_PASSWORD=xh1b1tth1n

SET KEY_ALIAS=xhibitthin
SET KEY_PASSWORD=xh1b1tth1n

SET CERT_FILE=%GEN_DIR%\xhibit.cer

SET CLIENT_FILE=%GEN_DIR%\xhibit.client.jks
SET CLIENT_PASSWORD=xh1b1tth1n

@rem 
@rem Check Parameters
@rem 

IF EXIST %SRC_FILE% GOTO SRC_FILE_EXIST
ECHO Src keystore %SRC_FILE% does not exist
EXIT /B 2
:SRC_FILE_EXIST

@rem 
@rem Clean Server Key Store
@rem 

IF EXIST %SERVER_FILE% DEL %SERVER_FILE%

COPY %SRC_FILE% %SERVER_FILE% 

%JAVA_HOME%\bin\keytool.exe -delete -keystore %SERVER_FILE% -storepass %SERVER_PASSWORD% -alias mykey 

%JAVA_HOME%\bin\keytool.exe -delete -keystore %SERVER_FILE% -storepass %SERVER_PASSWORD% -alias xhibitthinca

%JAVA_HOME%\bin\keytool.exe -delete -keystore %SERVER_FILE% -storepass %SERVER_PASSWORD% -alias xhibitthinint

%JAVA_HOME%\bin\keytool.exe -keyclone -keystore %SERVER_FILE% -storepass %SERVER_PASSWORD% -alias xhibitkey -keypass xh1b1tth1n -dest %KEY_ALIAS% -new %KEY_PASSWORD%

%JAVA_HOME%\bin\keytool.exe -delete -keystore %SERVER_FILE% -storepass %SERVER_PASSWORD% -alias xhibitkey

ECHO Server Key Store "%SERVER_FILE%":
%JAVA_HOME%\bin\keytool.exe -list -keystore %SERVER_FILE% -storepass %SERVER_PASSWORD% 
ECHO;

@rem 
@rem Export Certificate
@rem 

IF EXIST %CERT_FILE% DEL %CERT_FILE%

%JAVA_HOME%\bin\keytool.exe -export -keystore %SERVER_FILE% -storepass %SERVER_PASSWORD% -alias %KEY_ALIAS% -file %CERT_FILE% 1>NUL 2>&1 

ECHO Certificate File "%CERT_FILE%":
ECHO;
%JAVA_HOME%\bin\keytool.exe -printcert -file %CERT_FILE%
ECHO;

@rem 
@rem Generate Client Key Store
@rem 

IF EXIST %CLIENT_FILE% DEL %CLIENT_FILE%

%JAVA_HOME%\bin\keytool.exe -import -keystore %CLIENT_FILE% -storepass %CLIENT_PASSWORD% -alias %KEY_ALIAS% -keypass %KEY_PASSWORD% -file %CERT_FILE% -noprompt 1>NUL 2>&1 

@rem Print contents of keystore
ECHO Client Key Store "%CLIENT_FILE%":
%JAVA_HOME%\bin\keytool.exe -list -keystore %CLIENT_FILE% -storepass %CLIENT_PASSWORD% 
ECHO;
