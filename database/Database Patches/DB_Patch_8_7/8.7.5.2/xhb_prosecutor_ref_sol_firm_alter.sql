-- CTX-4465
-- Introduce new column on the XHB_PROSECUTOR_REF_SOL_FIRM table and it's corresponding audit table
ALTER TABLE xhb_prosecutor_ref_sol_firm 
 ADD (legal_aid_order_id NUMBER(8));

ALTER TABLE aud_prosecutor_ref_sol_firm 
 ADD (legal_aid_order_id NUMBER(8));
 
 -- Setup foreign key constraints on the new columns
ALTER TABLE xhb_prosecutor_ref_sol_firm ADD (CONSTRAINT xhb_prsf_laoid_fk FOREIGN KEY (legal_aid_order_id) REFERENCES xhb_legal_aid_order (legal_aid_order_id)); 

-- Rebuild the database trigger to include the new column
@@xhb_prosecutorrefsolfirm_bur_tr.sql;

commit;