
1) After running the test all files in the dist\jmsutilclient-X.X.X\outbox will be moved to the sentbox

NOTE: messages with duplicate ids will NOT be moved if there is already a copy in the sentbox
(if you sent the duplicate message to the same SCJSE Gateway then it will be rejected as a duplicate)
   
    