@ECHO OFF
SETLOCAL

SET THIS_JAVA_HOME=..
IF "%JAVA_HOME%" == "" GOTO JAVAHOMEERROR
IF NOT "%JAVA_HOME%"=="" SET THIS_JAVA_HOME=%JAVA_HOME%
%THIS_JAVA_HOME%\bin\java uk.gov.courtservice.xhibit.training.TrainingDialog
IF ERRORLEVEL 1 GOTO CMDERROR
GOTO FINISH

:JAVAHOMEERROR
ECHO Need to set the JAVA_HOME environment variable.
ECHO Contact support for further assistance.
PAUSE

:CMDERROR
ECHO Missing file or directory.
ECHO Contact support for further assistance.
PAUSE

:FINISH
ENDLOCAL