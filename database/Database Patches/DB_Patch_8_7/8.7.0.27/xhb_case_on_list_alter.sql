/*ctx-2084*/
--xhb_case_on_list
ALTER TABLE xhb_case_on_list DROP CONSTRAINT XHB_CASE_ON_LIST_SITTING_ID_FK;
ALTER TABLE xhb_case_on_list RENAME COLUMN SITTING_ID TO SITTING_ON_LIST_ID;
ALTER TABLE aud_case_on_list RENAME COLUMN SITTING_ID TO SITTING_ON_LIST_ID;

ALTER TABLE xhb_case_on_list ADD (CONSTRAINT xhb_case_on_list_sol_fk FOREIGN KEY (SITTING_ON_LIST_ID) REFERENCES xhb_sitting_on_list (sitting_on_list_id)); 

@@xhb_case_on_list_bur_tr;

commit;


