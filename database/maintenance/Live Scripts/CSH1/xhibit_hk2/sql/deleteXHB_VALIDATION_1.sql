/* deleteXHB_VALIDATION.sql         								   */
/* deletes from the XHB_VALIDATION table after running XHB_VALIDATION_CJI AND XHB_VALIDATION_EXISS */

DECLARE

  rows_before INTEGER;
  rows_before_both INTEGER;
  rows_before_cji INTEGER;
  rows_before_exi INTEGER;
  
  rows_after INTEGER;
  rows_after_cji INTEGER;
  rows_after_exi INTEGER;

  rows_to_delete INTEGER;
  rows_to_delete_cji INTEGER;
  rows_to_delete_exi INTEGER;
  
  actual_rows_deleted INTEGER;
  actual_rows_deleted_both INTEGER;
  actual_rows_deleted_cji INTEGER;
  actual_rows_deleted_exi INTEGER;
  actual_rows_deleted_orphans INTEGER;

  l_start_time NUMBER;
  l_end_time NUMBER;
  l_diff NUMBER;

BEGIN

  l_start_time := DBMS_UTILITY.get_time;
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_CJI DISABLE CONSTRAINTS XHB_VALIDATION_CJI');
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_EXISS DISABLE CONSTRAINTS XHB_VALIDATION_EXISS');

  -- count of rows at start
--  select count(*) into rows_before from xhb_validation xv;


  --delete statements
 BEGIN
 -- delete from both first
	delete from xhb_validation xv where not exists (select 1 from xhb_validation_cji cji where xv.validation_id=cji.validation_id) and not exists (select 1 from xhb_validation_exiss exi where xv.validation_id=exi.validation_id); 

	actual_rows_deleted_both := SQL%ROWCOUNT;
	DBMS_OUTPUT.put_line ('Rows deleted relating to xhb_validation_cji and xhb_validation_exiss:' || actual_rows_deleted_both);
  
 END;

  -- count of rows after
--  select count(*) into rows_after from xhb_validation xv;

  -- get timings
  l_end_time := DBMS_UTILITY.get_time;
  l_diff := (l_end_time - l_start_time)/100;

  actual_rows_deleted := (actual_rows_deleted_both);
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Rows before (xv): ' || rows_before || ' Rows after (xv): ' || rows_after );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Time taken: ' || l_diff || ' seconds' );

  DBMS_OUTPUT.put_line ('');

  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_CJI ENABLE CONSTRAINTS XHB_VALIDATION_CJI');
  EXECUTE IMMEDIATE ('ALTER TABLE XHB_VALIDATION_EXISS ENABLE CONSTRAINTS XHB_VALIDATION_EXISS');

END;
/
