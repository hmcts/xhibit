/*    ------------------------------------------------------------------
*     Add column to XHB_D20_OFFENCE_CODES table
*/    ------------------------------------------------------------------
ALTER TABLE XHB_D20_OFFENCE_CODES ADD (
	MAND_DISQ VARCHAR2(1),
	MAND_ALC_DRUG_LEVEL VARCHAR2(1),
	MAND_DTETP VARCHAR2(1)
);


/*	------------------------------------------------------------------
/*	 Add column to AUDIT TABLE FOR AUD_D20_OFFENCE_CODES table
*/	------------------------------------------------------------------
ALTER TABLE AUD_D20_OFFENCE_CODES ADD (
	MAND_DISQ VARCHAR2(1),
	MAND_ALC_DRUG_LEVEL VARCHAR2(1),
	MAND_DTETP VARCHAR2(1)
);


/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_d20_offence_codes_bur_tr.sql;

commit;