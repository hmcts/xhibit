SET ECHO OFF
SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF

select count(*) from tmp_dof_ids where defendant_on_offence_id=99999999;
EXIT;
