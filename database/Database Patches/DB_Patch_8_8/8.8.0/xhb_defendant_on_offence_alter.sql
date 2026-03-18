/*    ------------------------------------------------------------------
*     Add column to XHB_DEFENDANT_ON_OFFENCE table
*/    ------------------------------------------------------------------
/* Remove this setting of a default value at this stage due to performance hist when running in Live as part of a larger set of scripts */
ALTER TABLE XHB_DEFENDANT_ON_OFFENCE
	ADD INTERIM_D20 VARCHAR2(1);


/*	------------------------------------------------------------------
/*	 Add column to AUDIT TABLE FOR AUD_DEFENDANT_ON_OFFENCE table
*/	------------------------------------------------------------------

/* No need to set this on Audit table, not to mention the manssive performance hit when running it */
ALTER TABLE AUD_DEFENDANT_ON_OFFENCE
	ADD INTERIM_D20 VARCHAR2(1);


/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_defendant_on_offence_bur_tr.sql;

commit;