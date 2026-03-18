/* deleteXHB_VALIDATION_CJI.sql                                                  */
/* Deletes everything prior to the number of RETAIN_DAYS entered"                */

DECLARE
  input_days INTEGER :='&1';
  rows_before INTEGER;
  rows_after INTEGER;
  rows_before_exi INTEGER;
  rows_after_exi INTEGER;

  earliest_date DATE;
  last_date DATE;
  post_earliest_date DATE;
  post_last_date DATE;
  diff_days INTEGER;
  diff_date DATE;

  rows_to_delete INTEGER;
  rows_to_delete_exi INTEGER;
  actual_rows_deleted INTEGER;
  actual_rows_deleted_exi INTEGER;

  l_start_time NUMBER;
  l_end_time NUMBER;
  l_diff NUMBER;

BEGIN

  l_start_time := DBMS_UTILITY.get_time;

  -- disable any triggers
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_CJI DISABLE CONSTRAINTS XHB_VALIDATION_CJI');
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_EXISS DISABLE CONSTRAINTS XHB_VALIDATION_EXISS');

  -- count of rows at start
--  select count(*) into rows_before from XHB_VALIDATION_CJI cji;
--  select count(*) into rows_before_Exi from XHB_VALIDATION_EXISS exi;
  select min(cji.creation_date) into earliest_date from XHB_VALIDATION_CJI cji;
--  select max(cji.creation_date) into last_date from XHB_VALIDATION_CJI cji;

  IF (sysdate-7-input_days) < (earliest_date)
  THEN
   
     DBMS_OUTPUT.put_line ('Number of days too many for deletion in range');
     DBMS_OUTPUT.put_line ('Earliest date: ' || earliest_date || ' Last date: ' || last_date );
  ELSE

 --delete statement
  BEGIN
 -- select count(*) into rows_to_delete from XHB_VALIDATION_CJI cji where cji.creation_date<=(sysdate-7-input_days);
 -- select count(*) into rows_to_delete_exi from XHB_VALIDATION_EXISS exi,XHB_VALIDATION_CJI cji where exi.validation_id=cji.validation_id and cji.creation_date<=(sysdate-7-input_days);

   delete from XHB_VALIDATION_EXISS exi where exists (select cji.validation_id from xhb_validation_cji cji where exi.validation_id=cji.validation_id and cji.creation_date<=(sysdate-7-input_days));
   actual_rows_deleted_exi := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows deleted (exi):' || actual_rows_deleted_exi);
   
   delete from XHB_VALIDATION_CJI cji where cji.creation_date<=(sysdate-7-input_days);
   actual_rows_deleted := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows deleted (cji):' || actual_rows_deleted);
  END;

  --generate stats info
  
  -- count of rows after
  -- select count(*) into rows_after from XHB_VALIDATION_CJI cji;
 -- select count(*) into rows_after_exi from XHB_VALIDATION_EXISS exi;
 -- select min(cji.creation_date) into post_earliest_date from XHB_VALIDATION_CJI cji;
 -- select max(cji.creation_date) into post_last_date from XHB_VALIDATION_CJI cji;
  -- get timings
  l_end_time := DBMS_UTILITY.get_time;
  l_diff := (l_end_time - l_start_time)/100 ;
  diff_days := sysdate-last_date;


  DBMS_OUTPUT.put_line ('Input days: ' || input_days );
  DBMS_OUTPUT.put_line ('Rows to delete: ' || rows_to_delete );
  DBMS_OUTPUT.put_line ('Rows before (cji): ' || rows_before || ' Rows after (cji): ' || rows_after );
  DBMS_OUTPUT.put_line ('Rows before (exi): ' || rows_before_exi || ' Rows after (exi): ' || rows_after_exi );
  DBMS_OUTPUT.put_line ('Deleting range:' || earliest_date || ' to ' || (sysdate-7-input_days) );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Pre-run earliest date: ' || earliest_date || ' Pre-run latest date of all rows: ' || last_date );
  DBMS_OUTPUT.put_line ('Post-run Earliest date: ' || post_earliest_date || ' Post-run last date of all rows: ' || post_last_date );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Time taken: ' || l_diff || ' seconds' );

  DBMS_OUTPUT.put_line ('');

   END IF;
  -- re-enable all triggers previously disabled
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_CJI ENABLE CONSTRAINTS XHB_VALIDATION_CJI');
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_EXISS ENABLE CONSTRAINTS XHB_VALIDATION_EXISS');
END;
/
