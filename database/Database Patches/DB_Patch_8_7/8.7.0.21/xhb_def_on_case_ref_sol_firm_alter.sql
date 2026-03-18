/*
 *    -------------------------------------------------------------------------------
 *     CTX-1470
 *    -------------------------------------------------------------------------------
 */

alter table XHB_DEF_ON_CASE_REF_SOL_FIRM drop (LIST_DATE_SENT, LIST_DATE_RECEIVED);
alter table AUD_DEF_ON_CASE_REF_SOL_FIRM drop (LIST_DATE_SENT, LIST_DATE_RECEIVED);

@@xhb_def_case_sol_firm_bur_tr.sql;

commit;
