/*    ------------------------------------------------------------------
*     XHB_CASE_LISTING_ENTRY
*     AUD_CASE_LISTING_ENTRY
*     REPLACE TRIGGERS
*/    ------------------------------------------------------------------

ALTER TABLE XHB_CASE_LISTING_ENTRY ADD (OBS_IND VARCHAR2(1) DEFAULT NULL);


/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR CASE_LISTING_ENTRY
*/	------------------------------------------------------------------

ALTER TABLE AUD_CASE_LISTING_ENTRY ADD (OBS_IND VARCHAR2(1) DEFAULT NULL);

/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_case_listing_entry_bur_tr.sql;

commit;

/