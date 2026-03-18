/*ctx-2473*/
-- Introduce new column on the XHB_DEF_HEARING_RECORD table and it's corresponding audit table
ALTER TABLE xhb_def_hearing_record 
 ADD (FORMA_COURT_CLERK VARCHAR2(255));

ALTER TABLE aud_def_hearing_record 
 ADD (FORMA_COURT_CLERK VARCHAR2(255));

-- Rebuild the database trigger to include the new column
@@xhb_def_hearing_record_bur_tr.sql;

COMMIT;