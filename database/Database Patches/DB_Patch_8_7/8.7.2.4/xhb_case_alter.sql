/*    -------------------------------------------------------------------------------
*     CTX-4136
*/    -------------------------------------------------------------------------------
ALTER TABLE XHB_CASE ADD
(
    S28_ELIGIBLE CHAR(1)
);
ALTER TABLE XHB_CASE ADD
(
    S28_ORDER_MADE CHAR(1)
);


ALTER TABLE AUD_CASE ADD
(
    S28_ELIGIBLE CHAR(1)
);
ALTER TABLE AUD_CASE ADD
(
    S28_ORDER_MADE CHAR(1)
);

@@xhb_case_bur_tr.sql;

COMMIT;