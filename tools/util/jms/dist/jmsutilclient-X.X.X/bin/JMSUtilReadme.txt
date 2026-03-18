The jmsutilclient.bat is for the purpose of testing the SCJSE Gateway domain in isolation.

Instructions to run the jmsutilclient.bat for multiple inbound messages.
There are two ways to run the Inbound Test App

1. Sending existing inbound messages (as per release 8p1p1)
2. Converting Outbound Messages to Inbound Messages before sending them. This is new functionality for 8p1p3 and is the default setup. It is useful for inbound performance tests and for testing different types of inbound message (exception (with or without RelatesTo) and normal)




Converting Outbound Messages to Inbound Messages before sending them

This will convert all outbound messages in the specified stub_inbox to an appropriate inbound format. This is the default setup
There are three inbound formats that can be tested:

1. Inbound Exception Messages with a RelatesTo field: This is the default setup
2. Inbound Exception Messages with no RelatesTo field: created if -y is present unless -g is present in the java.exe command inside the jmsutilclient.bat
3. Normal Inbound Messages: created if -g is present in the java.exe command inside the jmsutilclient.bat

From there the are sent to the stub where they will be converted to inbound deliver request messages and sent to the Inbound Web Service on the SCJSE Gateway
 
Refer to the dist\jmsutilclient-X.X.X\convert\ConvertReadme.txt document for more information

1) Comment this command in jmsutilclient.bat to disable the original 8p1p1 command (this is commented out by default)
@rem %JAVA_HOME%\bin\java.exe %JAVA_OPTIONS% -classpath %CLASSPATH% jmsutil.Main -q %1 -d %2 -m -s

Uncomment this command 
%JAVA_HOME%\bin\java.exe %JAVA_OPTIONS% -classpath %CLASSPATH% jmsutil.Main -q %1 -d %2 -a %3 -i %4 -b %5 -m -s >inboundAppDebug.log
 
2)  Ensure the directory (parameter b) where the outbound messages to be converted has some outbound messages that have been already been sent to the stub. 

The default directory is dist\jmsutilclient-X.X.X\convert but the directory where the outbound messages have been dumped in the stub 
user_projects\Cjsestub\stub_inbox\<dated_directory>

An example message will have the following format (other messages will be ignored):
<id>_Outbound_Receive_Business_Request_<time>.xml
Eg
38063_Outbound_Receive_Business_Request_12-28-39-368.xml

You can have as many uniquely identified outbound messages of the above format as you like

3) Ensure the dist\jmsutilclient-X.X.X\examples directory has the following templates 
9997MessageNoRelatesTo.txt is the template for inbound exception messages with no relates to field (used if -y is present unless -g is present)
9998MessageRECEIVEERROR.txt is the template for inbound exception messages with a relates to field (default)
9999MessageXHIBITFirmList.txt is the template for inbound normal messages with a relates to field (used if -g is present)

4) Launch a DOS Window and go to the dist\jmsutilclient-X.X.X\bin directory.

5) Call the jmsutilclient.bat file with the following 5 arguments set:

jmsutilclient.bat <IP Address (or hostname) and Port Number of SCJSE Stub Server 1>  scjsegateway/jms/InboundTestQueue <delay> <INBOUND_MESSAGE_ID> <Outbound Message Location>

eg
jmsutilclient.bat 130.177.3.58:8001 scjsegateway/jms/InboundTestQueue 500 10000 D:\user_projects\Cjsestub\stub_inbox\sjcse_ws_stub_log_scjsegateM1_28-03-2007
        
Description of Arguments
    
<IP Address (or hostname)  and Port Number of SCJSE Stub Server 1>  - IP Address (or hostname) and port number of the Weblogic running on the SCJSE Stub

For testing inbound messages the next parameter must always be the JNDI name of the inbound test queue (which MUST be set up on the SCJSE Stub)
    scjsegateway/jms/InboundTestQueue

<delay> is a number in milliseconds set to an appropriate value to avoid overloading the stub (defaults to 300ms)

<INBOUND_MESSAGE_ID> is the next appropriate INBOUND_MESSAGE_ID on the GDG_INBOUND_MESSAGES table on the GDGATE database

<Outbound Message Location>: directory path to where the outbound messages to be converted are (user_projects\Cjsestub\stub_inbox\<dated_directory>)

Optional parameter:
Can add -z to the java.exe startup command in jmsutilclient.bat if required
A second Stub weblogic can be added if required     

Optional  parameter 
Can add -g to the java.exe startup command in jmsutilclient.bat if required)

If you wish to convert outbound messages to normal inbound messages add parameter -g to the %JAVA_HOME%\bin\java.exe in jmsutilclient.bat

If you wish to convert outbound messages to exception messages ensure -g is not present in the %JAVA_HOME%\bin\java.exe in jmsutilclient.bat

Optional parameter
Can add -y to the java.exe startup command in jmsutilclient.bat if required
Must remove -g from the java.exe startup command in jmsutilclient.bat

If you wish to convert outbound messages to exception inbound messages with no RelatesTo field add parameter -y to the %JAVA_HOME%\bin\java.exe in jmsutilclient.bat

If you wish to convert outbound messages to exception inbound messages with a RelatesTo field ensure -y is not present in the %JAVA_HOME%\bin\java.exe in jmsutilclient.bat
    

6) After running the test all converted inbound files will be moved to the sentbox  
 
 NOTE: messages with duplicate ids will NOT be moved if there is already a copy in the sentbox

 NOTE: the original outbound message will NOT be moved anywhere



Sending existing inbound messages (8p1p1 backwards compatibility)

This will send all inbound messages in the outbox directory to the stub where they will be converted to inbound deliver request messages and sent to the Inbound Web Service on the SCJSE Gateway
The messages must be manually created using the examples in the examples directory as a template

1)
Uncomment this command in jmsutilclient.bat to enable the original 8p1p1 command (this is commented out by default)
%JAVA_HOME%\bin\java.exe %JAVA_OPTIONS% -classpath %CLASSPATH% jmsutil.Main -q %1 -d %2 -m -s

Comment this command 
@rem %JAVA_HOME%\bin\java.exe %JAVA_OPTIONS% -classpath %CLASSPATH% jmsutil.Main -q %1 -d %2 -a %3 -i %4 -b %5 -m -s >inboundAppDebug.log


2) Ensure one or more JMS Body files exists in the dist\jmsutilclient-X.X.X\outbox directory. 
These are named as follows:
id + Message + type
Use the templates in the examples directory to create and name the messages:
 
9997MessageNoRelatesTo.txt is the template for inbound exception messages with no relates to field (used if -y is present unless -g is present)
9998MessageRECEIVEERROR.txt is the template for inbound exception messages with a relates to field (default)
9999MessageXHIBITFirmList.txt is the template for inbound normal messages with a relates to field (used if -g is present)

You can have as many uniquely identified files as you like

3) Launch a DOS Window and go to the dist\jmsutilclient-X.X.X\bin directory.

4) Call the jmsutilclient.bat file with the following arguments set:

jmsutilclient.bat <IP Address (or hostname) and Port Number of SCJSE Stub Server 1> scjsegateway/jms/InboundTestQueue
    
eg
jmsutilclient.bat 130.177.3.58:8001 scjsegateway/jms/InboundTestQueue
 
    
   
 Description of Arguments
    
<IP Address (or hostname)  and Port Number of SCJSE Stub Server 1>  - IP Address (or hostname) and port number of the Weblogic running on the SCJSE Stub

For testing inbound messages the next parameter must always be the JNDI name of the inbound test queue (which MUST be set up on the SCJSE Stub)
    scjsegateway/jms/InboundTestQueue

Optional parameter:
Can add -z to the java.exe startup command in jmsutilclient.bat if required
A second Stub weblogic can be added if required     

    
5) After running the test all files in the dist\jmsutilclient-X.X.X\outbox will be moved to the sentbox  
 
NOTE: messages with duplicate ids will NOT be moved if there is already a copy in the sentbox
(if you sent the duplicate message to the same SCJSE Gateway then it will be rejected as a duplicate)



   
    