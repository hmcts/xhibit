/*    ------------------------------------------------------------------
*     Add column to XHB_PROSECUTOR_REF_SOL_FIRM table
*/    ------------------------------------------------------------------

ALTER TABLE XHB_PROSECUTOR_REF_SOL_FIRM
	ADD OBS_IND VARCHAR2(1);

ALTER TABLE AUD_PROSECUTOR_REF_SOL_FIRM
	ADD OBS_IND VARCHAR2(1);

	
/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_prosecutor_ref_sol_firm_bur_tr.sql

COMMIT;
/