/*
	CTX-4295 adding message lookup delay
*/
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'MESSAGE_LOOKUP_DELAY',60);

/*
	CTX-4360 Test/Dev
*/
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.midM1','transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.midM2','transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.midM3','transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');

Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.DevTest_as1_midM1','transformtask,listdistributiontask,ahmpollertask,courtlogtask, dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.DevTest_as1_midM2','transformtask,listdistributiontask,ahmpollertask,courtlogtask, dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.DevTest_as1_midM3','transformtask,listdistributiontask,ahmpollertask,courtlogtask, dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.DevTest_as1_midM4','transformtask,listdistributiontask,ahmpollertask,courtlogtask, dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');

Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.DevTest1midM1','transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.DevTest1midM2','transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.DevTest1midM3','transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.DevTest1midM4','transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');


/*
 CTX-4360 NLE
*/
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.NLEmidM1', 'transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.NLEmidM2', 'transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.NLEmidM3', 'transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.NLEmidM4', 'transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.NLEmidM5', 'transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask');

/*
 CTX-4360 LIVE, also CTX-4294 
*/
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.LIVEmidM1','transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.LIVEmidM2','transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.LIVEmidM3', 'transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.LIVEmidM4', 'transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask,courtellisttask');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) 
values (XHB_CONFIG_PROP_SEQ.NEXTVAL,'scheduledtasks.LIVEmidM5', 'transformtask,listdistributiontask,ahmpollertask,courtlogtask,dartsmessagetask1,dartsmessagetask2,dartsmessagetask3,dartsprioritymessagetask');

COMMIT;