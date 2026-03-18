/*
 * Filename:    Xhibit_DB_Patch_8_7_0_PreGoLive.sql (CREST to XHIBIT functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE         WHO             COMMENT
 * ----         ---             -------
 * 07-12-2018   SA              Setup of all the files required for the GoLive. This is primarily creating (but not running) a bunch of stored procedures.
 * 12-12-2018   Mark Harris     ctx-2474 - Add xhb_update_def_hearing_record
 * 13-12-2018	David Burden	CTX-3332 - Added load_xhb_ref_hearing_types procedure
 * 15-01-2019   John Riley      CTX-3483 = Added xhb_update_ref_system_code procedure
 * 20-12-2018   J Riley         CTX-3420 - Changed call to xhb_populate_ref_calendar to call xhb_populate_ref_calendar_all
 * 03-07-2019	Chris Vincent	CTX-4302 - Added xhb_update_xhb_charge procedure
 * 05-07-2019	Chris Vincent	CTX-4383 - Added xhb_pop_xhb_offence_crest_seq procedure
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_PreGoLive'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

@@update_court_police_force_data_LIVE.sql;
@@xhb_exporta_aiu_tr.sql;
@@xhb_populate_pub_running_list.sql;
@@xhb_update_xhb_defendant.sql;
@@xhb_update_xhb_case.sql;
@@xhb_update_no_def.sql;
@@xhb_update_xhb_offence.sql;
@@xhb_populate_ref_calendar_all.sql;
@@xhb_update_def_hearing_record.sql;
@@load_xhb_ref_hearing_types.sql;
@@xhb_update_ref_system_code.sql; 
@@xhb_ref_system_code_alter.sql;
@@xhb_update_xhb_charge.sql;
@@xhb_pop_xhb_offence_crest_seq.sql;

COMMIT;

spool off
