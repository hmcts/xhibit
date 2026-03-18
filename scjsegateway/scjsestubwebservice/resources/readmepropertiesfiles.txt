The properties files are used for stub testing of inbound functionality and
also for testing outbound synchronous responses

Two of the stub files are always (for stub testing) copied to the following location in 
scjsegateway1 and scjsegateway2

/software/apps/scjsegate/managed/scjsegate/properties
WebServiceStubProperties.txt		Submit Response properties (for testing outbound error codes thrown back from Steria in production from the sychronous response. Note the stub does validation on incoming messages as well)
WebServiceDeliveryProperties.txt	Deliver Request properties (properties of an inbound asycn message from Steria)

If running in end to end mode the following file must also be deployed to the above directory:
WebServiceDeliverMessage.txt		Deliver Request body (body (ie message) of an inbound asycn message from Steria. Can be an exception message or a normal message)

Also in end to end mode the WebServiceDeliverProperties.txt must be updated with the Identifier and Type
(when testing an inbound exception the type must be RECEIVEERROR) with the addition of these properties:

XHBMessageIdentifier,1234
XHBMessageTypeType,RECEIVEERROR or specify a <inbound message type>

When running in inbound only mode the outbox of the Inbound Test App can be populated
with as many inbound messages as required
They must be of the format as follows:
<id>Message<type>.txt

Two examples are included for inbound only mode testing - a normal message and an error message

5002MessageXHIBITFirmList.txt
4066MessageRECEIVEERROR.txt

The WebServiceDeliveryProperties.txt and the test inbound message (whether it be the WebServiceDeliverMessage.txt
for end to end testing or messages used for inbound only mode testing) are used in conjunction with the 
WebServiceClientStubProperties.txt (held in the scjsestubwebserviceclient\resources)











