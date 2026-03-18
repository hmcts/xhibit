DECLARE
   v_next_id NUMBER;
   v_max_no  NUMBER;

   unique_constraint EXCEPTION;
   PRAGMA EXCEPTION_INIT(unique_constraint,-1);
   
   PROCEDURE insert_record IS
   BEGIN
      INSERT INTO XHB_CONFIG_PROP( CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) 
      SELECT XHB_CONFIG_PROP_SEQ.NEXTVAL, 'BATCH_CASES_ALLOWED_TO_DELETE', 1 FROM DUAL
      WHERE NOT EXISTS (SELECT 1 FROM XHB_CONFIG_PROP X WHERE X.PROPERTY_NAME = 'BATCH_CASES_ALLOWED_TO_DELETE'); 
      COMMIT;
   END insert_record;
   
   PROCEDURE get_next_id IS
   BEGIN 
     SELECT XHB_CONFIG_PROP_SEQ.NEXTVAL
       INTO v_next_id
       FROM DUAL;
   END get_next_id;
   
BEGIN
  get_next_id;
  insert_record;
EXCEPTION WHEN unique_constraint THEN
    SELECT MAX(CONFIG_PROP_ID) INTO v_max_no FROM XHB_CONFIG_PROP;
    LOOP
      EXIT WHEN v_next_id >= v_max_no;
      get_next_id;
    END LOOP;
    insert_record;
END;
/