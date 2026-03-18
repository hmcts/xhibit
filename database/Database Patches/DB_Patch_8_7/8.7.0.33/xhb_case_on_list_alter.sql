/*CTX-1088*/
-- Introduce new column on the XHB_CASE_ON_LIST table and it's corresponding audit table
ALTER TABLE xhb_case_on_list 
 ADD (NHA_FIRM_LIST VARCHAR2(1));

ALTER TABLE aud_case_on_list 
 ADD (NHA_FIRM_LIST VARCHAR2(1));

-- Rebuild the database trigger to include the new column
@@xhb_case_on_list_bur_tr.sql;

COMMIT;