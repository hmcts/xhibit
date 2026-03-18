DECLARE
  v_new_seq_last_id NUMBER;
BEGIN
  FOR rec IN (SELECT SUBQRY.*, SUBQRY.ACTUAL_LAST_ID - SUBQRY.SEQ_LAST_ID LOOP_COUNT FROM (SELECT 
                     (SELECT MAX(config_prop_id) FROM xhb_config_prop) ACTUAL_LAST_ID,
                     (SELECT last_number FROM all_sequences WHERE SEQUENCE_NAME = 'XHB_CONFIG_PROP_SEQ') SEQ_LAST_ID
                     FROM DUAL) SUBQRY
                     WHERE SUBQRY.SEQ_LAST_ID <= ACTUAL_LAST_ID) LOOP
      DBMS_OUTPUT.PUT_LINE('Actual Last Id = '||rec.ACTUAL_LAST_ID);
      DBMS_OUTPUT.PUT_LINE('Sequence Last Id = '||rec.SEQ_LAST_ID);
      -- Increase the SEQ id to match the actual last id
      FOR loop_no IN 1..rec.LOOP_COUNT LOOP
         SELECT XHB_CONFIG_PROP_SEQ.NEXTVAL INTO v_new_seq_last_id FROM DUAL;
      END LOOP;
      DBMS_OUTPUT.PUT_LINE('New Sequence Last Id = '||v_new_seq_last_id);
  END LOOP;
END;
/