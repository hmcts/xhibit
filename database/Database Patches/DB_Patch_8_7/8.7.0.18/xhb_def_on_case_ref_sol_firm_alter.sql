/*    ------------------------------------------------------------------
*     Add column to XHB_DEF_ON_CASE_REF_SOL_FIRM table
*/    ------------------------------------------------------------------

ALTER TABLE XHB_DEF_ON_CASE_REF_SOL_FIRM
	ADD OBS_IND VARCHAR2(1);

ALTER TABLE AUD_DEF_ON_CASE_REF_SOL_FIRM
	ADD OBS_IND VARCHAR2(1);

	
/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_def_on_case_ref_sol_firm_bur_tr.sql

COMMIT;
/