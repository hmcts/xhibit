
DROP TABLE CJI_PARAMETER_VALUE
/

CREATE TABLE CJI_PARAMETER_VALUE (
	PARAMETER_NAME VARCHAR2 ( 30 ) NOT NULL,
	PARAMETER_VALUE VARCHAR2 ( 255 )
	)
TABLESPACE DATAD
   STORAGE  (
   INITIAL 1M
   NEXT 1M
   PCTINCREASE 0 
  )
/
ALTER TABLE CJI_PARAMETER_VALUE
       ADD  (PRIMARY KEY (PARAMETER_NAME)
       USING INDEX
      TABLESPACE DATAX
   STORAGE  (
   INITIAL 1M
   NEXT 1M
   PCTINCREASE 0 
  ) ) 
/

Insert into CJI_Parameter_value values ('BITSLastMessageTime', '2002-11-08T05:04:02.1234567');
Insert into CJI_Parameter_value values ('BITSPollInterval','5');
Insert into CJI_Parameter_value values ('BITSRegisterTimeOut','86400');
Insert into CJI_Parameter_value values ('BITSRegisterCheckCycle','86400');
Insert into CJI_Parameter_value values ('CJIPLastMessageTime', '2002-11-08T05:04:02.1234567');
Insert into CJI_Parameter_value values ('CJIPPollInterval','2');
Insert into CJI_Parameter_value values ('CJIPOperationTimeOut','3600');
Insert into CJI_Parameter_value values ('CJIPRegisterCheckCycle','86400');
Insert into CJI_Parameter_value values ('AggDeliveryTimeOut','3600');
Insert into CJI_Parameter_value values ('AggLastMessageTime', 'Sat, 14 Jul 2002 19:30:00 GMT');
Insert into CJI_Parameter_value values ('AggPollIntervalStatus','300');
Insert into CJI_Parameter_value values ('AggPollIntervalReply','300');
Insert into CJI_Parameter_value values ('AggRegisterTimeOut','3600');
Insert into CJI_Parameter_value values ('AggRegisterCheckCycle','86400');
Insert into CJI_Parameter_value values ('DocumentDeleteTimeOut','432000');
Insert into CJI_Parameter_value values ('AggDeleteTimeOut','86400');
Insert into CJI_Parameter_value values ('CJIPDeleteTimeOut','432000');
Insert into CJI_Parameter_value values ('DocumentExpiryTimeOut','2592000');
Insert into CJI_Parameter_value values ('AggMessageExpiryInterval','25200');
Insert into CJI_Parameter_value values ('AggLastMessageReplyTime', 'Sat, 14 Jul 2002 19:30:00 GMT');
Insert into CJI_Parameter_value values ('BITSDeleteTimeOut', '432000');
Insert into CJI_Parameter_value values ('MobileSysMONumber', '447781484138');
Insert into CJI_Parameter_value values ('MobileSysReplyMessage', 'An Incorrect reply code was received - this is an automated response - please resend your message with the correct code');

commit;

/
