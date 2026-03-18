SET ECHO OFF
SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF
/*
---------------------------------------------------------------
EXISS_GET_OUTBOUND_FAILURES
This script gets the first 100 failures in the last 30 days and for
each failed message, outputs the last record in EXI_ITEM_OUTBOUND_TRACKING
so that the user can establish where the problem was and what the best way to 
re-send is.
---------------------------------------------------------------
*/
declare
results SYS_REFCURSOR;
tracking_results SYS_REFCURSOR;
the_item_id EXI_ITEM_OUTBOUND.Item_Id%TYPE;
the_type_id EXI_ITEM_OUTBOUND.TYPE_ID%TYPE;
the_item_created EXI_ITEM_OUTBOUND.Item_created%TYPE;

tracking_text EXI_REF_TRACKING_STATUS.INTERNAL_NAME%TYPE;
tracking_date varchar(20);

min_value EXI_ITEM_OUTBOUND.ITEM_ID%TYPE;

begin

--Get min ITEM_ID for this time frame (item_id is indexed so easier to query by)
OPEN RESULTS FOR
select min(item_id) 
from exi_item_outbound 
where ITEM_CREATED > (SYSDATE - 5);
FETCH RESULTS INTO MIN_VALUE;

--Get the first 100 failures
open results for 
select T.ITEM_ID,T.TYPE_ID,T.ITEM_CREATED From exi_item_outbound T where item_id in (
SELECT ITEM_ID FROM EXI_ITEM_OUTBOUND WHERE ITEM_ID > MIN_VALUE
MINUS
SELECT ITEM_ID FROM EXI_ITEM_OUTBOUND_TRACKING WHERE ITEM_ID > MIN_VALUE 
AND STATUS_ID = 8) 
and item_created < (sysdate - (2/24))
and type_id not in (6,7,8,9,14,16,17,18,19,22,410,425,426,427,428,429)
AND ROWNUM <= 100
ORDER BY ITEM_ID;

dbms_output.put_line('first 100 failed items with most recent tracking record');

--Loop through all failures
LOOP
       fetch results into the_item_id,the_type_id,the_item_created;
       exit when results%NOTFOUND;

       --Get the most recent tracking item for info
       OPEN tracking_results for
       select s.internal_name,to_char(t.tracking_date,'DD-MON-YYY HH24:MI') 
       from exi_item_outbound_tracking t,exi_ref_tracking_status s 
       where t.status_id = s.status_id
       and t.item_id = the_item_id 
       order by tracking_date desc;
       
       fetch tracking_results into tracking_text,tracking_date;
       
       --Output result
       dbms_output.put_line(the_item_id||' ('||the_item_created||') Latest tracking item: '||tracking_text||' @ '||tracking_date);
END LOOP;
end;

/
exit;
