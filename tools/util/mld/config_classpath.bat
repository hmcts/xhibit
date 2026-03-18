@ECHO off

@rem
@rem Title:         XHIBIT Config Classpath
@rem Description:   This script is used to configure the classpath
@rem Copyright:     Copyright (c) 2005
@rem Company:       Electronic Data Systems
@rem Author:        Will Fardell
@rem Version:       0.1
@rem 

@rem JAVA CLASSPATH
SET CLASSPATH=%JAVA_HOME%\lib\tools.jar
@rem WEBLOGIC CLASSPATH
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\tools\weblogic\server\xhibit_2\common\patch\CR265181_910.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\server\lib\weblogic.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\server\lib\webservices.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\common\eval\pointbase\lib\pbclient51.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\server\lib\xqrl.jar
SET CLASSPATH=%CLASSPATH%;%WL_HOME%\integration\lib\util.jar
@rem XHIBIT LIB CLASSPATH
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\bsh-2.0b4.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\castor-1.0.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\commons-logging-1.0.3.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\fop-0.20.5-avalon-framework.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\fop-0.20.5-batik.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\fop-0.20.5.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\jdom-1.0.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\log4j-1.2.13.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\standard-1.0.6-jaxen-full.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\standard-1.0.6-jdbc2_0-stdext.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\standard-1.0.6-jstl.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\standard-1.0.6-saxpath.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\standard-1.0.6-weblogic.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\xalan-2.7.0-xercesImpl.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\xalan-2.7.0-xml-apis.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\xalan-2.7.0.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\xalan.serializer-2.7.0.jar
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\lib\environment\build\classes
@rem TEST CLASSPATH
SET CLASSPATH=%CLASSPATH%;%XHIBIT_HOME%\tools\util\mld\properties

@rem SET CLASSPATH
@rem ECHO;

