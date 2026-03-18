/*CTX-2382*/
-- Introduce new columns on the XHB_SCHEDULED_HEARING table and it's corresponding audit table
ALTER TABLE xhb_scheduled_hearing 
 ADD ( ref_cracked_effective_id		NUMBER(8) );

ALTER TABLE aud_scheduled_hearing 
 ADD ( ref_cracked_effective_id		NUMBER(8) );

-- Setup foreign key constraint for the new column
ALTER TABLE xhb_scheduled_hearing ADD (CONSTRAINT xhb_scheduled_hearing_rce_fk FOREIGN KEY (ref_cracked_effective_id) REFERENCES xhb_ref_cracked_effective (ref_cracked_effective_id)); 

-- Rebuild the database trigger to include the new column
@@xhb_scheduled_hearing_bur_tr.sql;

COMMIT;