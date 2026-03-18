/* deleteXHB_VALIDATION_EXISS.sql                                                */
/* Deletes the number of DAYS                                                    */

DECLARE
  input_days INTEGER :='&1';
  rows_before INTEGER;
  rows_after INTEGER;
  rows_before_cji INTEGER;
  rows_after_cji INTEGER;

  earliest_date DATE;
  last_date DATE;
  post_earliest_date DATE;
  post_last_date DATE;
  diff_days INTEGER;

  rows_to_delete INTEGER;
  rows_to_delete_cji INTEGER;
  actual_rows_deleted INTEGER;
  actual_rows_deleted_cji INTEGER;

  l_start_time NUMBER;
  l_end_time NUMBER;
  l_diff NUMBER;

BEGIN

  l_start_time := DBMS_UTILITY.get_time;

  -- disable any triggers
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_EXISS DISABLE CONSTRAINTS XHB_VALIDATION_EXISS');
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_CJI DISABLE CONSTRAINTS XHB_VALIDATION_CJI');

  -- count of rows at start
--  select count(*) into rows_before_cji from XHB_VALIDATION_CJI cji;
--  select count(*) into rows_before from XHB_VALIDATION_EXISS exi;
  select min(exi.item_created) into earliest_date from XHB_VALIDATION_EXISS exi;
 -- select max(exi.item_created) into last_date from XHB_VALIDATION_EXISS exi;

  IF (sysdate-7) < (earliest_date+input_days)
  THEN
     diff_days := input_days-(sysdate-7-last_date);
     DBMS_OUTPUT.put_line ('Number of days too many for deletion');
     DBMS_OUTPUT.put_line ('Earliest date: ' || earliest_date || ' Last date: ' || last_date );
  ELSE

  --delete statement
 BEGIN
 -- select count(*) into rows_to_delete from XHB_VALIDATION_EXISS exi where exi.item_created>=earliest_date and exi.item_created<=(earliest_date+input_days-1);
--  select count(*) into rows_to_delete_cji from XHB_VALIDATION_EXISS exi where exists (select cji.validation_id from xhb_validation_cji cji where cji.validation_id=exi.validation_id and exi.item_created>=earliest_date and exi.item_created<=(earliest_date+input_days-1));

  delete from XHB_VALIDATION_CJI cji where exists(select exi.validation_id from xhb_validation_exiss exi where cji.validation_id=exi.validation_id and exi.item_created>=earliest_date and exi.item_created<=(earliest_date+input_days-1));
  actual_rows_deleted_cji := SQL%ROWCOUNT;
  DBMS_OUTPUT.put_line('Rows deleted (cji):' || actual_rows_deleted_cji);

  delete from XHB_VALIDATION_EXISS exi where exi.item_created>=earliest_date and exi.item_created<=(earliest_date+input_days-1);
   actual_rows_deleted := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows deleted:' || actual_rows_deleted);
  END;

  --generate stats info
  -- count of rows after
--  select count(*) into rows_after from XHB_VALIDATION_EXISS exi;
--  select count(*) into rows_after_cji from XHB_VALIDATION_CJI cji;
--  select min(exi.item_created) into post_earliest_date from XHB_VALIDATION_EXISS exi;
--  select max(exi.item_created) into post_last_date from XHB_VALIDATION_EXISS exi;
  -- get timings
  l_end_time := DBMS_UTILITY.get_time;
  l_diff := (l_end_time - l_start_time)/100 ;
  diff_days := sysdate-last_date;


  DBMS_OUTPUT.put_line ('Input days: ' || input_days );
  DBMS_OUTPUT.put_line ('Rows to delete (exi): ' || rows_to_delete);
  DBMS_OUTPUT.put_line ('Rows before (cji): ' || rows_before_cji || ' Rows after (cji): ' || rows_after_cji);
  DBMS_OUTPUT.put_line ('Rows before (exi): ' || rows_before || ' Rows after (exi): ' || rows_after);
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Pre-run earliest date: ' || earliest_date || ' Pre-run latest date of all rows: ' || last_date );
  DBMS_OUTPUT.put_line ('Post-run Earliest date: ' || post_earliest_date || ' Post-run last date of all rows: ' || post_last_date );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Time taken: ' || l_diff || ' seconds' );

  DBMS_OUTPUT.put_line ('');

   END IF;
  -- re-enable all triggers previously disabled
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_EXISS ENABLE CONSTRAINTS XHB_VALIDATION_EXISS');
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_CJI ENABLE CONSTRAINTS XHB_VALIDATION_CJI');
END;
/

