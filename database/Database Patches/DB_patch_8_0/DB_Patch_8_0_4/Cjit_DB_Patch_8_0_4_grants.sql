/*
 * Filename:    Cjit_DB_Patch_8_0_4_grants.sql
 *
 * Must login with system dba privs to run
 * this script.
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 *  13/10/2006	K Shah			Changes for release 8.0.4
 */ 


/*
 * Require system dba privileges for grants
 */

conn /as sysdba

@$ORACLE_HOME/rdbms/admin/xaview.sql
grant select on v$xatrans$ to cjit; 
grant select on pending_trans$ to public; 
grant select on dba_2pc_pending to public; 
grant select on dba_pending_transactions to public; 
grant execute on dbms_system to cjit;

 

/*
 * Login as user cjit again
 */

conn cjit/cjit
