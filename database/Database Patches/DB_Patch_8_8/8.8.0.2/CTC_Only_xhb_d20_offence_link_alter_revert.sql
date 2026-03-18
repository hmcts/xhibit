/*    ------------------------------------------------------------------
*     Rename column to XHB_D20_OFFENCE_LINK table
*/    ------------------------------------------------------------------
ALTER TABLE XHB_D20_OFFENCE_LINK
	RENAME COLUMN OFFENCE_NUMBER TO SEQ_NO;


/*	------------------------------------------------------------------
/*	 Rename column to AUDIT TABLE FOR AUD_D20_OFFENCE_LINK table
*/	------------------------------------------------------------------
ALTER TABLE AUD_D20_OFFENCE_LINK
	RENAME COLUMN OFFENCE_NUMBER TO SEQ_NO;


/*    ------------------------------------------------------------------
*     Update DATABASE TRIGGERS
*/    ------------------------------------------------------------------
@@xhb_d20_offence_link_bur_tr.sql

exit;