/*
 *    -------------------------------------------------------------------------------
 *     CTX-1474
 *    -------------------------------------------------------------------------------
 */

alter table XHB_LEGAL_AID_AMENDMENT drop (CHANGE_TYPE_ID);
alter table AUD_LEGAL_AID_AMENDMENT drop (CHANGE_TYPE_ID);

alter table XHB_LEGAL_AID_AMENDMENT add (AMENDMENT_TYPE VARCHAR2(15));
alter table AUD_LEGAL_AID_AMENDMENT add (AMENDMENT_TYPE VARCHAR2(15));

@@xhb_legal_aid_amendment_bur_tr.sql;

commit;
