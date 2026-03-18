1) Ensure one or more JMS Body files exists in the dist\jmsutilclient-X.X.X\outbox directory. 
These are named as follows:
id + Message + type 
eg 9998MessageRECEIVEERROR.txt will represent an exception message (type must be RECEIVEERROR)
eg 9999MessageXHIBITFirmList.txt represents a Firm List

You can have as many uniquely identified files as you like

2) After running the test all files in the dist\jmsutilclient-X.X.X\outbox will be moved to the sentbox

NOTE: messages with duplicate ids will NOT be moved if there is already a copy in the sentbox
(if you sent the duplicate message to the same SCJSE Gateway then it will be rejected as a duplicate)
   
    