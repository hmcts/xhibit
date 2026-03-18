ALTER TABLE XHB_CASE ADD
(CIVIL_UNREST VARCHAR2(1) 
);

ALTER TABLE AUD_CASE ADD
(CIVIL_UNREST VARCHAR2(1) 
);

@@xhb_case_ai_tr.sql;
@@xhb_case_bur_tr.sql;

COMMIT;
/