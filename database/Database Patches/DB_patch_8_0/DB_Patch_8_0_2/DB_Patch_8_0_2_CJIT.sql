/*
 * Filename:    DB_Patch_8_0_2_CJIT.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
   20/06/2006   K SHAH			database changes made to comprise 
                     			release 8.0.2
 *
 */ 
set echo on
set term off
column filename new_value spool_filename
  select 'DBPATCH_8_0_2_CJIT_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename
/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */


/*
 * Changes, additions or deletion of sequences
 */

DECLARE
     nid number;
BEGIN
     select nvl(max(device_id),0)
     into nid
     from CJIT.CJI_AHM_DEVICE_TYPE;
     nid:=nid+1;

  EXECUTE IMMEDIATE 'CREATE SEQUENCE CJIT.CJI_AHM_DEVICE_TYPE_SEQ
  START WITH '|| nid||
  ' MAXVALUE 1E27
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER';
     
END;
/
COMMIT;


GRANT SELECT ON CJIT.CJI_AHM_DEVICE_TYPE_SEQ  TO public;


DECLARE
     nid number;
BEGIN
     select nvl(max(status_id),0)
     into nid
     from CJIT.CJI_AHM_REPLY_STATUS;
     nid:=nid+1;

  EXECUTE IMMEDIATE 'CREATE SEQUENCE CJIT.CJI_AHM_REPLY_STATUS_SEQ
  START WITH '|| nid||
  ' MAXVALUE 1E27
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER';
     
END;
/
COMMIT;


GRANT SELECT ON CJIT.CJI_AHM_REPLY_STATUS_SEQ  TO public;


DECLARE
     nid number;
BEGIN
     select nvl(max(status_id),0)
     into nid
     from CJIT.CJI_AHM_STATUS;
     nid:=nid+1;

  EXECUTE IMMEDIATE 'CREATE SEQUENCE CJIT.CJI_AHM_STATUS_SEQ
  START WITH '|| nid||
  ' MAXVALUE 1E27
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER';
     
END;
/
COMMIT;


GRANT SELECT ON CJIT.CJI_AHM_STATUS_SEQ  TO public;




/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes, additions or deletion of standing data
 */


/*
 * Updating of table CJI_VERSION
 */

/*
 * Updating of table CJI_VERSION
 */
/*************************/
/* update Version No     */
/*************************/

DELETE FROM CJI_VERSION;

INSERT INTO CJI_VERSION
            (SCHEMA_NAME,SCHEMA_VERSION,LAST_UPDATE_DATE,UPDATED_BY,DISPLAY_NAME,DISPLAY_SEQ) 
VALUES ('CJSE','8_0_2',sysdate,'CJIT','CJSE Database Schema 8_0_2',1);


commit;


spool off
