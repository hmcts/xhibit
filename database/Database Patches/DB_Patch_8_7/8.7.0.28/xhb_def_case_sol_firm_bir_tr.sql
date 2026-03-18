create or replace TRIGGER "XHIBIT".XHB_DEF_CASE_SOL_FIRM_BIR_TR
  BEFORE INSERT
  ON XHB_DEF_ON_CASE_REF_SOL_FIRM
  FOR EACH ROW

  DECLARE v_defoncaserefsolfirmid NUMBER;

  
BEGIN

  IF :NEW.DEF_ON_CASE_REF_SOL_FIRM_ID IS NULL THEN

    SELECT XHB_DEF_CASE_SOL_FIRM_SEQ .NEXTVAL
    INTO   v_defoncaserefsolfirmid
    FROM   DUAL;
	:NEW.DEF_ON_CASE_REF_SOL_FIRM_ID :=v_defoncaserefsolfirmid;
	
	ELSE
		v_defoncaserefsolfirmid := :NEW.DEF_ON_CASE_REF_SOL_FIRM_ID;
  END IF;
  
  IF :NEW.CREST_CPF_ID IS NULL THEN
    :NEW.CREST_CPF_ID :=v_defoncaserefsolfirmid;
  END IF;

  IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
      (:NEW.CREATED_BY IS NULL)) THEN

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