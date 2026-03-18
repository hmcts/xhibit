/* deleteXHB_VALIDATION_CJI.sql                                                  */
/* Deletes the number of ROWS                                                    */

DECLARE

  input_rows INTEGER :='&1';
  rows_before INTEGER;
  rows_after INTEGER;
  rows_before_exi INTEGER;
  rows_after_exi INTEGER;

  minx INTEGER;
  maxx INTEGER;

  earliest_date DATE;
  last_date DATE;
  post_earliest_date DATE;
  post_last_date DATE;
  diff_rows INTEGER;

  rows_to_delete INTEGER;
  rows_to_delete_exi INTEGER;
  actual_rows_deleted INTEGER;
  actual_rows_deleted_exi INTEGER;
  min_rows_to_keep INTEGER;

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
--  select count(*) into rows_before_exi from XHB_VALIDATION_EXISS exi;
--  select min(cji.creation_date) into earliest_date from XHB_VALIDATION_CJI cji;
--  select max(cji.creation_date) into last_date from XHB_VALIDATION_CJI cji;
  select count(*) into min_rows_to_keep from XHB_VALIDATION_CJI cji where cji.creation_date<=(sysdate-7);


  IF (min_rows_to_keep) < (input_rows)
  THEN
     diff_rows := (min_rows_to_keep);
     DBMS_OUTPUT.put_line ('Number of rows too many for deletion, rows available:'|| (diff_rows));
     DBMS_OUTPUT.put_line ('Earliest date: ' || earliest_date || ' Last date: ' || last_date );
  ELSE

     DBMS_OUTPUT.put_line ('Rows available: ' || min_rows_to_keep );
  BEGIN

  --delete statement
   select min(validation_id) into minx from (select cji.creation_date,cji.validation_id from XHB_VALIDATION_CJI cji order by cji.creation_date ASC) where rownum<=input_rows;

   select max(validation_id) into maxx from (select cji.creation_date,cji.validation_id from XHB_VALIDATION_CJI cji order by cji.creation_date ASC) where rownum<=input_rows;
   delete from XHB_VALIDATION_EXISS exi where exists (select cji.validation_id from xhb_validation_cji cji where cji.validation_id>=minx and cji.validation_id<=maxx and exi.validation_id=cji.validation_id);
   
   actual_rows_deleted_exi := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows deleted (exi):' || actual_rows_deleted_exi);
   delete from XHB_VALIDATION_CJI cji where cji.validation_id>=minx and cji.validation_id<=maxx;

   actual_rows_deleted := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows deleted (cji):' || actual_rows_deleted);
   DBMS_OUTPUT.put_line ('Min validation_id: ' || minx || ' Max validation_id: ' || maxx);
  END;


  --generate stats info


  -- count of rows after
 -- select count(*) into rows_after from XHB_VALIDATION_CJI cji;
 -- select count(*) into rows_after_exi from XHB_VALIDATION_EXISS exi;
--  select min(cji.creation_date) into post_earliest_date from XHB_VALIDATION_CJI cji;
--  select max(cji.creation_date) into post_last_date from XHB_VALIDATION_CJI cji;
  -- get timings
  l_end_time := DBMS_UTILITY.get_time;
  l_diff := (l_end_time - l_start_time)/100 ;
  diff_rows := sysdate-last_date;


  DBMS_OUTPUT.put_line ('Input rows: ' || input_rows );
  DBMS_OUTPUT.put_line ('Rows before (cji): ' || rows_before || ' Rows after (cji): ' || rows_after );
  DBMS_OUTPUT.put_line ('Rows before (exi): ' || rows_before_exi || ' Rows after (exi): ' || rows_after_exi);
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
