set linesize 32767
set long 2000000000
set trimout on
set trimspool on
set heading off
set pagesize 0
set longchunksize 90000
column txt format a121 word_wrapped
spool &1

SELECT clob_data FROM xhb_clob WHERE clob_id IN (
	SELECT SUBSTR(log_message,instr(log_message,'CLOB_ID')+27) CLOB_ID
	FROM   xhb_dmi_cad_run_log
	WHERE  log_message LIKE '%CLOB%'
	AND dmi_cad_run_history_id = (SELECT MAX(dmi_cad_run_history_id) 
				      FROM xhb_dmi_cad_run_history))
ORDER BY clob_id ASC;

spool off
exit;
