/*
 *    -------------------------------------------------------------------------------
 *    CTX-1468
 *    -------------------------------------------------------------------------------
 */

alter table XHB_CASE drop (BENCH_WARRANT, BENCH_WARRANT_DEF_SENTENCE);
alter table AUD_CASE drop (BENCH_WARRANT, BENCH_WARRANT_DEF_SENTENCE);

@@xhb_case_bur_tr.sql;
@@xhb_case_ai_tr.sql;

commit;
