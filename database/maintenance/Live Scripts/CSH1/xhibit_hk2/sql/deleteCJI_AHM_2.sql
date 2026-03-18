/* deleteCJI_AHM.sql 	                                                         */
/* Deletes the number of ROWS 							 */

DECLARE

  input_rows INTEGER :='&1';
  rows_before INTEGER;
  rows_after INTEGER;
  
  minx INTEGER;
  maxx INTEGER;

  earliest_date DATE;
  last_date DATE;
  post_earliest_date DATE;
  post_last_date DATE;
  diff_rows INTEGER;

  rows_to_delete INTEGER;
  actual_rows_deleted INTEGER;
  min_rows_to_keep INTEGER;

  l_start_time NUMBER;
  l_end_time NUMBER;
  l_diff NUMBER;

BEGIN

  l_start_time := DBMS_UTILITY.get_time;

  -- disable any triggers
  --EXECUTE IMMEDIATE ('ALTER TABLE xxxx DISABLE CONSTRAINTS xxxxx_FK');

  -- count of rows at start
--  select count(*) into rows_before from CJI_AHM t;
 -- select min(xhb.sent_agg_date) into earliest_date from CJI_AHM xhb;
  --select max(xhb.sent_agg_date) into last_date from CJI_AHM xhb;
  select count(*) into min_rows_to_keep from CJI_AHM xhb where xhb.sent_agg_date<=(sysdate-7);
  

  IF (min_rows_to_keep) < (input_rows) 
  THEN
     diff_rows := (min_rows_to_keep);
     DBMS_OUTPUT.put_line ('Number of rows too many for deletion');
     DBMS_OUTPUT.put_line ('Earliest date: ' || earliest_date || ' Last date: ' || last_date );
  ELSE
  
     DBMS_OUTPUT.put_line ('Rows available: ' || min_rows_to_keep );
  BEGIN

  --delete statement
   select min(message_id) into minx from (select xhb.message_id from CJI_AHM xhb order by xhb.sent_agg_date ASC) where rownum<=input_rows;
  
   select max(message_id) into maxx from (select xhb.message_id from CJI_AHM xhb order by xhb.sent_agg_date ASC) where rownum<=input_rows;

   delete from CJI_AHM xhb where xhb.message_id>=minx and xhb.message_id<=maxx;
  
   actual_rows_deleted := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows deleted:' || actual_rows_deleted);
   DBMS_OUTPUT.put_line ('Min message_id: ' || minx || ' Max message_id: ' || maxx);
  END;

  
  --generate stats info
  
  
  -- count of rows after
--  select count(*) into rows_after from CJI_AHM xhb;
--  select min(xhb.sent_agg_date) into post_earliest_date from CJI_AHM xhb;
--  select max(xhb.sent_agg_date) into post_last_date from CJI_AHM xhb;
  -- get timings
  l_end_time := DBMS_UTILITY.get_time;
  l_diff := (l_end_time - l_start_time)/100 ;
  diff_rows := sysdate-last_date;
  
 
  DBMS_OUTPUT.put_line ('Input rows: ' || input_rows );
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

