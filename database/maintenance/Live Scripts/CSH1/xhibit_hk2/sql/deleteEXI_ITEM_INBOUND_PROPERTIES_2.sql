/* deleteEXI_ITEM_INBOUND_PROPERTIES.sql                                         */
/* Deletes after running EXI_ITEM_INBOUND					 */

DECLARE
  input_days INTEGER :='&1';
  rows_before INTEGER;
  rows_after INTEGER;

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
--  select count(*) into rows_before from EXISS.EXI_ITEM_INBOUND_PROPERTIES exi;

  --delete statement
 BEGIN
--  select count(*) into rows_to_delete from EXISS.EXI_ITEM_INBOUND_PROPERTIES exi where not exists(select exx.date_created from EXISS.EXI_ITEM_INBOUND exx where exx.item_id=exi.item_id);
  
  delete from EXISS.EXI_ITEM_INBOUND_PROPERTIES exi where not exists(select exx.date_created from EXISS.EXI_ITEM_INBOUND exx where exx.item_id=exi.item_id);
   actual_rows_deleted := SQL%ROWCOUNT;
   DBMS_OUTPUT.put_line ('Rows to be deleted: ' || rows_to_delete);
   DBMS_OUTPUT.put_line ('Rows deleted:' || actual_rows_deleted);
  END;

  --generate stats info
   
  -- count of rows after
 -- select count(*) into rows_after from EXISS.EXI_ITEM_INBOUND_PROPERTIES exi;
 
  -- get timings
  l_end_time := DBMS_UTILITY.get_time;
  l_diff := (l_end_time - l_start_time)/100 ;


  DBMS_OUTPUT.put_line ('Input days: ' || input_days );
 DBMS_OUTPUT.put_line ('Rows before: ' || rows_before || ' Rows after: ' || rows_after );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Time taken: ' || l_diff || ' seconds' );

  DBMS_OUTPUT.put_line ('');


  -- re-enable all triggers previously disabled
  -- EXECUTE IMMEDIATE ('ALTER TABLE xxxx ENABLE CONSTRAINTS xxxx_FK');
END;
/
