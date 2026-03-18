create or replace trigger XHB_SKELETON_IMP_EXP
  AFTER UPDATE ON XHB_SKELETON_SCHEDULE
  FOR EACH ROW

DECLARE  
  l_courtId NUMBER(8) := NULL;
  l_codeid  NUMBER(8) := NULL;

BEGIN
/*  SELECT skeleton_delivery_status_id INTO l_codeid FROM XHB_SKELETON_DELIVERY_STATUS
   WHERE CODE = 'READY';
*/  
  IF (:NEW.skeleton_delivery_status_id = 2) THEN

    SELECT COURT_ID
    INTO   l_courtId
    FROM   XHB_CASE C
    WHERE  C.CASE_ID = :NEW.CASE_ID;

    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE, STATUS_CODE, CASE_ID, COURT_ID)
    VALUES ('SS','R', :OLD.CASE_ID, l_courtId);
  END IF;

END;
/
