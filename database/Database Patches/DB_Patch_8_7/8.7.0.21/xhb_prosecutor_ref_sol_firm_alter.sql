/*
 *    -------------------------------------------------------------------------------
 *     CTX-1472
 *    -------------------------------------------------------------------------------
 */

alter table XHB_PROSECUTOR_REF_SOL_FIRM drop (LIST_DATE_SENT, LIST_DATE_RECEIVED);
alter table AUD_PROSECUTOR_REF_SOL_FIRM drop (LIST_DATE_SENT, LIST_DATE_RECEIVED);

@@xhb_prosecutorrefsolfirm_bur_tr.sql;

commit;
