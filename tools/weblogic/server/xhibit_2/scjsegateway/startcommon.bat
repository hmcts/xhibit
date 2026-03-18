@ECHO off

@rem
@rem Check Environment
@rem

IF DEFINED WL_HOME GOTO WL_HOME_DEFINED
ECHO Please set WL_HOME environment variable.
EXIT /B 4
:WL_HOME_DEFINED

IF DEFINED JAVA_HOME GOTO JAVA_HOME_DEFINED
ECHO Please set JAVA_HOME environment variable.
EXIT /B 8
:JAVA_HOME_DEFINED

@rem
@rem Set Environment (from script location)
@rem

PUSHD %~pd0% 1>NUL 2>&1
IF %ERRORLEVEL% EQU 0 GOTO PUSHD_DOMAIN_HOME_OK

ECHO An error occured finding the domain home "%~pd0%", please correct and rerun.
POPD
EXIT /B 8

:PUSHD_DOMAIN_HOME_OK
SET DOMAIN_HOME=%CD%
POPD

PUSHD %DOMAIN_HOME%\..\scjsegateway-@xhibit.version@ 1>NUL 2>&1
IF %ERRORLEVEL% EQU 0 GOTO PUSHD_APPLICATION_HOME_OK

ECHO An error occured finding the application home "%DOMAIN_HOME%\..\scjsegateway-@xhibit.version@", please correct and rerun.
POPD
EXIT /B 16

:PUSHD_APPLICATION_HOME_OK
SET APPLICATION_HOME=%CD%
POPD

PUSHD %DOMAIN_HOME%\..\common\patch 1>NUL 2>&1
IF %ERRORLEVEL% EQU 0 GOTO PUSHD_PATCH_HOME_OK

ECHO An error occured finding the patch home "%DOMAIN_HOME%\..\common\patch", please correct and rerun.
POPD
EXIT /B 32

:PUSHD_PATCH_HOME_OK
SET PATCH_HOME=%CD%
POPD

@rem
@rem Set Classpath
@rem

@rem JAVA CLASSPATH
SET CLASSPATH=%JAVA_HOME%\lib\tools.jar
@rem WEBLOGIC CLASSPATH
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\server\lib\weblogic.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\server\lib\webservices.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\common\eval\pointbase\lib\pbclient51.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\server\lib\xqrl.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\integration\lib\util.jar
@rem XHIBIT CLASSPATH

@rem
@rem Set Path
@rem

@rem JAVA PATH
PATH=%JAVA_HOME%\jre\bin
PATH=%PATH%;%JAVA_HOME%\bin
@rem WEBLOGIC PATH
PATH=%PATH%;%WL_HOME%\server\native\win\32