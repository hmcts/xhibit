CREATE OR REPLACE TRIGGER xhb_def_on_case_on_list_bir_tr
  BEFORE INSERT
  ON xhb_def_on_case_on_list
  FOR EACH ROW
  
BEGIN

  IF :NEW.DEF_ON_CASE_ON_LIST_ID IS NULL THEN
  
    SELECT XHB_DEF_ON_CASE_ON_LIST_SEQ.NEXTVAL
    INTO   :NEW.DEF_ON_CASE_ON_LIST_ID
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

END xhb_def_on_case_on_list_bir_tr;
/