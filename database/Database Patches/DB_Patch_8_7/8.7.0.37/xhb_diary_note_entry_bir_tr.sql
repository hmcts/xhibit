create or replace trigger XHB_DIARY_NOTE_ENTRY_BIR_TR
  BEFORE INSERT
  ON XHB_DIARY_NOTE_ENTRY
  FOR EACH ROW

DECLARE
 v_creation_date DATE := SYSDATE;

BEGIN

  IF :NEW.DIARY_NOTE_ENTRY_ID IS NULL THEN

    SELECT XHB_DIARY_NOTE_ENTRY_SEQ.NEXTVAL
    INTO   :NEW.DIARY_NOTE_ENTRY_ID
    FROM   DUAL;

  END IF;
  
  --For the data migration process.  Use the creation date value the rows being brought over for DM, if it is not null.
  IF :NEW.creation_date IS NOT NULL
   THEN v_creation_date := :NEW.creation_date;
   ELSE
    v_creation_date := SYSDATE;
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
         v_creation_date,
         1
  INTO   :NEW.LAST_UPDATE_DATE,
         :NEW.CREATION_DATE,
         :NEW.VERSION
  FROM   DUAL;

END;
/