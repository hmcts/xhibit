/*CTX-3689*/
-- Introduce new column on the XHB_DEF_ON_CASE_ON_LIST table and it's corresponding audit table
ALTER TABLE xhb_def_on_case_on_list 
 ADD (is_court_room_list_entry VARCHAR2(1));

ALTER TABLE aud_def_on_case_on_list 
 ADD (is_court_room_list_entry VARCHAR2(1));

-- Rebuild the database trigger to include the new column
@@xhb_def_on_case_on_list_bur_tr.sql;

COMMIT;