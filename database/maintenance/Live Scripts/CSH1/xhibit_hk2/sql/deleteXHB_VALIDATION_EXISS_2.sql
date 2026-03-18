/* deleteXHB_VALIDATION_EXISS.sql                                                */
/* Deletes the number of ROWS                                                    */

DECLARE

  input_rows INTEGER :='&1';
  rows_before INTEGER;
  rows_after INTEGER;
  rows_before_cji INTEGER;
  rows_after_cji INTEGER;

  minx INTEGER;
  maxx INTEGER;

  earliest_date DATE;
  last_date DATE;
  post_earliest_date DATE;
  post_last_date DATE;
  diff_rows INTEGER;

  rows_to_delete INTEGER;
  rows_to_delete_cji INTEGER;
  actual_rows_deleted INTEGER;
  actual_rows_deleted_cji INTEGER;
  min_rows_to_keep INTEGER;

  l_start_time NUMBER;
  l_end_time NUMBER;
  l_diff NUMBER;

BEGIN

  l_start_time := DBMS_UTILITY.get_time;

  -- disable any triggers
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_EXISS DISABLE CONSTRAINTS XHB_VALIDATION_EXISS');
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_CJI DISABLE CONSTRAINTS XHB_VALIDATION_CJI');

  -- count of rows at start
--  select count(*) into rows_before from XHB_VALIDATION_EXISS exi;
--  select count(*) into rows_before_cji from XHB_VALIDATION_CJI cji;
--  select min(exi.item_created) into earliest_date from XHB_VALIDATION_EXISS exi;
--  select max(exi.item_created) into last_date from XHB_VALIDATION_EXISS exi;
  select count(*) into min_rows_to_keep from XHB_VALIDATION_EXISS exi where exi.item_created<=(sysdate-7);


  IF (min_rows_to_keep) < (input_rows)
  THEN
     diff_rows := (min_rows_to_keep);
     DBMS_OUTPUT.put_line ('Number of rows too many for deletion, rows available:'|| (diff_rows));
     DBMS_OUTPUT.put_line ('Earliest date: ' || earliest_date || ' Last date: ' || last_date );
  ELSE

     DBMS_OUTPUT.put_line ('Rows available: ' || min_rows_to_keep );
  BEGIN

  --delete statement
   select min(validation_id) into minx from (select exi.item_created,exi.validation_id from XHB_VALIDATION_EXISS exi order by exi.item_created ASC) where rownum<=input_rows;

   select max(validation_id) into maxx from (select exi.item_created,exi.validation_id from XHB_VALIDATION_EXISS exi order by exi.item_created ASC) where rownum<=input_rows;

   delete from XHB_VALIDATION_CJI cji where exists (select exi.validation_id from xhb_validation_exiss exi where exi.validation_id>=minx and exi.validation_id<=maxx and exi.validation_id=cji.validation_id);
   actual_rows_deleted_cji := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line('Rows deleted (cji): ' || actual_rows_deleted_cji);   
   
   delete from XHB_VALIDATION_EXISS exi where exi.validation_id>=minx and exi.validation_id<=maxx;

   actual_rows_deleted := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows deleted (exi):' || actual_rows_deleted);
   DBMS_OUTPUT.put_line ('Min validation_id: ' || minx || ' Max validation_id: ' || maxx);
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
  diff_rows := sysdate-last_date;


  DBMS_OUTPUT.put_line ('Input rows: ' || input_rows );
  DBMS_OUTPUT.put_line ('Rows before (cji): ' || rows_before_cji || ' Rows after (cji): ' || rows_after_cji);
  DBMS_OUTPUT.put_line ('Rows before: ' || rows_before || ' Rows after: ' || rows_after );
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
