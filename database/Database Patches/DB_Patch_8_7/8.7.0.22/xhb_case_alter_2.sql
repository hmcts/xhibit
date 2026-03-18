--ctx-1657
--Add DATE_TRANS_TO to xhb_case
ALTER TABLE xhb_case ADD DATE_TRANS_TO DATE;

ALTER TABLE aud_case ADD DATE_TRANS_TO DATE;

/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_case_bur_tr.sql;
@@xhb_case_ai_tr.sql;

COMMIT;