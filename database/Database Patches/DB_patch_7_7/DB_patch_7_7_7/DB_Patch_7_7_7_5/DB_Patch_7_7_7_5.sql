/*
 * Filename:    DB_Patch_7_7_7_5.sql
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
   10/10/2006   K Shah			Changes for release 7.7.7.5
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'DBPATCH_7_7_7_5_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 */

/*
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



/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */

CREATE OR REPLACE TRIGGER XHIBIT.XHB_EMAIL2_UPD_XHB_EMAIL_TR 
AFTER INSERT ON XHB_EMAIL
FOR EACH ROW
DECLARE

v_recipients   CLOB;
v_recipient VARCHAR2(255);
v_recipient2 VARCHAR2(255);
v_email_protocol VARCHAR2(1) := 'M';
v_status 	VARCHAR2(1) := 'R';
v_EMAIL2_ID     NUMBER;

BEGIN


v_recipients := dbms_lob.substr (:new.recipients, 254, 1);
v_recipient := to_char(v_recipients);


v_email2_id := :new.mail_id;

INSERT INTO XHB_EMAIL2
	(
	MAIL_ID,
	RECIPIENT,
	SUBJECT,
	SENDER,
	STATUS,
	CREATION_TIME,
	MUSTRECEIVE,
	REJECTTIME,
	REASON,
	COURT_ID,
	EMAIL_TO,
	COMPANY,
	ATTACHMENT,
	MIME_TYPE,
	MIME_BODY_BLOB_ID,
	EMAIL_PROTOCOL
	)
	values
	(
	v_email2_id,
	v_recipient,
	:new.SUBJECT,
	:new.SENDER,
	v_status,
	:new.CREATION_TIME,
	:new.MUSTRECEIVE,
	:new.REJECTTIME,
	:new.REASON,
	:new.COURT_ID,
	:new.EMAIL_TO,
	:new.COMPANY,
	:new.ATTACHMENT,
	:new.MIME_TYPE,
	:new.MIME_BODY_BLOB_ID,
 	v_email_protocol
	);


Exception when others then null;

END;
/

show errors;




CREATE OR REPLACE TRIGGER XHIBIT.XHB_EMAIL2_UPD_XHB_EMAIL2_TR 
before update ON XHB_EMAIL2
FOR EACH ROW
DECLARE

v_recipient VARCHAR2(255);
v_recipient2 VARCHAR2(255);
v_email_protocol VARCHAR2(1) := 'M';
v_status 	VARCHAR2(1) := 'R';
v_EMAIL2_ID     NUMBER;

BEGIN

v_email2_id := :old.mail_id;

IF :old.status = 'E' and :old.recipient is null then

select to_char(dbms_lob.substr (xhb_email.recipients, 254, 1))
into v_recipient2
from xhb_email
where xhb_email.mail_id = v_email2_id;

select v_recipient2
into :new.recipient
from dual;


END IF;

dbms_output.put_line ('email_id = '|| v_email2_id||' and recipient = '|| v_recipient2);

END;

/

show errors;



/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes, additions or deletion of data
 */



/*
 * Updating of table XHB_VERSION
 */

update xhb_version
set schema_version = '7.7.7.5',
last_UPDATE_DATE = sysdate
where display_name = 'Database';

update xhb_version
set schema_version = '7.7.7.5',
last_UPDATE_DATE = sysdate
where display_name = 'Mercator';


COMMIT;

spool off
