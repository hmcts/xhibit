/*************************************/
/* Create Sequence Triggers          */
/*************************************/

create or replace trigger CJI_DOCUMENT_TR
  BEFORE INSERT
  on CJI_DOCUMENT
  
  for each row
BEGIN

IF :NEW.DOCUMENT_ID  IS NULL THEN
  SELECT CJI_DOCUMENT_SEQ.NEXTVAL INTO                 :NEW.DOCUMENT_ID  FROM DUAL;
END IF;


end;
/
create or replace trigger CJI_EVENT_TR
  BEFORE INSERT
  on CJI_EVENT
  
  for each row
BEGIN

IF :NEW.EVENT_ID   IS NULL THEN
  SELECT CJI_EVENT_SEQ.NEXTVAL INTO                 :NEW.EVENT_ID  FROM DUAL;
END IF;


end;
/
create or replace trigger CJI_AHM_TR
  BEFORE INSERT
  on CJI_AHM
  
  for each row
BEGIN

IF :NEW.MESSAGE_ID  IS NULL THEN
  SELECT CJI_AHM_SEQ.NEXTVAL INTO                 :NEW.MESSAGE_ID  FROM DUAL;
END IF;


end;
/
create or replace trigger CJI_AHM_REPLY_TR
  BEFORE INSERT
  on CJI_AHM_REPLY
  
  for each row
BEGIN

IF :NEW.REPLY_ID  IS NULL THEN
  SELECT CJI_AHM_REPLY_SEQ.NEXTVAL INTO                 :NEW.REPLY_ID  FROM DUAL;
END IF;


end;
/
create or replace trigger CJI_CJIP_REQUEST_TR
  BEFORE INSERT
  on CJI_CJIP_REQUEST
  
  for each row
BEGIN

IF :NEW.REQUEST_ID   IS NULL THEN
  SELECT CJI_CJIP_REQUEST_SEQ.NEXTVAL INTO                 :NEW.REQUEST_ID   FROM DUAL;
END IF;


end;
/

