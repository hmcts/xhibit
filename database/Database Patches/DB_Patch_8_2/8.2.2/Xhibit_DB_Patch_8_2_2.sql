/*
 * Filename:    Xhibit_DB_Patch_8_2_2.sql
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
 *  15/11/2007	D RAI			Changes for release 8.2.2
 
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_2_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename





/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 */





 /*
  * Additions or deletion of XHB_ tables, indexes and foreign keys
  */





/*
 * Changes, additions or deletion of views
 */



CREATE OR REPLACE VIEW XHB_ORIGINAL_CHARGES_CHGS_V
AS
select decode(nvl(doo.obs_ind,'N'),'Y','Y',
           decode(nvl(o.obs_ind,'N'),'Y','Y',
           decode(nvl(ro.obs_ind,'N'),'Y','Y',
           decode(nvl(c.obs_ind,'N'),'Y','Y','N')))) obsolete,
       doo.defendant_on_case_id,
       doo.defendant_on_offence_id,
       doo.seq_no,
       o.offence_id,
       o.crest_offence_freetext,
       o.crest_offence_seq_no,
       ro.ref_offence_id,
       ro.offence_desc,
       ro.offence_code,
       c.charge_id,
       c.charge_type,
       c.crest_charge_seq_no
from   xhb_defendant_on_offence    doo,
       xhb_offence                 o,
       xhb_ref_offence             ro,
       xhb_charge                  c
where  doo.offence_id           =  o.offence_id
and    o.ref_offence_id         =  ro.ref_offence_id
and    o.charge_id              =  c.charge_id;



create or replace view xhb_original_charges_defs_v as
select case.court_id,
       case.case_id,
       case.case_type,
       case.case_number,
       doc.defendant_on_case_id,
       doc.asn,
       d.defendant_id,
       d.first_name,
       d.middle_name,
       d.surname
from   xhb_case                 case,
       xhb_defendant            d,
       xhb_defendant_on_case    doc
where  case.case_id             =  doc.case_id
and    doc.defendant_id         =  d.defendant_id
and   (doc.obs_ind is null or doc.obs_ind = 'N');




/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */




/*
 * Changes, additions or deletion of sequences
 */




/*
 * Changes, additions or deletion of packages/procedures/functions
 */

@@xhb_code_release_control_822.sql






/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */




/*
 * Changes, additions or deletion of standing data
 */





/*
 * Updating of table XHB_SYS_AUDIT
 */

   

/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.2', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.2.2', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.2', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

spool off
