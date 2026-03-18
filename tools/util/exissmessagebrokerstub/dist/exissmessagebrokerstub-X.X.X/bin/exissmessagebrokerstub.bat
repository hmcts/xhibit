@ECHO OFF

SETLOCAL

@rem
@rem Set classpath to the correct values.
@rem

SET XHIBIT_CLASSPATH=..\lib\exissmessagebrokerstub.jar;..\..\3rdparty-9.1.0\lib\log4j-1.2.13.jar;..\..\3rdparty-9.1.0\lib\weblogic.jar

@rem
@rem Set Xhibit VM Args To the correct values.
@rem

SET JAVA_OPTIONS=

@rem
@rem Execute ExISS Message Broker Stub
@rem

@ECHO ON

..\..\jre-1.5.0_04\bin\java.exe %JAVA_OPTIONS% -classpath %XHIBIT_CLASSPATH% exissmessagebrokerstub.Main -o ../outbox