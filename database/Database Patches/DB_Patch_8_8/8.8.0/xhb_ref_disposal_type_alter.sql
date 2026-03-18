/*    ------------------------------------------------------------------
*     Add column to XHB_REF_DISPOSAL table
*/    ------------------------------------------------------------------
ALTER TABLE XHB_REF_DISPOSAL_TYPE
	ADD D20_OTHER_SENTENCE VARCHAR2(1);


/*	------------------------------------------------------------------
/*	 Add column to AUDIT TABLE FOR AUD_REF_DISPOSAL table
*/	------------------------------------------------------------------
ALTER TABLE AUD_REF_DISPOSAL_TYPE
	ADD D20_OTHER_SENTENCE VARCHAR2(1);


/*    ------------------------------------------------------------------
*     Update DATABASE TRIGGERS
*/    ------------------------------------------------------------------
@@xhb_ref_disposal_type_bur_tr.sql

/