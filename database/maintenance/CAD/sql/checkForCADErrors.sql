set termout off;
spool &1
select count(*) from xhb_dmi_cad_run_log where dmi_cad_run_log_id=&2 and log_message like '%ORA%';
exit;
