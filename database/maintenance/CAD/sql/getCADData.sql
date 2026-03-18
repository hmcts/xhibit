set termout off;
spool &1
select max(dmi_cad_run_log_id) from xhb_dmi_cad_run_log;
exit;
