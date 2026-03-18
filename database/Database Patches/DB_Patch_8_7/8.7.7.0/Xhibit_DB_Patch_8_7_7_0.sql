/*
 * Filename:    Xhibit_DB_Patch_8_7_7_0.sql (CPPX functionality release)
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
 * 13/11/19		N Walters		adding CPP Formatting table
 * 13/11/19     E Patterson		adding CPP_LIST table
 * 14/11/19	N Walters	Adding in staging table
 * 14/11/19     E Patterson     insert row into XHB_CONFIG_PROP
 * 15/11/19		E Pattterson    update XHB_COURT
 * 18/11/19     E Patterson     amend status column in xhb_cpp_list
 * 27/11/19	N Walters	Drop foreign key constraint (cpp-64)
 */
set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_7_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

@xhb_cppstaging_create.sql;
@xhb_cppformatting_create.sql;
@xhb_cpp_list_create.sql;
@xhb_config_prop_insert.sql;
@xhb_cppformattingmerge_create.sql;

@xhb_court_column_update.sql;

@drop_fk_constraint.sql;

@xhb_formatting_pkg_h.sql;
@xhb_formatting_pkg_b.sql;


DELETE FROM XHB_VERSION;


INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.6.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.7.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.7.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.7.0', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.7.0', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.7.0', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
