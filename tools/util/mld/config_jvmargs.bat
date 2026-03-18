@echo off

@rem
@rem Title:         XHIBIT Environment Config Script
@rem Description:   This script is used to configure the JVMARGS environment.
@rem Copyright:     Copyright (c) 2005
@rem Company:       Electronic Data Systems
@rem Author:        Will Fardell
@rem Version:       1.0
@rem 

@rem 
@rem Set JVMARGS Environment Variables
@rem 

@rem JAVA JVMARGS
SET JVMARGS=-server
SET JVMARGS=%JVMARGS% -Xms256m
SET JVMARGS=%JVMARGS% -Xmx512m
SET JVMARGS=%JVMARGS% -XX:PermSize=128m
SET JVMARGS=%JVMARGS% -XX:MaxPermSize=256m
SET JVMARGS=%JVMARGS% -da
@rem WERBLOGIC JVMARGS
SET JVMARGS=%JVMARGS% -Dplatform.home=%WL_HOME%
SET JVMARGS=%JVMARGS% -Dwls.home=%WL_HOME%\server
SET JVMARGS=%JVMARGS% -Dwli.home=%WL_HOME%\integration
SET JVMARGS=%JVMARGS% -Dweblogic.management.discover=false
SET JVMARGS=%JVMARGS% -Dweblogic.management.server=http://%COMPUTERNAME%:7001
SET JVMARGS=%JVMARGS% -Dwlw.iterativeDev=false
SET JVMARGS=%JVMARGS% -Dwlw.testConsole=false
SET JVMARGS=%JVMARGS% -Dwlw.logErrorsToConsole=true
SET JVMARGS=%JVMARGS% -Dweblogic.management.username=weblogic
SET JVMARGS=%JVMARGS% -Dweblogic.management.password=password
SET JVMARGS=%JVMARGS% -Dweblogic.Name=midM1
SET JVMARGS=%JVMARGS% -Djava.security.policy=%WL_HOME%\server\lib\weblogic.policy
SET JVMARGS=%JVMARGS% -Dweblogic.alternateTypesDirectory=%XHIBIT_HOME%\security\build\lib
SET JVMARGS=%JVMARGS% -Dweblogic.ext.dirs=%DOMAIN_HOME%\lib;%XHIBIT_HOME%\lib
@rem XHIBIT JVMARGS
SET JVMARGS=%JVMARGS% -Ddefault.AUTHENTICATION_URL=t3s://%COMPUTERNAME%:9002,%COMPUTERNAME%:9502
SET JVMARGS=%JVMARGS% -Ddefault.PROVIDER_URL=t3://%COMPUTERNAME%:9001,%COMPUTERNAME%:9501
SET JVMARGS=%JVMARGS% -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl
SET JVMARGS=%JVMARGS% -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl
SET JVMARGS=%JVMARGS% -Djavax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl
SET JVMARGS=%JVMARGS% -DdisableMercatorUse=true
SET JVMARGS=%JVMARGS% -Dresults.activateVerifyResults=Y
SET JVMARGS=%JVMARGS% -Dlocator.eager.load
SET JVMARGS=%JVMARGS% -Dlog4j.log.dir=%CD%\logs
@rem XHIBIT MERCATOR INTEGRATION JVMARGS (to use these settings disableMercatorUse must be false)
SET JVMARGS=%JVMARGS% -DMERCATOR_WRAPPER_TYPE=STUB
SET JVMARGS=%JVMARGS% -DMERCATOR_AGENT_NAME=appcomm
SET JVMARGS=%JVMARGS% -DMERCATOR_AGENT_IPADDRESS=130.177.3.216
SET JVMARGS=%JVMARGS% -DMERCATOR_AGENT_PORT=8091
SET JVMARGS=%JVMARGS% -DMERCATOR_CONNECTION_TIMEOUT=60000
SET JVMARGS=%JVMARGS% -DIS_MERCATOR_XML_FILE_LOGGING=true
SET JVMARGS=%JVMARGS% -DIS_MERCATOR_VOLUME_STATS=true
SET JVMARGS=%JVMARGS% -DIS_MERCATOR_PERF_STATS=true
SET JVMARGS=%JVMARGS% -DIS_MERCATOR_XML_TO_LOG=true
SET JVMARGS=%JVMARGS% -DIS_MERCATOR_BUSINESS_ERRORS_FILE_LOGGING=true
SET JVMARGS=%JVMARGS% -DIS_MERCATOR_EXCEPTION_TESTING=false
SET JVMARGS=%JVMARGS% -DMERCATOR_EXCEPTION_TYPE=IO
SET JVMARGS=%JVMARGS% -DMERCATOR_EXCEPTION_RETURN_CODE=37

SET JVMARGS=%JVMARGS% -DdisableSchedulerServlet=true
SET JVMARGS=%JVMARGS% -Dscheduler.scheduledtasks=default

@rem SET JVMARGS
@rem ECHO;


