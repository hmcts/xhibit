--ctx-1671
-- xhb_case_listing_entry 
ALTER TABLE xhb_case_listing_entry ADD (CONSTRAINT xhb_case_list_entry_case_id_fk FOREIGN KEY (case_id) REFERENCES xhb_case (case_id));
ALTER TABLE xhb_case_listing_entry ADD (CONSTRAINT xhb_case_list_entry_crt_id_fk FOREIGN KEY (court_id) REFERENCES xhb_court (court_id));

commit;