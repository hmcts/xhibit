/*    -------------------------------------------------------------------------------
*     CTX-4216
*/    -------------------------------------------------------------------------------
ALTER TABLE XHB_DEFENDANT_ON_CASE ADD
(
    CTL_APPLIES CHAR(1)
);

ALTER TABLE AUD_DEFENDANT_ON_CASE ADD
(
    CTL_APPLIES CHAR(1)
);

@@xhb_defendantoncase_bur_tr.sql;

COMMIT;