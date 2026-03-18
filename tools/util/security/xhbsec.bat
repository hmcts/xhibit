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

SET GEN_DIR=%BUILD_DIR%\xhbsec
IF NOT EXIST %GEN_DIR% mkdir %GEN_DIR%

SET SERVER_FILE=%GEN_DIR%\xhibit.development.server.jks
SET SERVER_PASSWORD=password

SET KEY_ALIAS=xhibitthin
SET KEY_PASSWORD=password
SET KEY_DNAME="CN=xhibit, OU=courtservice, OU=development, O=EDS, C=UK"
SET KEY_VALIDITY=999

SET CERT_FILE=%GEN_DIR%\xhibit.development.cer

SET CLIENT_FILE=%GEN_DIR%\xhibit.development.client.jks
SET CLIENT_PASSWORD=password

@rem 
@rem Generate Server Key Store
@rem 

IF EXIST %SERVER_FILE% DEL %SERVER_FILE%

%JAVA_HOME%\bin\keytool.exe -genkey -keystore %SERVER_FILE% -storepass %SERVER_PASSWORD% -alias %KEY_ALIAS% -keypass %KEY_PASSWORD% -dname %KEY_DNAME% -keyalg RSA

@rem Extend the duration of the key
%JAVA_HOME%\bin\keytool.exe -selfcert -keystore %SERVER_FILE% -storepass %SERVER_PASSWORD% -alias %KEY_ALIAS% -keypass %KEY_PASSWORD% -dname %KEY_DNAME% -keyalg RSA -validity %KEY_VALIDITY% 

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

%JAVA_HOME%\bin\keytool.exe -import -keystore %CLIENT_FILE% -storepass %CLIENT_PASSWORD% -alias %KEY_ALIAS% -file %CERT_FILE% -noprompt 1>NUL 2>&1 

@rem Print contents of keystore
ECHO Client Key Store "%CLIENT_FILE%":
%JAVA_HOME%\bin\keytool.exe -list -keystore %CLIENT_FILE% -storepass %CLIENT_PASSWORD% 
ECHO;
