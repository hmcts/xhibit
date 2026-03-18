/*CTX-2280 and CTX-2237*/
-- Introduce new columns on the XHB_CASE_ON_LIST table and it's corresponding audit table
ALTER TABLE xhb_case_on_list 
 ADD (case_diary_fixture_id		NUMBER(8)
     ,date_of_removal			DATE
	 ,list_note_predefined_id	NUMBER(8)
	 ,list_note_text			VARCHAR2(100)
	 ,parent_case_on_list_id    NUMBER(8)
     );

ALTER TABLE aud_case_on_list 
 ADD (case_diary_fixture_id		NUMBER(8)
     ,date_of_removal			DATE
	 ,list_note_predefined_id	NUMBER(8)
	 ,list_note_text			VARCHAR2(100)
	 ,parent_case_on_list_id    NUMBER(8)
     );

-- Setup foreign key constraints on the new columns
ALTER TABLE xhb_case_on_list ADD (CONSTRAINT xhb_case_on_list_cdf_fk FOREIGN KEY (case_diary_fixture_id) REFERENCES xhb_case_diary_fixture (case_diary_fixture_id)); 
ALTER TABLE xhb_case_on_list ADD (CONSTRAINT xhb_case_on_list_lnpd_fk FOREIGN KEY (list_note_predefined_id) REFERENCES xhb_ref_listing_data (ref_listing_data_id));
ALTER TABLE xhb_case_on_list ADD (CONSTRAINT xhb_case_on_list_col_fk FOREIGN KEY (parent_case_on_list_id) REFERENCES xhb_case_on_list (case_on_list_id)); 

-- Rebuild the database trigger to include the new column
@@xhb_case_on_list_bur_tr.sql;

COMMIT;