/*
 *    -------------------------------------------------------------------------------
 *     CTX-1474
 *    -------------------------------------------------------------------------------
 */

alter table XHB_LEGAL_AID_ORDER drop (SOLICITOR_REFERENCE, REF_SOLICITOR_FIRM_ID, REP_START_DATE, REP_END_DATE);
alter table AUD_LEGAL_AID_ORDER drop (SOLICITOR_REFERENCE, REF_SOLICITOR_FIRM_ID, REP_START_DATE, REP_END_DATE);

@@xhb_legal_aid_order_bur_tr.sql;

commit;
