/* deleteEXI_ITEM_INBOUND.sql                                                    */
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
  EXECUTE IMMEDIATE ('ALTER TABLE EXISS.EXI_ITEM_INBOUND_PROPERTIES DISABLE CONSTRAINTS ITEM_ID_FK');

  -- count of rows at start
--  select count(exi.item_id) into rows_before from EXISS.EXI_ITEM_INBOUND exi;
  -- select min(exi.date_created) into earliest_date from EXISS.EXI_ITEM_INBOUND exi;
 -- select max(exi.date_created) into last_date from EXISS.EXI_ITEM_INBOUND exi;
  select count(exi.item_id) into min_rows_to_keep from EXISS.EXI_ITEM_INBOUND exi where exi.date_created<=(sysdate-7);


  IF (min_rows_to_keep) < (input_rows)
  THEN
     diff_rows := (min_rows_to_keep);
     DBMS_OUTPUT.put_line ('Number of rows too many for deletion, rows available:'|| (diff_rows));
     DBMS_OUTPUT.put_line ('Earliest date: ' || earliest_date || ' Last date: ' || last_date );
  ELSE

     DBMS_OUTPUT.put_line ('Rows available: ' || min_rows_to_keep );
  BEGIN

  --delete statement
   select min(item_id) into minx from (select exi.date_created,exi.item_id from EXISS.EXI_ITEM_INBOUND exi order by exi.item_id ASC) where rownum<=input_rows;

   select max(item_id) into maxx from (select exi.date_created,exi.item_id from EXISS.EXI_ITEM_INBOUND exi order by exi.item_id ASC) where rownum<=input_rows;

   delete from EXISS.EXI_ITEM_INBOUND exi where exi.item_id>=minx and exi.item_id<=maxx;

   actual_rows_deleted := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows deleted:' || actual_rows_deleted);
   DBMS_OUTPUT.put_line ('Min item_id: ' || minx || ' Max item_id: ' || maxx );
  END;


  --generate stats info


  -- count of rows after
 -- select count(exi.item_id) into rows_after from EXISS.EXI_ITEM_INBOUND exi;
 -- select min(exi.date_created) into post_earliest_date from EXISS.EXI_ITEM_INBOUND exi;
 -- select max(exi.date_created) into post_last_date from EXISS.EXI_ITEM_INBOUND exi;
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
--  EXECUTE IMMEDIATE ('ALTER TABLE EXISS.EXI_ITEM_INBOUND_PROPERTIES ENABLE CONSTRAINTS ITEM_ID_FK'); 

END;
/

