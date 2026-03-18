CREATE OR REPLACE TRIGGER xhb_case_history_bir_tr
  BEFORE INSERT
  ON xhb_case_history
  FOR EACH ROW
  
BEGIN

  IF :NEW.CASE_HISTORY_ID IS NULL THEN
  
    SELECT XHB_CASE_HISTORY_SEQ.NEXTVAL
    INTO   :NEW.CASE_HISTORY_ID
    FROM   DUAL;
    
  END IF;

  IF ((:NEW.LAST_UPDATED_BY IS NULL)
     OR (:NEW.CREATED_BY IS NULL)) THEN
     
    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
           SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   :NEW.LAST_UPDATED_BY,
           :NEW.CREATED_BY 
    FROM   DUAL;
    
  END IF;

  SELECT SYSDATE,
         SYSDATE,
         1
  INTO   :NEW.LAST_UPDATE_DATE, 
         :NEW.CREATION_DATE, 
         :NEW.VERSION 
  FROM   DUAL;

END xhb_case_history_bir_tr;
/