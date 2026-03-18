/*    ------------------------------------------------------------------
*     XHB_REF_LISTING_DATA
*     AUD_REF_LISTING_DATA
*     REPLACE TRIGGERS
*/    ------------------------------------------------------------------

ALTER TABLE XHB_REF_LISTING_DATA ADD (OBS_IND VARCHAR2(1) DEFAULT NULL);


/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR REF_LISTING_DATA
*/	------------------------------------------------------------------

ALTER TABLE AUD_REF_LISTING_DATA ADD (OBS_IND VARCHAR2(1) DEFAULT NULL);

/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_ref_listing_data_bur_tr.sql;

commit;

/