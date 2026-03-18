/*
 * Filename:    Xhibit_DB_Patch_8_6_4.sql (Feb 2016 Legislative release)
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
 *  13/10/2015	S Atwell	Changes for DB release 8.6.4
 *
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_6_4_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

# HK includes fix for case_pros_agency_id fk error
@@xhb_housekeeping_pkg_b.sql

# Create new D20 offence code table and populate
@@xhb_d20_offence_codes_create.sql;
@@xhb_d20_offence_codes_bir_tr.sql;
@@xhb_d20_offence_codes_bur_tr.sql;
@@load_d20_offence_data.sql;

# Create new Monetary Orders ref disposals table and populate
@@xhb_ref_mon_ord_disposals_create.sql;
@@xhb_ref_mon_ord_disp_bir_tr.sql;
@@xhb_ref_mon_ord_disp_bur_tr.sql;
@@load_ref_mon_ord_disposals_data.sql;

# Create new Collection Centre table for monetary orders and populate
@@CollectionCentreTableAndData.sql;
@@load_collection_centre_data.sql;

# New roles for Monetary Orders
@@OrdersSecurity.sql;

# No longer needed as added in 8.6.3 release
## AlterOrderTable.sql;

# New order type/template for Monetary Orders
@@MonetaryOrders.sql;

# Other order updates/additions
@@OrderUpdates.sql;


COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.4', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.6.4', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.6.4', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.6.4', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.6.4', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off


