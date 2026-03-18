/*
 * Filename:    Xhibit_DB_Patch_8_0_3_2.sql
 *
 * Must login with system dba privs to run
 * this script.
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 *  03/08/2006	PG			Changes for release 8.0.3.2
 *  04/08/2006	C Ranaweera		Delete from XHB_VERSION changes. 
*/ 

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_0_3_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


@$ORACLE_HOME/rdbms/admin/xaview.sql
grant select on v$xatrans$ to xhibit ; 
grant select on pending_trans$ to public; 
grant select on dba_2pc_pending to public; 
grant select on dba_pending_transactions to public; 
grant execute on dbms_system to xhibit;

 

/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHIBIT.XHB_VERSION WHERE SCHEMA_NAME = 'XHIBIT';

INSERT INTO XHIBIT.XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.0.3.2', sysdate , 'RELEASE', 'Database', 3); 

COMMIT;

spool off
