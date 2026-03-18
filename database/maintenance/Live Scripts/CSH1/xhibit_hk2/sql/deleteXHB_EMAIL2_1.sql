/* deleteXHB_EMAIL2.sql                                                          */
/* Deletes the number of DAYS							 */

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
  --EXECUTE IMMEDIATE ('ALTER TABLE xxxx DISABLE CONSTRAINTS xxxxx_FK');

  -- count of rows at start
--  select count(*) into rows_before from XHB_EMAIL2 xhb;
  select min(xhb.creation_date) into earliest_date from XHB_EMAIL2 xhb;
--  select max(xhb.creation_date) into last_date from XHB_EMAIL2 xhb;
 
  IF (sysdate-7) < (earliest_date+input_days) 
  THEN
     diff_days := input_days-(sysdate-7-last_date); 
     DBMS_OUTPUT.put_line ('Number of days too many for deletion');
     DBMS_OUTPUT.put_line ('Earliest date: ' || earliest_date || ' Last date: ' || last_date );
  ELSE
  --delete statement
 BEGIN
-- select count(*) into rows_to_delete from XHB_EMAIL2 xhb where xhb.creation_date>=earliest_date and xhb.creation_date<=(earliest_date+input_days-1); 
  
   delete from XHB_EMAIL2 where creation_date>=earliest_date and creation_date<=(earliest_date+input_days-1);
   actual_rows_deleted := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows deleted:' || actual_rows_deleted);
  END;
  
  --generate stats info
  
  -- count of rows after
--  select count(*) into rows_after from XHB_EMAIL2 xhb;
--  select min(xhb.creation_date) into post_earliest_date from XHB_EMAIL2 xhb;
--  select max(xhb.creation_date) into post_last_date from XHB_EMAIL2 xhb;
  -- get timings
  l_end_time := DBMS_UTILITY.get_time;
  l_diff := (l_end_time - l_start_time)/100 ;
  diff_days := sysdate-last_date;
  
 
  DBMS_OUTPUT.put_line ('Input days: ' || input_days );
  DBMS_OUTPUT.put_line ('Rows to delete: ' || rows_to_delete );
  DBMS_OUTPUT.put_line ('Rows before: ' || rows_before || ' Rows after: ' || rows_after );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Pre-run earliest date: ' || earliest_date || ' Pre-run latest date of all rows: ' || last_date );
  DBMS_OUTPUT.put_line ('Post-run Earliest date: ' || post_earliest_date || ' Post-run last date of all rows: ' || post_last_date );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Time taken: ' || l_diff || ' seconds' );
  
  DBMS_OUTPUT.put_line ('');
  
   END IF;
  -- re-enable all triggers previously disabled
  -- EXECUTE IMMEDIATE ('ALTER TABLE xxxx ENABLE CONSTRAINTS xxxx_FK');
END;
/

