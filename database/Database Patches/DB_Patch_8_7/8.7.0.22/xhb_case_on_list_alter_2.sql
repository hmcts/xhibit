--xhb_case_on_list
ALTER TABLE xhb_case_on_list MODIFY court_room_id NUMBER(8);
ALTER TABLE xhb_case_on_list RENAME COLUMN floating_case TO floater_case;

ALTER TABLE aud_case_on_list MODIFY court_room_id NUMBER(8);
ALTER TABLE aud_case_on_list RENAME COLUMN floating_case TO floater_case;

ALTER TABLE xhb_case_on_list ADD (CONSTRAINT XHB_CASE_ON_LIST_CR_IID_ID_FK FOREIGN KEY (cracked_ineffective_id) REFERENCES xhb_ref_system_code (REF_SYSTEM_CODE_ID));

@@xhb_case_on_list_bur_tr.sql;

commit;