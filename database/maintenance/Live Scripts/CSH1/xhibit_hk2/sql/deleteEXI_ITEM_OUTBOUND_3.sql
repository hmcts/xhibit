/* deleteEXISS.EXI_ITEM_OUTBOUND.sql                                                   */
/* Deletes everything prior to the number of RETAIN_DAYS entered		       */

DECLARE
  input_days INTEGER :='&1';
  rows_before INTEGER;
  rows_after INTEGER;
  rows_before_track INTEGER;
  rows_after_track INTEGER;


  earliest_date DATE;
  last_date DATE;
  post_earliest_date DATE;
  post_last_date DATE;
  diff_days INTEGER;

  rows_to_delete INTEGER;
  rows_to_delete_track INTEGER;
  actual_rows_deleted INTEGER;
  actual_rows_deleted_track INTEGER;

  l_start_time NUMBER;
  l_end_time NUMBER;
  l_diff NUMBER;

BEGIN

  l_start_time := DBMS_UTILITY.get_time;

  -- disable any triggers
   EXECUTE IMMEDIATE ('ALTER TABLE EXISS.EXI_ITEM_OUTBOUND_TRACKING DISABLE CONSTRAINTS ITEM_OUTB_TRACKING_ITEM_ID_FK');
   EXECUTE IMMEDIATE ('ALTER TABLE EXISS.EXI_JMS_MESSAGE DISABLE CONSTRAINTS JMS_MESSAGE_ITEM_ID_FK');

  -- count of rows at start
--  select count(exi.item_id) into rows_before from EXISS.EXI_ITEM_OUTBOUND exi;
--  select count(txi.item_id) into rows_before_track from  EXI_ITEM_OUTBOUND_TRACKING txi;
  select min(exi.item_created) into earliest_date from EXISS.EXI_ITEM_OUTBOUND exi;
--  select max(exi.item_created) into last_date from EXISS.EXI_ITEM_OUTBOUND exi;
 
  IF (sysdate-7-input_days) < (earliest_date) 
  THEN
     diff_days := input_days-(sysdate-7-last_date); 
     DBMS_OUTPUT.put_line ('Number of days too many for for deletion in range');
     DBMS_OUTPUT.put_line ('Earliest date: ' || earliest_date || ' Last date: ' || last_date );
  ELSE
  
  --delete statement
  BEGIN
 --   select count(item_created) into rows_to_delete from EXISS.EXI_ITEM_OUTBOUND exi where exi.item_created<=(sysdate-7-input_days); 

   delete from EXISS.EXI_ITEM_OUTBOUND exi where exi.item_created<=(sysdate-7-input_days);
   actual_rows_deleted := SQL%ROWCOUNT;

  -- select count(item_id) into rows_to_delete_track from EXI_ITEM_OUTBOUND_TRACKING exi where not exists (select item_id from exi_item_outbound exx where exi.item_id=exx.item_id);

   delete from EXI_ITEM_OUTBOUND_TRACKING exi where not exists (select item_id from exi_item_outbound exx where exi.item_id=exx.item_id);
   actual_rows_deleted_track := SQL%ROWCOUNT;


   DBMS_OUTPUT.put_line ('Rows deleted:' || actual_rows_deleted);
   DBMS_OUTPUT.put_line ('Rows deleted (outbound_tracking):' || actual_rows_deleted_track);
  END;
  
  --generate stats info

  
  -- count of rows after
  --select count(exi.item_id) into rows_after from EXISS.EXI_ITEM_OUTBOUND exi;
  --select count(exi.item_id) into rows_after_track from EXISS.EXI_ITEM_OUTBOUND_TRACKING txi;
  --select min(exi.item_created) into post_earliest_date from EXISS.EXI_ITEM_OUTBOUND exi;
  --select max(exi.item_created) into post_last_date from EXISS.EXI_ITEM_OUTBOUND exi;
  -- get timings
  l_end_time := DBMS_UTILITY.get_time;
  l_diff := (l_end_time - l_start_time)/100 ;
  diff_days := sysdate-last_date;
  
  DBMS_OUTPUT.put_line (''); 
  DBMS_OUTPUT.put_line ('Input days: ' || input_days );
  DBMS_OUTPUT.put_line ('Rows to delete: ' || rows_to_delete );
  DBMS_OUTPUT.put_line ('Rows to delete (outbound_tracking): ' || rows_to_delete_track );
  DBMS_OUTPUT.put_line ('Rows before: ' || rows_before || ' Rows after: ' || rows_after );
  DBMS_OUTPUT.put_line ('Rows before (outbound_tracking): ' || rows_before_track || ' Rows after (outbound_tracking): ' || rows_after_track);
  DBMS_OUTPUT.put_line ('Deleting range:' || earliest_date || ' to ' || (sysdate-7-input_days) );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Pre-run earliest date: ' || earliest_date || ' Pre-run latest date of all rows: ' || last_date );
  DBMS_OUTPUT.put_line ('Post-run Earliest date: ' || post_earliest_date || ' Post-run last date of all rows: ' || post_last_date );
  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Time taken: ' || l_diff || ' seconds' );
  
  DBMS_OUTPUT.put_line ('');
  
   END IF;
  -- re-enable all triggers previously disabled
   EXECUTE IMMEDIATE ('ALTER TABLE EXISS.EXI_ITEM_OUTBOUND_TRACKING ENABLE CONSTRAINTS ITEM_OUTB_TRACKING_ITEM_ID_FK');
   EXECUTE IMMEDIATE ('ALTER TABLE EXISS.EXI_JMS_MESSAGE ENABLE CONSTRAINTS JMS_MESSAGE_ITEM_ID_FK');
END;
/

