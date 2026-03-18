/*
 * Filename:    Xhibit_DB_Patch_8_0_4_grants.sql
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
 *  13/10/2006	Kadu Shah		Changes for release 8.0.4
*/ 


/*
 * Require system dba privileges for grants
 */

conn /as sysdba


@$ORACLE_HOME/rdbms/admin/xaview.sql
grant select on v$xatrans$ to xhibit ; 
grant select on pending_trans$ to public; 
grant select on dba_2pc_pending to public; 
grant select on dba_pending_transactions to public; 
grant execute on dbms_system to xhibit;

 

/*
 * Login as user Xhibit again
 */

conn xhibit/xhibit
