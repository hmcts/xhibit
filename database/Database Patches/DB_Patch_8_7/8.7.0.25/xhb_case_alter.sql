--ctx-1914
--xhb_case
ALTER TABLE xhb_case ADD CASE_GROUP_NUMBER NUMBER(7);
ALTER TABLE aud_case ADD CASE_GROUP_NUMBER NUMBER(7);


@@xhb_case_bur_tr;
@@xhb_case_ai_tr;

commit;
