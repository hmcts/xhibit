CREATE OR REPLACE TRIGGER XHB_LIST_BIR_TR
  BEFORE INSERT
  ON XHB_LIST
  FOR EACH ROW
DECLARE
  v_list_id XHB_LIST.LIST_ID%TYPE;
  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);
BEGIN

  IF :NEW.LIST_ID IS NULL THEN
  
    SELECT XHB_LIST_SEQ.NEXTVAL
    INTO   :NEW.LIST_ID
    FROM   DUAL;
    
  END IF;

  v_list_id := xhb_listing_pkg.get_existing_list_id(
                  p_court_id     => :NEW.COURT_ID,
                  p_list_type_id => :NEW.LIST_TYPE_ID,
                  p_start_date   => :NEW.LIST_START_DATE,
                  p_end_date     => :NEW.LIST_END_DATE);
  IF NVL(v_list_id,:NEW.LIST_ID) <> :NEW.LIST_ID THEN
     RAISE OPTIMISTIC_LOCK_PROB;
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

END;
/