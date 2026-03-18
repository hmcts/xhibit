/*
 * Filename:    DB_Patch_7_7_4.sql
 *
 * System:      Preproduction, Production
 *
 * Date:        17 January 2006
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 17/01/2006	K Shah			Database changes made to comprise release 7.7.4
 * 26/01/2006   K Shah			Changed create script for audit table
 */


/*
 *  Create audit table
 */

CREATE TABLE AUD_MTBL_MERC_DL_STORAGE
(
  NAME              VARCHAR2(64 BYTE),
  ADDRESS           VARCHAR2(64 BYTE),
  TOID              VARCHAR2(8 BYTE),
  UNIQUEMSGID       VARCHAR2(64 BYTE),
  MSGTYPE           VARCHAR2(32 BYTE),
  MSGTIME           VARCHAR2(16 BYTE),
  MSGDATE           DATE,
  ADDITIONAL1       VARCHAR2(128 BYTE),
  ADDITIONAL2       VARCHAR2(128 BYTE),
  ADDITIONAL3       VARCHAR2(128 BYTE),
  ADDITIONAL4       VARCHAR2(128 BYTE),
  FROMID            VARCHAR2(8 BYTE),
  LAST_UPDATE_DATE  DATE DEFAULT SYSDATE,
  INSERT_EVENT      VARCHAR2(1) NOT NULL
)
tablespace AUDITD;

CREATE PUBLIC SYNONYM AUD_MTBL_MERC_DL_STORAGE FOR XHIBIT.AUD_MTBL_MERC_DL_STORAGE;

GRANT DELETE, INSERT, SELECT, UPDATE ON  XHIBIT.AUD_MTBL_MERC_DL_STORAGE TO PUBLIC;



/*
 * Create update delete trigger 
 */

CREATE OR REPLACE TRIGGER  MTBL_MERC_DL_STORAGE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON  MTBL_MERC_DL_STORAGE
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN
    l_trig_event := 'U';
  ELSE -- Must be DELETING
    l_trig_event := 'D';
  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('MTBL_MERC_DL_STORAGE') = 1) THEN

    INSERT INTO AUD_MTBL_MERC_DL_STORAGE(NAME, ADDRESS, TOID, UNIQUEMSGID, MSGTYPE, MSGTIME, MSGDATE, ADDITIONAL1, ADDITIONAL2,
				ADDITIONAL3, ADDITIONAL4, FROMID, INSERT_EVENT)
    VALUES (:old.NAME,
            :old.ADDRESS,
            :old.TOID,
            :old.UNIQUEMSGID,
            :old.MSGTYPE,
            :old.MSGTIME,
            :old.MSGDATE,
            :old.ADDITIONAL1,
            :old.ADDITIONAL2,
            :old.ADDITIONAL3,
            :old.ADDITIONAL4,
            :old.FROMID,
            l_trig_event);

  END IF;

END;
/

show errors 


/*
 * Update data to enable populating audit table
 */

insert into  xhb_sys_audit
values (XHB_SYS_AUDIT_SEQ.NEXTVAL, 'MTBL_MERC_DL_STORAGE','AUD_MTBL_MERC_DL_STORAGE','Y');


/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '7.7', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '7.7.3', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '7.7.4', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.7.4', sysdate, 'RELEASE', 'Mercator', 4); 

