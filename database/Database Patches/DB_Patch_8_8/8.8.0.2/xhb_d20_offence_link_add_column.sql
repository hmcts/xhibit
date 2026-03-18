/*    ------------------------------------------------------------------
*     Add column to XHB_D20_OFFENCE_LINK table
*/    ------------------------------------------------------------------
ALTER TABLE XHB_D20_OFFENCE_LINK
	ADD CONVICTION_DATE DATE;


/*	------------------------------------------------------------------
/*	 Add column to AUDIT TABLE FOR AUD_D20_OFFENCE_LINK table
*/	------------------------------------------------------------------
ALTER TABLE AUD_D20_OFFENCE_LINK
	ADD CONVICTION_DATE DATE;


/*    ------------------------------------------------------------------
*     Update DATABASE TRIGGERS
*/    ------------------------------------------------------------------
@@xhb_d20_offence_link_bur_tr.sql

/