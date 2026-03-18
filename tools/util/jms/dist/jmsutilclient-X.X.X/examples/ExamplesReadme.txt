1) Ensure one or more JMS Body files exists in the dist\jmsutilclient-X.X.X\outbox directory. 
These are named as follows:
id + Message + type 

9997MessageNoRelatesTo.txt will represent an exception message with no RelatesTo field (parameter -y included)
9998MessageRECEIVEERROR.txt will represent an exception message with a RelatesTo field (default conversion template)
9999MessageXHIBITFirmList.txt represents a Firm List (parameter -g included)

You can have as many uniquely identified files as you like

NOTE: It is expected that any RECEIVEERROR messages MUST have a record on the 
EXI ITEM OUTBOUND table with an EXI Item Id that matches the id of the message (eg 9998 in the example(  

NOTE: do not send messages with a duplicate id to a message previously sent as
the same SCJSE Gateway will reject it and it will not be copied to the sentbox if there was already an entry there
   
    