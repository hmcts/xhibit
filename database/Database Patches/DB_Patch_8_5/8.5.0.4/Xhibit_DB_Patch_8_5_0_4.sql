/*
 * Filename:    Xhibit_DB_Patch_8_5_0_4.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE		WHO		COMMENT
 * ----         ---     	-------
 *  09/11/2011	B Hingston	Changes for DB release 8.5.0.4
 *  27/09-2011	S Atwell	Amend XHB_SEARCH_PKG for updated ref advocate query
 *
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_5_0_4_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename



/* SQL to update existing authorised defendants for PR6707 */

update xhb_defendant_on_case set amended_Date_exported = date_exported where date_exported is not null;

COMMIT;

/* Alter the XHB_SEARCH_PKG for the updated ref advocate query that was causing serious database inefficienies - rewritten by Tony Berrington */
@xhb_search_pkg

spool off


