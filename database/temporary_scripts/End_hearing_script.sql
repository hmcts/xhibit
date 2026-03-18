set pagesize 9999;
set feedback on;
set heading off;
set lines 300;
spool tmp.sql;
SELECT 'spool ./logs/endhearings_' || to_char(sysdate, 'DDMMYYYYHH24MISS') || '.txt' FROM dual;
spool off;

@tmp.sql

set heading on;
variable val1 VARCHAR2(21);
variable val2 VARCHAR2(10);
execute :val1 := to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
execute :val2 := to_char(sysdate, 'DD/MM/YYYY');

column CASE_ID format 99999;
column CASE format a10;
column HEARING_ID format 99999;
column TYPE format a5;
column OLD_HEARING_END_TIME format a21;
column SH_ID format 99999;
column OLD_SH_START_TIME format a21;
column NEW_TIME format a21;

/*
Selects the information to display in the file produced by the script. 
Contains details of the records updated with old and new values.
*/
SELECT DISTINCT 
	hrg.case_id CASE_ID, 
	(cse.case_type || cse.case_number) as CASE, 
	hrg.hearing_id as HEARING_ID, 
	ref_hrg.HEARING_TYPE_code as TYPE, 
	nvl(to_char(hrg.hearing_end_date, 'DD/MM/YYYY HH24:MI:SS'), hrg.hearing_end_date) as OLD_HEARING_END_TIME,
 	sched_hrg.scheduled_hearing_id as SH_ID,
	nvl(to_char(sched_hrg.start_time, 'DD/MM/YYYY HH24:MI:SS'), sched_hrg.start_time) as OLD_SH_START_TIME,
	:val1 AS NEW_TIME	
FROM XHB_HEARING hrg, XHB_CASE cse, XHB_REF_HEARING_TYPE ref_hrg, XHB_SCHEDULED_HEARING sched_hrg
WHERE hrg.case_id = cse.case_id AND hrg.ref_hearing_type_id = ref_hrg.ref_hearing_type_id AND hrg.hearing_id = sched_hrg.hearing_id
	  AND hrg.hearing_id IN(
		SELECT hearing_id FROM XHB_SCHEDULED_HEARING 
		WHERE start_time IS NULL AND to_char(not_before_time, 'DD/MM/YYYY') = :val2
);

/*
Updates the end date for the hearing
*/
UPDATE XHB_HEARING 
SET hearing_end_date = to_date(:val1, 'DD/MM/YYYY HH24:MI:SS')
WHERE hearing_id IN(
	SELECT hearing_id FROM XHB_SCHEDULED_HEARING 
	WHERE start_time IS NULL AND to_char(not_before_time, 'DD/MM/YYYY') = :val2
);

/*
Sets the start date for the scheduled hearing so that this will not be 
processed again next time the script is run
*/
UPDATE XHB_SCHEDULED_HEARING
SET START_TIME = to_date(:val1, 'DD/MM/YYYY HH24:MI:SS')
WHERE START_TIME IS NULL AND to_char(not_before_time, 'DD/MM/YYYY') = :val2;


spool off;








