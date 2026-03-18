CREATE OR REPLACE TRIGGER xhb_ref_cracked_effect_bir_tr
  BEFORE INSERT
  ON xhb_ref_cracked_effective
  FOR EACH ROW
  
BEGIN

  IF :NEW.REF_CRACKED_EFFECTIVE_ID IS NULL THEN
  
    SELECT XHB_REF_CRACKED_EFFECTIVE_SEQ.NEXTVAL
    INTO   :NEW.REF_CRACKED_EFFECTIVE_ID
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

END xhb_ref_cracked_effect_bir_tr;
/