/* deleteEXI_ITEM_INBOUND.sql                                                    */
/* Deletes everything prior to the number of RETAIN_DAYS entered"                */

DECLARE
  input_days INTEGER :='&1';
  rows_before INTEGER;
  rows_after INTEGER;

  earliest_date DATE;
  last_date DATE;
  post_earliest_date DATE;
  post_last_date DATE;
  diff_days INTEGER;

  rows_to_delete INTEGER;
  actual_rows_deleted INTEGER;

  l_start_time NUMBER;
  l_end_time NUMBER;
  l_diff NUMBER;

BEGIN

  l_start_time := DBMS_UTILITY.get_time;

  -- disable any triggers
--  EXECUTE IMMEDIATE ('ALTER TABLE EXISS.EXI_ITEM_INBOUND_PROPERTIES DISABLE CONSTRAINTS ITEM_ID_FK');


  -- count of rows at start
--  select count(exi.item_id) into rows_before from EXISS.EXI_ITEM_INBOUND exi;
  select min(exi.date_created) into earliest_date from EXISS.EXI_ITEM_INBOUND exi;
--  select max(exi.date_created) into last_date from EXISS.EXI_ITEM_INBOUND exi;

  IF (sysdate-7-input_days) < (earliest_date)
  THEN
     diff_days := input_days-(sysdate-7-last_date);
     DBMS_OUTPUT.put_line ('Number of days too many for for deletion in range');
     DBMS_OUTPUT.put_line ('Earliest date: ' || earliest_date || ' Last date: ' || last_date );
  ELSE

   --delete statement
  BEGIN
 --  select count(exi.date_created) into rows_to_delete from EXISS.EXI_ITEM_INBOUND exi where exi.date_created<=(sysdate-7-input_days);

   delete from EXISS.EXI_ITEM_INBOUND exi where exi.date_created<=(sysdate-7-input_days);
   actual_rows_deleted := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows deleted:' || actual_rows_deleted);
  END;

  --generate stats info
 
  -- count of rows after
  --select count(exi.item_id) into rows_after from EXISS.EXI_ITEM_INBOUND exi;
 -- select min(exi.date_created) into post_earliest_date from EXISS.EXI_ITEM_INBOUND exi;
 -- select max(exi.date_created) into post_last_date from EXISS.EXI_ITEM_INBOUND exi;
  -- get timings
  l_end_time := DBMS_UTILITY.get_time;
  l_diff := (l_end_time - l_start_time)/100 ;
  diff_days := sysdate-last_date;


 DBMS_OUTPUT.put_line ('Input days: ' || input_days );
  DBMS_OUTPUT.put_line ('Rows to delete: ' || rows_to_delete );
  DBMS_OUTPUT.put_line ('Rows before: ' || rows_before || ' Rows after: ' || rows_after );
  DBMS_OUTPUT.put_line ('Deleting range:' || earliest_date || ' to ' || (sysdate-7-input_days) );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Pre-run earliest date: ' || earliest_date || ' Pre-run latest date of all rows: ' || last_date );
  DBMS_OUTPUT.put_line ('Post-run Earliest date: ' || post_earliest_date || ' Post-run last date of all rows: ' || post_last_date );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Time taken: ' || l_diff || ' seconds' );

  DBMS_OUTPUT.put_line ('');

   END IF;
  -- re-enable all triggers previously disabled
--  EXECUTE IMMEDIATE ('ALTER TABLE EXISS.EXI_ITEM_INBOUND_PROPERTIES ENABLE CONSTRAINTS ITEM_ID_FK');

END;
/

