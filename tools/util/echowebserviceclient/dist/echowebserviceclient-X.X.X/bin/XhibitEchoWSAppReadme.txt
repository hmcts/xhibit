Executing the echowebserviceclient application

The echowebserviceclient is a standalone app that relies on java classes in Weblogic 9.2, SUN JDK 1.5_04 
and the bespoke jar file: echowebserviceclient.jar. 

The echowebserviceclient.jar should not be deployed to Weblogic - the Main class within it
should be executed using echowebserviceclient.bat (Windows) or an equivalent Solaris script
using the properties specified in EchoWebServiceClientProperties.txt 
and a proxy password on the command line if required

On execution a request echo message will be sent to and a response echo message received 
from a remote Echo Web Service deployed on CJSE (Steria).

The echowebserviceclient can be run in one of two modes based on the 
EchoWebServiceProxyEnabled property in EchoWebServiceClientProperties.txt:
1. The message will be sent via a Proxy (EchoWebServiceProxyEnabled,true). This is the default mode
2. The message will be direct (EchoWebServiceProxyEnabled,false).


Sending a message via a Proxy to CJSE
This will send a request echo message (XHIBIT) to a remote Echo Web Service (deployed on CJSE (Steria)) 
via a Proxy and receive a response echo message

These instructions refer to Windows Execution of echowebserviceclient.bat in steps (4) and (5)
1) Ensure a EchoWebServiceClientProperties.txt file exists in the 'properties' directory.
2) Add the IP Address, Port Number and Web Service Name of the remote Echo Web Service to EchoWebServiceClientProperties.txt.
3) Add the Hostname, Port Number and User Name of the Proxy and ensure EchoWebServiceProxyEnabled is set to true.
4) Launch a DOS Window from the bin directory.
5) Execute the echowebserviceclient.bat command from DOS passing in the unencrypted Password of the Proxy a single parameter
6) If the message "Could not determine BEA_HOME, please set BEA_HOME environment variable and rerun" ensure the BEA_HOME is set and echowebserviceclient.bat can find it
7) The request echo message will be saved to the outbox in a dated directory
8) On a successful execution the response echo message will be saved to the inbox in a dated directory
9) On a failed execution the error message will be saved to the errorbox in a dated directory.
10) Additional debug is available in echoDebug.log in the bin directory


Sending a message direct to CJSE (no proxy)
This will send an request echo message (XHIBIT) direct to a remote Echo Web Service (deployed on CJSE (Steria)) 
and receive a response echo message.
   
These instructions refer to Windows Execution of echowebserviceclient.bat in steps (4) and (5)
1) Ensure a EchoWebServiceClientProperties.txt file exists in the 'properties' directory.
2) Add the IP Address, Port Number and Web Service Name of the remote Echo Web Service to EchoWebServiceClientProperties.txt.
3) Ensure EchoWebServiceProxyEnabled is set to false in EchoWebServiceClientProperties.txt
4) Launch a DOS Window from the bin directory.
5) Execute the echowebserviceclient.bat command from DOS. Do not pass any parameters
6) If the message "Could not determine BEA_HOME, please set BEA_HOME environment variable and rerun" ensure the BEA_HOME is set and echowebserviceclient.bat can find it
7) The request echo message will be saved to the outbox in a dated directory
8) On a successful execution the response echo message will be saved to the inbox in a dated directory
9) On a failed execution the error message will be saved to the errorbox in a dated directory.
10) Additional debug is available in echoDebug.log in the bin directory
    

EchoWebServiceClientProperties.txt properties overview

EchoWebServiceIPAddress,<Remote_Echo_Web_Service_IP_Address>
EchoWebServicePort,<Remote_Echo_Web_Service_Port>
EchoWebServiceName,<Remote_Echo_Web_Service_Name>
EchoWebServiceProxyEnabled,true
EchoWebServiceProxyHost,<Proxy_Host_Name>
EchoWebServiceProxyPort,<Proxy_Port>
EchoWebServiceProxyUser,<Proxy_User>
EchoWebServiceConnectionTimeout,0
EchoWebServiceReadTimeout,0

The EchoWebServiceIPAddress, Port and Name will be that of the Echo Web Service deployed on CJSE (Steria)
The EchoWebServiceName may also include the context path eg services/echo
The EchoWebServiceProxyEnabled will be true by default, if false the EchoWebServiceProxyHost,Port and User will not be required, neither will the Proxy Password on the commmand line
The EchoWebServiceProxyHost,Port and User will be for the Proxy between the SCJSE Gateway (XHIBIT system) and CJSE (Steria)

The two timeout properties default to 0 (ie no timeout) but can be set if required:
EchoWebServiceConnectionTimeout: weblogic.wsee.transport.connection.timeout (milliseconds (WL 9.2 (Windows)))
EchoWebServiceReadTimeout: weblogic.wsee.transport.read.timeout (hundredths of a second (WL 9.2 (Windows)))


Troubleshooting
Check the Echo Web Service has been deployed successfully to CJSE (Steria) as per the properties specified in (2)
Check the error logged in (9) and the echoDebug.log in (10) to see whether any additional info has been logged
Check the BEA Home (6) is set correctly - the echowebserviceclient relies on BEA classes from Weblogic 9.2

If there is Proxy between the SCJSE Gateway (Xhibit) and CJSE (Steria):
Check the Proxy is setup as per the properties specified in (3) 
and the proxy password passed in as a command line parameter in (5) 
is that required for the proxy user specified
Ensure EchoWebServiceProxyEnabled in EchoWebServiceClientProperties.txt is true



