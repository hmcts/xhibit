@ECHO off

SETLOCAL

@rem 
@rem Check Parameters
@rem 

SET SERVER_NUMBER=%1

IF DEFINED SERVER_NUMBER GOTO SERVER_NUMBER_DEFINED
ECHO please pass SERVER_NUMBER parameter.
EXIT /B 1
:SERVER_NUMBER_DEFINED

@rem
@rem Check/Set Environment
@rem

CALL %~pd0%startcommon.bat
IF %ERRORLEVEL% EQU 0 GOTO START_COMMON_OK
EXIT /B 1
:START_COMMON_OK
                
@rem 
@rem Set Java Options
@rem 

@rem JAVA JAVA_OPTIONS
SET JAVA_OPTIONS=-server
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Xms128m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Xmx256m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -XX:PermSize=64m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -XX:MaxPermSize=128m
SET JAVA_OPTIONS=%JAVA_OPTIONS% -da
@rem WERBLOGIC JAVA_OPTIONS
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dplatform.home=%WL_HOME%
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwls.home=%WL_HOME%\server
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwli.home=%WL_HOME%\integration
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.management.discover=false
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.management.server=http://@computer.name@:@scjsegateadmin.port@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwlw.iterativeDev=false
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwlw.testConsole=false
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwlw.logErrorsToConsole=true
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.management.username=@admin.username@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.management.password=@admin.password@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.Name=scjsegateM%SERVER_NUMBER%
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Djava.security.policy=%WL_HOME%\server\lib\weblogic.policy
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dweblogic.ext.dirs=%DOMAIN_HOME%\lib;%APPLICATION_HOME%\lib
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dlog4j.log.dir=%DOMAIN_HOME%\logs

@rem XHIBIT JAVA_OPTIONS
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Ddefault.AUTHENTICATION_URL=t3s://@computer.name@:@scjsegateM1.sslport@,@computer.name@:@scjsegateM2.sslport@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Ddefault.PROVIDER_URL=t3://@computer.name@:@scjsegateM1.port@,@computer.name@:@scjsegateM2.port@

@rem Switches for Scheduler process
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DdisableSchedulerServlet=@disable.scheduler@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dscheduler.scheduledtasks=@scheduler.scheduledtasks@

@rem Implementation of the Web Service Client on the SCJSE Gateway
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Duk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClient=uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.client.ServiceWSClientStub

@rem This Implementation will go via a Proxy Server
@rem SET JAVA_OPTIONS=%JAVA_OPTIONS% -Duk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClient=uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClientProxy

@rem Implementation of the SCJSE Outbound Processor  on the SCJSE Gateway
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Duk.gov.courtservice.xhibit.services.scjsegateway.outbound.ScjseOutboundProcessor=uk.gov.courtservice.xhibit.services.gdgateway.outbound.ScjseOutboundProcessorGDImpl

@rem URL and name of the Inbound Web Service (format: http://ip_address:port/delivery/services/Delivery)
@rem Used only in testing - it is intended that the Inbound Web Service location be specified on the GD Gateway Database in production
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DINBOUND_WEB_SERVICE_LOCATION=http://@computer.name@:@scjsegateM1.port@/delivery/services/Delivery

@rem URL and name of the Stub Web Service (format: http://ip_address:port/stubdelivery/stubservices/StubDelivery)
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DSTUB_WEB_SERVICE_LOCATION=http://@computer.name@:@scjsegateM1.port@/stubdelivery/stubservices/StubDelivery

@rem Properties directory of the Stub web service and the Stub Web Service Client
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dwebservice.stub.dir=%DOMAIN_HOME%\properties

@rem Stub properties for additional stub testing and logging. Set all to false (or remove) in production
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DWS_CLIENT_XML_FILE_LOGGING=TRUE
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DWS_XML_FILE_LOGGING=TRUE
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DSPECIALISED_VALIDATION_ENABLED=TRUE

@rem Properties for additional logging from the outbound WS Client. Set all to FALSE (or remove) in production
@rem If the Java Stub Web Service is used it will do all the logging and the two booleans should set to FALSE
@rem WS_CLIENT_LOG_DIR_NAME should be set to "stub" for inbound testing using the Inbound Test App and Java Stub Web Service
@rem WS_CLIENT_LOG_DIR_NAME should be set to  "message" for the outbound testing - the test directories are named after this property
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DWS_CLIENT_XML_FILE_LOGGING=FALSE
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DFORCE_WS_CLIENT_XML_FILE_LOGGING=FALSE
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DWS_CLIENT_LOG_DIR_NAME=stub

@rem Enables end to end (Stib creates an Inbound message when an Outbound message received). False by default
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DINBOUND_TESTING_ENABLED=FALSE

@rem Web Service Timeout properties (required for stub only as the production values are in the DB)
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DWEB_SERVICE_CONNECTION_TIMEOUT=0
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DWEB_SERVICE_READ_TIMEOUT=0

@rem Proxy Testing - the next 8 properties are for Proxy testing in DEV only - ServiceWSClient must be set to ServiceWSClientProxy
@rem Proxy Testing - the environment specific properties for the SCJSE Gateway can be retrieved from this file if the DB equivalents are blank
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dscjse.serverstart.args.enabled=false

@rem Proxy Testing - The password MUST be set as a startup argument (it may or may not be encrypted depending on whether the Proxy will compare the encrypted version against its local copy of the encrypted password)
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DPROXY_PASSWORD=password

@rem Proxy Testing - The next 6 properties are only used if the corresponding property on the GDG_CONFIG_PROPERTIES table is blank and scjse.serverstart.args.enabled is true
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DPROXY_HOST=@computer.name@
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DPROXY_PORT=1001
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DPROXY_USER=xhibit
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DSCJSE_WEB_IP_ADDR=@computer.name@ 
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DSCJSE_WEB_PORT=@scjsegateM1.port@ 
SET JAVA_OPTIONS=%JAVA_OPTIONS% -DSCJSE_WEB_NAME=stubdelivery/stubservices/StubDelivery

@rem 
@rem Set Command
@rem 

SET COMMAND=%JAVA_HOME%\bin\java %JAVA_OPTIONS% weblogic.Server

@rem 
@rem Execute Weblogic
@rem 

ECHO;
SET PATH

ECHO;
SET CLASSPATH
 
ECHO;
SET COMMAND
 
@rem ECHO;
%COMMAND%

