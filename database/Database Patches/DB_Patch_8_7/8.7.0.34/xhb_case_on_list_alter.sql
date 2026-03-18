/*CTX-2521*/
-- Introduce new column on the XHB_CASE_ON_LIST table and it's corresponding audit table
ALTER TABLE xhb_case_on_list 
 ADD (vacation_pre_defined_rson_id NUMBER(8));

ALTER TABLE aud_case_on_list 
 ADD (vacation_pre_defined_rson_id NUMBER(8));
 
 -- Setup foreign key constraints on the new columns
ALTER TABLE xhb_case_on_list ADD (CONSTRAINT xhb_case_on_list_vpdri_fk FOREIGN KEY (vacation_pre_defined_rson_id) REFERENCES xhb_ref_system_code (ref_system_code_id)); 

-- Rebuild the database trigger to include the new column
@@xhb_case_on_list_bur_tr.sql;

COMMIT;